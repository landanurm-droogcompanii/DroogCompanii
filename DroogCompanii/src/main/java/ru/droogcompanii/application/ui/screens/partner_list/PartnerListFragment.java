package ru.droogcompanii.application.ui.screens.partner_list;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.common.base.Optional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ru.droogcompanii.application.DroogCompaniiApplication;
import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.ui.screens.partner_details.PartnerDetailsActivity;
import ru.droogcompanii.application.util.Objects;
import ru.droogcompanii.application.util.StateManager;
import ru.droogcompanii.application.util.ui.able_to_start_task.FragmentAbleToStartTask;
import ru.droogcompanii.application.util.ui.able_to_start_task.TaskNotBeInterruptedDuringConfigurationChange;

/**
 * Created by ls on 22.01.14.
 */
public class PartnerListFragment extends FragmentAbleToStartTask implements AdapterView.OnItemClickListener {

    private static class Key {
        public static final String INPUT_PROVIDER = "INPUT_PROVIDER";
        public static final String SEARCH_RESULTS = "SEARCH_RESULTS";
        public static final String CURRENT_COMPARATOR = "CURRENT_COMPARATOR";
        public static final String IS_SEARCH_RESULTS_READY = "IS_SEARCH_RESULTS_READY";
        public static final String IS_SEARCH_RESULTS_SORTED = "IS_SEARCH_RESULTS_SORTED";
    }

    private static class TaskRequestCode {
        public static final int EXTRACT_SEARCH_RESULTS = 145;
        public static final int SORTING = EXTRACT_SEARCH_RESULTS + 1;
    }

    private boolean isSearchResultsReady;
    private boolean isSearchResultsSorted;
    private PartnerListActivity.InputProvider inputProvider;
    private List<Partner> searchResults;
    private Optional<Comparator<Partner>> currentComparator;
    private PartnerListAdapter adapter;


    public static PartnerListFragment newInstance(PartnerListActivity.InputProvider inputProvider) {
        Bundle args = new Bundle();
        args.putSerializable(Key.INPUT_PROVIDER, (Serializable) inputProvider);
        PartnerListFragment partnerListFragment = new PartnerListFragment();
        partnerListFragment.setArguments(args);
        return partnerListFragment;
    }


    private final StateManager STATE_MANAGER = new StateManager() {
        @Override
        public void initStateByDefault() {
            inputProvider = (PartnerListActivity.InputProvider)
                    getArguments().getSerializable(Key.INPUT_PROVIDER);
            currentComparator = Optional.absent();
            isSearchResultsReady = false;
            isSearchResultsSorted = false;
            searchResults = new ArrayList<Partner>();
            startTaskExtractSearchResults();
        }

        @Override
        public void restoreState(Bundle savedInstanceState) {
            inputProvider = (PartnerListActivity.InputProvider)
                    savedInstanceState.getSerializable(Key.INPUT_PROVIDER);
            currentComparator = (Optional<Comparator<Partner>>) savedInstanceState.getSerializable(Key.CURRENT_COMPARATOR);
            searchResults = (List<Partner>) savedInstanceState.getSerializable(Key.SEARCH_RESULTS);
            isSearchResultsReady = savedInstanceState.getBoolean(Key.IS_SEARCH_RESULTS_READY);
            isSearchResultsSorted = savedInstanceState.getBoolean(Key.IS_SEARCH_RESULTS_SORTED);
        }

        @Override
        public void saveState(Bundle outState) {
            outState.putSerializable(Key.INPUT_PROVIDER, (Serializable) inputProvider);
            outState.putSerializable(Key.CURRENT_COMPARATOR, currentComparator);
            outState.putSerializable(Key.SEARCH_RESULTS, (Serializable) searchResults);
            outState.putBoolean(Key.IS_SEARCH_RESULTS_READY, isSearchResultsReady);
            outState.putBoolean(Key.IS_SEARCH_RESULTS_SORTED, isSearchResultsSorted);
        }
    };

    private void startTaskExtractSearchResults() {
        isSearchResultsReady = false;
        isSearchResultsSorted = false;
        TaskNotBeInterruptedDuringConfigurationChange task = new TaskNotBeInterruptedDuringConfigurationChange() {
            @Override
            protected Serializable doInBackground(Void... voids) {
                return (Serializable) inputProvider.getPartners(DroogCompaniiApplication.getContext());
            }
        };
        startTask(TaskRequestCode.EXTRACT_SEARCH_RESULTS, task);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        STATE_MANAGER.initState(savedInstanceState);

        if (isSearchResultsReady) {
            initSearchResultList();
        }

        getListView().setOnItemClickListener(this);
    }

    private void initSearchResultList() {
        adapter = new PartnerListAdapter(getActivity(), searchResults);
        setListAdapter(adapter);
        if (adapter.isEmpty()) {
            setEmptyListView();
        }
    }

    private void setListAdapter(ListAdapter adapter) {
        getListView().setAdapter(adapter);
    }

    private void setEmptyListView() {
        Activity activity = getActivity();
        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        View emptyView = layoutInflater.inflate(R.layout.view_no_search_results, null);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        );
        activity.addContentView(emptyView, layoutParams);
        getListView().setEmptyView(emptyView);
    }

    private ListView getListView() {
        return (ListView) getView().findViewById(R.id.listView);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        STATE_MANAGER.saveState(outState);
    }

    public void setSearchResult(List<Partner> searchResults) {
        this.searchResults = searchResults;
        isSearchResultsReady = true;
        isSearchResultsSorted = false;
        if (currentComparator.isPresent()) {
            startSortingTask();
        } else {
            initSearchResultList();
        }
    }

    private void startSortingTask() {
        startTask(TaskRequestCode.SORTING, new TaskNotBeInterruptedDuringConfigurationChange() {
            @Override
            protected Serializable doInBackground(Void... voids) {
                return (Serializable) sortedResults();
            }

            private List<Partner> sortedResults() {
                List<Partner> copy = new ArrayList<Partner>(searchResults);
                Collections.sort(copy, currentComparator.get());
                return copy;
            }
        });
    }

    @Override
    public void onTaskResult(int requestCode, int resultCode, Serializable result) {
        if (resultCode != Activity.RESULT_OK) {
            getActivity().finish();
            return;
        }
        if (requestCode == TaskRequestCode.EXTRACT_SEARCH_RESULTS) {
            onSearchResultsExtracted(result);
        } else if (requestCode == TaskRequestCode.SORTING) {
            onSearchResultsSorted(result);
        }
    }

    private void onSearchResultsExtracted(Serializable result) {
        List<Partner> searchResults = (List<Partner>) result;
        setSearchResult(searchResults);
    }

    private void onSearchResultsSorted(Serializable result) {
        searchResults = (List<Partner>) result;
        isSearchResultsSorted = true;
        initSearchResultList();
    }

    public void setComparator(Comparator<Partner> newComparator) {
        if (newComparator == null) {
            throw new IllegalArgumentException(PartnerListFragment.class.getName() +
                    ".setComparator(comparator):  comparator should be not null");
        }
        if (currentComparator.isPresent() && Objects.equals(currentComparator.get(), newComparator)) {
            return;
        }
        currentComparator = Optional.of(newComparator);
        isSearchResultsSorted = false;
        if (isSearchResultsReady) {
            startSortingTask();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Partner partner = adapter.getItem(position);
        onPartnerClicked(partner);
    }

    private void onPartnerClicked(Partner partner) {
        Activity activity = getActivity();
        if (activity != null) {
            PartnerDetailsActivity.startWithoutFilters(activity, partner);
        }
    }

}
