package ru.droogcompanii.application.ui.activity_3.search_result_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;

import java.io.Serializable;
import java.util.List;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.util.Keys;
import ru.droogcompanii.application.ui.activity_3.partner_activity.PartnerActivity;
import ru.droogcompanii.application.ui.activity_3.search_activity.SearchResultProvider;
import ru.droogcompanii.application.ui.activity_3.search_result_map_activity.SearchResultMapActivity;
import ru.droogcompanii.application.ui.fragment.partner_points_map_fragment.PartnerPointsProvider;
import ru.droogcompanii.application.ui.fragment.search_result_fragment.SearchResultFragment;

/**
 * Created by ls on 14.01.14.
 */
public class SearchResultActivity extends FragmentActivity
                                 implements SearchResultFragment.OnPartnerClickListener {
    private SearchResultFragment searchResultFragment;
    private SearchResultProvider searchResultProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3_search_result);

        FragmentManager fragmentManager = getSupportFragmentManager();
        searchResultFragment = (SearchResultFragment) fragmentManager.findFragmentById(R.id.searchResultFragment);

        if (savedInstanceState == null) {
            init();
        } else {
            restoreState(savedInstanceState);
        }

        findViewById(R.id.showSearchResultOnMap).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onNeedToShowSearchResultOnMap();
            }
        });
    }

    private void init() {
        Bundle args = getIntent().getExtras();
        searchResultProvider = (SearchResultProvider) args.getSerializable(Keys.searchResultProvider);
        searchResultFragment.setSearchResultProvider(searchResultProvider);
    }

    private void restoreState(Bundle savedInstanceState) {
        searchResultProvider = (SearchResultProvider) savedInstanceState.getSerializable(Keys.searchResultProvider);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(Keys.searchResultProvider, (Serializable) searchResultProvider);
    }

    @Override
    public void onPartnerClick(Partner partner) {
        List<PartnerPoint> partnerPoints = searchResultProvider.getPartnerPoints(this, partner);
        PartnerActivity.start(this, partner, partnerPoints);
    }

    private void onNeedToShowSearchResultOnMap() {
        Serializable partnerPointsProvider = new AllPartnerPointsFromSearchResultProvider(searchResultProvider);
        showResultOnMap(partnerPointsProvider);
    }

    private void showResultOnMap(Serializable partnerPointsProvider) {
        Intent intent = new Intent(this, SearchResultMapActivity.class);
        intent.putExtra(Keys.partnerPointsProvider, partnerPointsProvider);
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
