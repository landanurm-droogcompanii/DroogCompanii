package ru.droogcompanii.application.view.activity_3.search_result_activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.util.Keys;
import ru.droogcompanii.application.view.activity_3.partner_activity.PartnerActivity;
import ru.droogcompanii.application.view.activity_3.search_activity.SearchResultProvider;
import ru.droogcompanii.application.view.activity_3.search_result_map_activity.SearchResultMapActivity;
import ru.droogcompanii.application.view.fragment.partner_points_map_fragment.PartnerPointsProvider;

/**
 * Created by ls on 14.01.14.
 */
public class SearchResultActivity extends Activity implements AdapterView.OnItemClickListener {
    private List<Partner> partners;
    private PartnerListAdapter adapter;
    private SearchResultProvider searchResultProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3_search_result);

        if (savedInstanceState == null) {
            init();
        } else {
            restoreState(savedInstanceState);
        }

        adapter = new PartnerListAdapter(this, partners);

        ListView listView = (ListView) findViewById(R.id.partnerPointListView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

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
        partners = searchResultProvider.getPartners(this);
    }

    private void restoreState(Bundle savedInstanceState) {
        searchResultProvider = (SearchResultProvider) savedInstanceState.getSerializable(Keys.searchResultProvider);
        partners = (List<Partner>) savedInstanceState.getSerializable(Keys.partners);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(Keys.searchResultProvider, (Serializable) searchResultProvider);
        outState.putSerializable(Keys.partners, (Serializable) partners);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Partner partner = adapter.getItem(position);
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
            List<PartnerPoint> partnerPoints = new ArrayList<PartnerPoint>();
            List<Partner> partners = searchResultProvider.getPartners(context);
            for (Partner partner : partners) {
                partnerPoints.addAll(searchResultProvider.getPartnerPoints(context, partner));
            }
            return partnerPoints;
        }
    }

}
