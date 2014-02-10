package ru.droogcompanii.application.ui.activity.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.io.Serializable;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerCategory;
import ru.droogcompanii.application.ui.activity.search.search_result_provider_impl.SearchResultProviderByPartnerCategory;
import ru.droogcompanii.application.ui.activity.search.search_result_provider_impl.SearchResultProviderBySearchQuery;
import ru.droogcompanii.application.ui.activity.search_result.SearchResultActivity;
import ru.droogcompanii.application.ui.fragment.partner_category_list.PartnerCategoryListFragment;
import ru.droogcompanii.application.ui.helpers.ActionBarActivityWithUpButton;
import ru.droogcompanii.application.util.Keys;

/**
 * Created by ls on 14.01.14.
 */
public class SearchActivity extends ActionBarActivityWithUpButton
                implements PartnerCategoryListFragment.Callbacks {

    public static void start(Context context, String usageType) {
        if (isUsageTypeIllegal(usageType)) {
            throw new IllegalArgumentException("Illegal usage type: " + usageType);
        }
        Intent intent = new Intent(context, SearchActivity.class);
        intent.putExtra(Keys.usageType, usageType);
        context.startActivity(intent);
    }

    private static boolean isUsageTypeIllegal(String usageType) {
        return ((!usageType.equals(UsageType.SEARCH_BY_QUERY)) &&
                (!usageType.equals(UsageType.CATEGORIES)));
    }

    public static class UsageType {
        public static final String SEARCH_BY_QUERY = "SearchByQuery";
        public static final String CATEGORIES = "Categories";
    }

    private EditText searchQueryInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchQueryInput = (EditText) findViewById(R.id.searchQueryInputEditText);

        if (savedInstanceState == null && isUsageTypeSearchByQuery()) {
            searchQueryInput.requestFocus();
        }

        findViewById(R.id.searchByQueryButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSearch();
            }
        });
    }

    private boolean isUsageTypeSearchByQuery() {
        String usageType = getIntent().getStringExtra(Keys.usageType);
        return usageType.equals(UsageType.SEARCH_BY_QUERY);
    }

    private void onSearch() {
        String searchQuery = getSearchQuery();
        if (searchQuery.isEmpty()) {
            searchQueryInput.requestFocus();
        } else {
            onSearch(searchQuery);
        }
    }

    private String getSearchQuery() {
        return searchQueryInput.getText().toString().trim();
    }

    private void onSearch(String searchQuery) {
        showSearchResult(new SearchResultProviderBySearchQuery(searchQuery));
    }

    @Override
    public void onPartnerCategoryClick(PartnerCategory partnerCategory) {
        showSearchResult(new SearchResultProviderByPartnerCategory(partnerCategory));
    }

    private void showSearchResult(SearchResultProvider searchResultProvider) {
        Intent intent = new Intent(this, SearchResultActivity.class);
        intent.putExtra(Keys.searchResultProvider, (Serializable) searchResultProvider);
        startActivity(intent);
    }

}
