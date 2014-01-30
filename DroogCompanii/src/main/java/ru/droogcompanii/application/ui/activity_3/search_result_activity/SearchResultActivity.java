package ru.droogcompanii.application.ui.activity_3.search_result_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import java.io.Serializable;
import java.util.List;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.ui.activity_3.partner_activity.PartnerActivity;
import ru.droogcompanii.application.ui.activity_3.search_activity.SearchResultProvider;
import ru.droogcompanii.application.ui.activity_3.search_result_map_activity.SearchResultMapActivity;
import ru.droogcompanii.application.ui.fragment.partner_points_map_fragment.PartnerPointsProvider;
import ru.droogcompanii.application.ui.fragment.search_result_fragment.SearchResultFragment;
import ru.droogcompanii.application.ui.helpers.task.TaskFragmentHolder;
import ru.droogcompanii.application.util.Keys;

/**
 * Created by ls on 14.01.14.
 */
public class SearchResultActivity extends FragmentActivity
                implements SearchResultFragment.OnPartnerClickListener, TaskFragmentHolder.Callbacks {

    private boolean isFirstLaunched;
    private SearchResultFragment searchResultFragment;
    private SearchResultProvider searchResultProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3_search_result);

        isFirstLaunched = (savedInstanceState == null);

        FragmentManager fragmentManager = getSupportFragmentManager();
        searchResultFragment = (SearchResultFragment) fragmentManager.findFragmentById(R.id.searchResultFragment);

        searchResultProvider = getSearchResultProvider(savedInstanceState);

        if (savedInstanceState == null) {
            startTask();
        }

        findViewById(R.id.showSearchResultOnMap).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onNeedToShowSearchResultOnMap();
            }
        });
    }

    public boolean isFirstLaunched() {
        return isFirstLaunched;
    }

    private SearchResultProvider getSearchResultProvider(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            return extractSearchResultProviderFrom(getIntent().getExtras());
        } else {
            return extractSearchResultProviderFrom(savedInstanceState);
        }
    }

    private SearchResultProvider extractSearchResultProviderFrom(Bundle bundle) {
        return (SearchResultProvider) bundle.getSerializable(Keys.searchResultProvider);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(Keys.searchResultProvider, (Serializable) searchResultProvider);
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
        /*
        if (resultCode != RESULT_OK) {
            finish();
            return;
        }
        */
        List<Partner> partners = (List<Partner>) result;
        removeTaskFragment();
        searchResultFragment.setSearchResult(partners);
    }

    private void removeTaskFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment taskFragment = fragmentManager.findFragmentById(R.id.taskFragmentContainer);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.remove(taskFragment);
        transaction.commit();
    }

    @Override
    public void onPartnerClick(Partner partner) {
        List<PartnerPoint> partnerPoints = searchResultProvider.getPartnerPoints(this, partner);
        PartnerActivity.start(this, partner, partnerPoints);
    }

    private void onNeedToShowSearchResultOnMap() {
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
        public List<PartnerPoint> getPartnerPoints(Context context) {
            return searchResultProvider.getAllPartnerPoints(context);
        }
    }

}
