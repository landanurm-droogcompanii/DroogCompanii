package ru.droogcompanii.application.ui.activity.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerCategory;
import ru.droogcompanii.application.ui.activity.base_menu_helper.MenuHelper;
import ru.droogcompanii.application.ui.activity.base_menu_helper.MenuHelperItemsProvider;
import ru.droogcompanii.application.ui.activity.base_menu_helper.menu_item_helper.MenuItemHelper;
import ru.droogcompanii.application.ui.activity.base_menu_helper.menu_item_helper.MenuItemHelpers;
import ru.droogcompanii.application.ui.activity.search.search_result_provider_impl.SearchResultProviderByPartnerCategory;
import ru.droogcompanii.application.ui.activity.search.search_result_provider_impl.SearchResultProviderBySearchQuery;
import ru.droogcompanii.application.ui.activity.search.search_result_provider_impl.SearchResultProviderFavorite;
import ru.droogcompanii.application.ui.activity.search_result_list.SearchResultListActivity;
import ru.droogcompanii.application.ui.fragment.partner_category_list.PartnerCategoryListFragment;
import ru.droogcompanii.application.ui.util.ActionBarActivityWithUpButton;

/**
 * Created by ls on 14.01.14.
 */
public class SearchActivity extends ActionBarActivityWithUpButton
                implements PartnerCategoryListFragment.Callbacks {

    public static void start(Context context) {
        Intent intent = new Intent(context, SearchActivity.class);
        context.startActivity(intent);
    }

    private EditText searchQueryInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchQueryInput = (EditText) findViewById(R.id.searchQueryInputEditText);

        initFavoriteTextView();

        findViewById(R.id.searchByQueryButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSearch();
            }
        });

        findViewById(R.id.showFavorite).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onShowFavorite();
            }
        });
    }

    private void initFavoriteTextView() {
        ViewGroup favoriteContainer = (ViewGroup) findViewById(R.id.favoriteContainer);
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        TextView favoriteTextView = (TextView) layoutInflater.inflate(android.R.layout.simple_list_item_1, null);
        favoriteTextView.setText(R.string.favorite);
        favoriteContainer.removeAllViews();
        favoriteContainer.addView(favoriteTextView);
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
        SearchResultListActivity.start(this, searchResultProvider);
    }

    private void onShowFavorite() {
        showSearchResult(new SearchResultProviderFavorite());
    }

    @Override
    protected MenuHelper getMenuHelper() {
        return new MenuHelperItemsProvider(this) {
            @Override
            protected MenuItemHelper[] getMenuItemHelpers() {
                return new MenuItemHelper[] {
                        MenuItemHelpers.PERSONAL_ACCOUNT,
                        MenuItemHelpers.SETTINGS,
                        MenuItemHelpers.HELP
                };
            }
        };
    }
}
