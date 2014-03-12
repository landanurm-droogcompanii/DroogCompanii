package ru.droogcompanii.application.ui.activity.search_2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerCategory;
import ru.droogcompanii.application.ui.activity.menu_helper.MenuHelper;
import ru.droogcompanii.application.ui.activity.menu_helper.MenuHelperItemsProvider;
import ru.droogcompanii.application.ui.activity.menu_helper.menu_item_helper.MenuItemHelper;
import ru.droogcompanii.application.ui.activity.menu_helper.menu_item_helper.MenuItemHelpers;
import ru.droogcompanii.application.ui.activity.partner_list.PartnerListActivity;
import ru.droogcompanii.application.ui.activity.search_result_list.SearchResultListActivity;
import ru.droogcompanii.application.ui.fragment.partner_category_list.PartnerCategoryListFragment;
import ru.droogcompanii.application.ui.util.ActionBarActivityWithUpButton;

/**
 * Created by ls on 11.03.14.
 */
public class SearchActivity2 extends ActionBarActivityWithUpButton
        implements PartnerCategoryListFragment.Callbacks {


    private EditText searchQueryInput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchQueryInput = (EditText) findViewById(R.id.searchQueryInputEditText);

        initFavoriteItem();

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

    private void initFavoriteItem() {
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
        SearchResultListActivity.start(this, searchQuery);
    }

    @Override
    public void onPartnerCategoryClick(PartnerCategory partnerCategory) {
        PartnerListActivity.start(this, new InputProviderByPartnerCategory(partnerCategory));
    }

    private void onShowFavorite() {
        PartnerListActivity.start(this, new FavoritePartnersProvider());
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