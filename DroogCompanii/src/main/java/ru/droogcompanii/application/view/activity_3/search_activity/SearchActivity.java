package ru.droogcompanii.application.view.activity_3.search_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.Serializable;
import java.util.List;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.data_structure.Partner;
import ru.droogcompanii.application.data.data_structure.PartnerCategory;
import ru.droogcompanii.application.data.data_structure.PartnerPoint;
import ru.droogcompanii.application.data.db_util.readers_from_database.PartnerPointsReader;
import ru.droogcompanii.application.data.db_util.readers_from_database.PartnersReader;
import ru.droogcompanii.application.util.Keys;
import ru.droogcompanii.application.view.activity_3.search_result_activity.SearchResultActivity;
import ru.droogcompanii.application.view.activity_3.search_result_map_activity.SearchResultMapActivity;
import ru.droogcompanii.application.view.fragment.partner_category_list_fragment.PartnerCategoryListFragment;

/**
 * Created by ls on 14.01.14.
 */
public class SearchActivity extends android.support.v4.app.FragmentActivity
                implements PartnerCategoryListFragment.OnPartnerCategoryClickListener {

    private View buttonSearchByQuery;
    private EditText editTextSearchQueryInput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3_search);

        buttonSearchByQuery = findViewById(R.id.buttonSearchByQuery);
        editTextSearchQueryInput = (EditText) findViewById(R.id.editTextSearchQueryInput);

        buttonSearchByQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSearch();
            }
        });
    }

    private void onSearch() {
        String searchQuery = editTextSearchQueryInput.getText().toString().trim();
        if (searchQuery.isEmpty()) {
            editTextSearchQueryInput.requestFocus();
        } else {
            onSearch(searchQuery);
        }
    }

    private void onSearch(String searchQuery) {
        // TODO:
        Toast.makeText(this, "Search query: <" + searchQuery + ">", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPartnerCategoryClick(PartnerCategory partnerCategory) {
        Intent intent = new Intent(this, SearchResultActivity.class);
        intent.putExtra(Keys.searchResultProvider, new SearchResultProviderImpl(partnerCategory));
        startActivity(intent);
    }


    private static class SearchResultProviderImpl implements SearchResultProvider, Serializable {
        private final PartnerCategory partnerCategory;

        SearchResultProviderImpl(PartnerCategory partnerCategory) {
            this.partnerCategory = partnerCategory;
        }

        @Override
        public List<Partner> getPartners(Context context) {
            PartnersReader reader = new PartnersReader(context);
            return reader.getPartnersOf(partnerCategory);
        }

        @Override
        public List<PartnerPoint> getPartnerPoints(Context context, Partner partner) {
            PartnerPointsReader reader = new PartnerPointsReader(context);
            return reader.getPartnerPointsOf(partner);
        }
    }

}
