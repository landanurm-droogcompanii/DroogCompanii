package ru.droogcompanii.application.ui.screens.search_result_list;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.common.base.Optional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerCategory;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.ui.screens.search_result_list.adapter.SearchResultListAdapter;
import ru.droogcompanii.application.ui.screens.search_result_list.adapter.SearchResultListItem;
import ru.droogcompanii.application.util.StateManager;
import ru.droogcompanii.application.util.ui.able_to_start_task.FragmentAbleToStartTask;
import ru.droogcompanii.application.util.ui.able_to_start_task.TaskNotBeInterruptedDuringConfigurationChange;

/**
 * Created by ls on 11.03.14.
 */
public class SearchResultListFragment extends FragmentAbleToStartTask {

    public static class Input implements Serializable {
        public final List<PartnerCategory> categories = new ArrayList<PartnerCategory>();
        public final List<Partner> partners = new ArrayList<Partner>();
        public final List<PartnerPoint> points = new ArrayList<PartnerPoint>();

        public final boolean isEmpty() {
            return categories.isEmpty() && partners.isEmpty() && points.isEmpty();
        }
    }

    public static interface InputProvider {
        Input getInput(Context context);
    }


    private static final int TASK_REQUEST_CODE_INPUT_RECEIVER = 140;

    private static class Key {
        private static final String INPUT_PROVIDER = "INPUT_PROVIDER";
        private static final String INPUT = "INPUT";
    }


    private InputProvider inputProvider;
    private Optional<Input> input;


    private final StateManager STATE_MANAGER = new StateManager() {
        @Override
        public void initStateByDefault() {
            inputProvider = (InputProvider) getArguments().getSerializable(Key.INPUT_PROVIDER);
            input = Optional.absent();
            startInputReceiverTask();
        }

        @Override
        public void restoreState(Bundle savedInstanceState) {
            inputProvider = (InputProvider) savedInstanceState.getSerializable(Key.INPUT_PROVIDER);
            input = (Optional<Input>) savedInstanceState.getSerializable(Key.INPUT);
            updateSearchResultListIfInputIsPresent();
        }

        @Override
        public void saveState(Bundle outState) {
            outState.putSerializable(Key.INPUT_PROVIDER, (Serializable) inputProvider);
            outState.putSerializable(Key.INPUT, input);
        }
    };


    public static SearchResultListFragment newInstance(String query) {
        SearchResultListFragment fragment = new SearchResultListFragment();
        Bundle args = new Bundle();
        args.putSerializable(Key.INPUT_PROVIDER, new InputProviderBySearchQuery(query));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_result_list, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        STATE_MANAGER.initState(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        STATE_MANAGER.saveState(outState);
    }

    private void startInputReceiverTask() {
        startTask(TASK_REQUEST_CODE_INPUT_RECEIVER, new TaskNotBeInterruptedDuringConfigurationChange() {
            @Override
            protected Serializable doInBackground(Void... voids) {
                return inputProvider.getInput(getActivity());
            }
        });
    }

    @Override
    public void onTaskResult(int requestCode, int resultCode, Serializable result) {
        if (requestCode == TASK_REQUEST_CODE_INPUT_RECEIVER) {
            onInputReceivingFinished(resultCode, result);
        }
    }

    private void onInputReceivingFinished(int resultCode, Serializable result) {
        if (resultCode != Activity.RESULT_OK) {
            getActivity().finish();
            return;
        }
        input = Optional.of((Input) result);
        updateSearchResultList();
    }

    private void updateSearchResultList() {
        if (isInputEmpty()) {
            initEmptyListView();
        } else {
            initListView();
        }
    }

    private boolean isInputEmpty() {
        if (input.isPresent()) {
            return input.get().isEmpty();
        }
        return true;
    }

    private void initListView() {
        ListView listView = (ListView) getView().findViewById(R.id.searchResultList);
        final SearchResultListAdapter adapter = new SearchResultListAdapter(getActivity(), input.get());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                onSearchResultListItemClicked(adapter.getItem(position));
            }
        });
        setPadding(listView);
    }

    private void setPadding(ListView listView) {
        int horizontalPadding = getActivity().getResources()
                .getDimensionPixelSize(R.dimen.horizontalPaddingOfSearchResultListItem);
        listView.setPadding(horizontalPadding, 0, horizontalPadding, 0);
    }

    private void initEmptyListView() {
        ListView listView = (ListView) getView().findViewById(R.id.searchResultList);
        View emptyListView = getView().findViewById(R.id.noSearchResults);
        listView.setEmptyView(emptyListView);
    }

    private void onSearchResultListItemClicked(SearchResultListItem item) {
        item.onClick(getActivity());
    }

    private void updateSearchResultListIfInputIsPresent() {
        if (input.isPresent()) {
            updateSearchResultList();
        }
    }

}
