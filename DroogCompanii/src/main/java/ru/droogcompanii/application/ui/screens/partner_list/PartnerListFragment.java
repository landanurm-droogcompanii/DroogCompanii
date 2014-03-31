package ru.droogcompanii.application.ui.screens.partner_list;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.google.common.base.Optional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.util.ui.able_to_start_task.AbleToStartTask;
import ru.droogcompanii.application.util.ui.able_to_start_task.TaskNotBeInterruptedDuringConfigurationChange;
import ru.droogcompanii.application.util.ui.able_to_start_task.TaskResultReceiver;
import ru.droogcompanii.application.util.Objects;

/**
 * Created by ls on 22.01.14.
 */
public class PartnerListFragment extends ListFragment
        implements AdapterView.OnItemClickListener, TaskResultReceiver {

    private static final int TASK_REQUEST_CODE_SORT_TASK =
            PartnerListActivity.LOWER_BOUND_VALID_TASK_REQUEST_CODE + 1;

    public static interface Callbacks {
        void onPartnerClick(Partner partner);
        void onFound();
        void onNotFound();
    }

    private static final String KEY_VISIBILITY = "KEY_VISIBILITY";
    private static final String KEY_SEARCH_RESULTS = "KEY_SEARCH_RESULTS";
    private static final String KEY_CURRENT_COMPARATOR = "KEY_CURRENT_COMPARATOR";
    private static final String KEY_IS_SEARCH_RESULTS_READY = "KEY_IS_SEARCH_RESULTS_READY";
    private static final String KEY_IS_SEARCH_RESULTS_SORTED = "KEY_IS_SEARCH_RESULTS_SORTED";

    private AbleToStartTask ableToStartTask;
    private boolean isSearchResultsReady;
    private boolean isSearchResultsSorted;
    private Callbacks callbacks;
    private List<Partner> searchResults;
    private Optional<Comparator<Partner>> currentComparator;
    private PartnerListAdapter adapter;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callbacks = (Callbacks) activity;
        ableToStartTask = (AbleToStartTask) activity;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState == null) {
            initStateByDefault();
        } else {
            restoreState(savedInstanceState);
        }

        if (isSearchResultsReady && isSearchResultsSorted) {
            initSearchResultList();
        }

        getListView().setOnItemClickListener(this);
    }

    private void initStateByDefault() {
        currentComparator = Optional.absent();
        isSearchResultsReady = false;
        isSearchResultsSorted = false;
        searchResults = new ArrayList<Partner>();
    }

    private void restoreState(Bundle savedInstanceState) {
        currentComparator = (Optional<Comparator<Partner>>) savedInstanceState.getSerializable(KEY_CURRENT_COMPARATOR);
        searchResults = (List<Partner>) savedInstanceState.getSerializable(KEY_SEARCH_RESULTS);
        isSearchResultsReady = savedInstanceState.getBoolean(KEY_IS_SEARCH_RESULTS_READY);
        isSearchResultsSorted = savedInstanceState.getBoolean(KEY_IS_SEARCH_RESULTS_SORTED);

        int visibility = savedInstanceState.getInt(KEY_VISIBILITY);
        getView().setVisibility(visibility);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveStateInto(outState);
    }

    private void saveStateInto(Bundle outState) {
        outState.putSerializable(KEY_CURRENT_COMPARATOR, currentComparator);
        outState.putSerializable(KEY_SEARCH_RESULTS, (Serializable) searchResults);
        outState.putBoolean(KEY_IS_SEARCH_RESULTS_READY, isSearchResultsReady);
        outState.putBoolean(KEY_IS_SEARCH_RESULTS_SORTED, isSearchResultsSorted);

        int visibility = getView().getVisibility();
        outState.putInt(KEY_VISIBILITY, visibility);
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
        TaskNotBeInterruptedDuringConfigurationChange task = new SortTask(searchResults, currentComparator.get());
        ableToStartTask.startTask(TASK_REQUEST_CODE_SORT_TASK, task);
    }

    @Override
    public void onTaskResult(int requestCode, int resultCode, Serializable result) {
        if (resultCode != Activity.RESULT_OK) {
            getActivity().finish();
            return;
        }
        if (requestCode == TASK_REQUEST_CODE_SORT_TASK) {
            onSortingCompleted(result);
        }
    }

    private void onSortingCompleted(Serializable result) {
        searchResults = (List<Partner>) result;
        isSearchResultsSorted = true;
        initSearchResultList();
    }

    private void initSearchResultList() {
        adapter = new PartnerListAdapter(getActivity(), searchResults);
        setListAdapter(adapter);
        if (adapter.isEmpty()) {
            callbacks.onNotFound();
            setEmptyListView();
        } else {
            callbacks.onFound();
        }
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

    public void setComparator(Comparator<Partner> newComparator) {
        if (newComparator == null) {
            throw new IllegalArgumentException(PartnerListFragment.class.getName() +
                    ".setComparator(comparator):  comparator should be not null");
        }
        if (currentComparator.isPresent() && Objects.equals(currentComparator.get(), newComparator)) {
            return;
        }
        currentComparator = Optional.of(newComparator);
        if (isSearchResultsReady) {
            startSortingTask();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Partner partner = adapter.getItem(position);
        callbacks.onPartnerClick(partner);
    }

    public void show() {
        getView().setVisibility(View.VISIBLE);
    }

    public void hide() {
        getView().setVisibility(View.INVISIBLE);
    }

}
