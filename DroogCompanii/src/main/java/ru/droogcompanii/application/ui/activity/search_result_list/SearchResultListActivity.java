package ru.droogcompanii.application.ui.activity.search_result_list;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.io.Serializable;
import java.util.List;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.data.searchable_sortable_listing.SearchResult;
import ru.droogcompanii.application.ui.activity.partner_details.PartnerDetailsActivity;
import ru.droogcompanii.application.ui.activity.search.SearchResultProvider;
import ru.droogcompanii.application.ui.activity.search_result_map.SearchResultMapActivity;
import ru.droogcompanii.application.ui.fragment.partner_points_map.PartnerPointsProvider;
import ru.droogcompanii.application.ui.fragment.search_result.SearchResultFragment;
import ru.droogcompanii.application.ui.helpers.ActionBarActivityWithGoToMapItem;
import ru.droogcompanii.application.ui.helpers.FragmentRemover;
import ru.droogcompanii.application.ui.helpers.task.TaskFragmentHolder;
import ru.droogcompanii.application.util.Keys;

/**
 * Created by ls on 14.01.14.
 */
public class SearchResultListActivity extends ActionBarActivityWithGoToMapItem
                implements SearchResultFragment.Callbacks, TaskFragmentHolder.Callbacks {

    private boolean isGoToMapItemVisible;
    private SearchResultFragment searchResultFragment;
    private SearchResultProvider searchResultProvider;


    public static void start(Context context, SearchResultProvider searchResultProvider) {
        Intent intent = new Intent(context, SearchResultListActivity.class);
        intent.putExtra(Keys.searchResultProvider, (Serializable) searchResultProvider);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        init(savedInstanceState);

        searchResultFragment = (SearchResultFragment)
                getSupportFragmentManager().findFragmentById(R.id.searchResultFragment);

        if (savedInstanceState == null) {
            searchResultFragment.hide();
            startTask();
        }
    }

    private void init(Bundle savedInstanceState) {
        searchResultProvider = getSearchResultProvider(savedInstanceState);
        setTitle(searchResultProvider.getTitle(this));
        updateGoToMapItemVisible(getIsGoToMapItemVisible(savedInstanceState));
    }

    private SearchResultProvider getSearchResultProvider(Bundle savedInstanceState) {
        Bundle bundleWithResult = (savedInstanceState != null) ? savedInstanceState : getIntent().getExtras();
        return (SearchResultProvider) bundleWithResult.getSerializable(Keys.searchResultProvider);
    }

    private boolean getIsGoToMapItemVisible(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            return false;
        }
        return savedInstanceState.getBoolean(Keys.isGoToMapItemVisible);
    }

    private void updateGoToMapItemVisible(boolean visible) {
        isGoToMapItemVisible = visible;
        updateGoToMapItemVisible();
    }

    @Override
    protected boolean isGoToMapItemVisible() {
        return isGoToMapItemVisible;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(Keys.searchResultProvider, (Serializable) searchResultProvider);
        outState.putBoolean(Keys.isGoToMapItemVisible, isGoToMapItemVisible);
    }

    private void startTask() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        SearchResultTaskFragmentHolder taskFragment = new SearchResultTaskFragmentHolder();
        Bundle args = getIntent().getExtras();
        taskFragment.setArguments(args);
        transaction.add(R.id.taskFragmentContainer, taskFragment);
        transaction.commit();
    }

    @Override
    public void onTaskFinished(int resultCode, Serializable result) {
        if (resultCode == RESULT_OK) {
            onTaskFinishedSuccessfully(result);
        } else {
            onTaskCancelled();
        }
    }

    private void onTaskFinishedSuccessfully(Serializable result) {
        FragmentRemover.removeFragmentByContainerId(this, R.id.taskFragmentContainer);
        showSearchResult(result);
    }

    private void onTaskCancelled() {
        finish();
    }

    private void showSearchResult(Serializable result) {
        List<SearchResult<Partner>> searchResults = (List<SearchResult<Partner>>) result;
        searchResultFragment.show();
        searchResultFragment.setSearchResult(searchResults);
    }

    @Override
    public void onPartnerClick(Partner partner) {
        List<PartnerPoint> partnerPoints = searchResultProvider.getPointsOfPartner(this, partner);
        PartnerDetailsActivity.start(this, partner, partnerPoints);
    }

    @Override
    public void onNotFound() {
        updateGoToMapItemVisible(false);
    }

    @Override
    public void onFound() {
        updateGoToMapItemVisible(true);
    }

    @Override
    protected void onNeedToGoToMap() {
        PartnerPointsProvider partnerPointsProvider =
                new AllPartnerPointsFromSearchResultProvider(searchResultProvider);
        showResultOnMap(partnerPointsProvider);
    }

    private void showResultOnMap(PartnerPointsProvider partnerPointsProvider) {
        Intent intent = new Intent(this, SearchResultMapActivity.class);
        intent.putExtra(Keys.partnerPointsProvider, (Serializable) partnerPointsProvider);
        startActivity(intent);
    }

    private static class AllPartnerPointsFromSearchResultProvider implements PartnerPointsProvider, Serializable {
        private final SearchResultProvider searchResultProvider;

        AllPartnerPointsFromSearchResultProvider(SearchResultProvider searchResultProvider) {
            this.searchResultProvider = searchResultProvider;
        }

        @Override
        public String getTitle(Context context) {
            return searchResultProvider.getTitle(context);
        }

        @Override
        public List<PartnerPoint> getPartnerPoints(Context context) {
            return searchResultProvider.getAllPartnerPoints(context);
        }
    }

}
