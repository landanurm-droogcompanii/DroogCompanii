package ru.droogcompanii.application.view.activity_3.search_result_activity;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.io.Serializable;
import java.util.List;

import ru.droogcompanii.application.data.data_structure.Partner;
import ru.droogcompanii.application.data.data_structure.PartnerPoint;
import ru.droogcompanii.application.util.Keys;
import ru.droogcompanii.application.view.activity_3.search_activity.SearchResultProvider;
import ru.droogcompanii.application.view.activity_3.search_result_map_activity.SearchResultMapActivity;
import ru.droogcompanii.application.view.fragment.partner_points_map_fragment.PartnerPointsProvider;

/**
 * Created by ls on 14.01.14.
 */
public class SearchResultActivity extends ListActivity {
    private List<Partner> partners;
    private PartnerListAdapter adapter;
    private SearchResultProvider searchResultProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            init();
        } else {
            restoreState(savedInstanceState);
        }

        adapter = new PartnerListAdapter(this, partners);
        setListAdapter(adapter);
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
    protected void onListItemClick(ListView listView, View view, int position, long id) {
        Partner partner = adapter.getItem(position);
        Intent intent = new Intent(this, SearchResultMapActivity.class);
        Serializable partnerPointsProvider = new PartnerPointsFromPartnerProvider(partner, searchResultProvider);
        intent.putExtra(Keys.partnerPointsProvider, partnerPointsProvider);
        startActivity(intent);
    }

    private static class PartnerPointsFromPartnerProvider implements PartnerPointsProvider, Serializable {
        private final Partner partner;
        private final SearchResultProvider searchResultProvider;

        PartnerPointsFromPartnerProvider(Partner partner, SearchResultProvider searchResultProvider) {
            this.partner = partner;
            this.searchResultProvider = searchResultProvider;
        }

        @Override
        public List<PartnerPoint> getPartnerPoints(Context context) {
            return searchResultProvider.getPartnerPoints(context, partner);
        }
    }

}
