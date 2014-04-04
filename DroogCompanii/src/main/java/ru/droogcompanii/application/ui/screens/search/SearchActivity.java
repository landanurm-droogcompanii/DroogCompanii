package ru.droogcompanii.application.ui.screens.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.EditText;

import java.io.Serializable;
import java.util.List;

import ru.droogcompanii.application.DroogCompaniiApplication;
import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.db_util.hierarchy_of_partners.PartnersReader;
import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.ui.screens.main.category_list.CategoryListFragment;
import ru.droogcompanii.application.ui.screens.partner_list.PartnerListActivity;
import ru.droogcompanii.application.ui.screens.search_result_list.SearchResultListActivity;
import ru.droogcompanii.application.util.ui.activity.ActionBarActivityWithUpButton;
import ru.droogcompanii.application.util.ui.activity.ReuseAlreadyLaunchedActivityFlag;
import ru.droogcompanii.application.util.ui.activity.menu_helper.MenuHelper;
import ru.droogcompanii.application.util.ui.activity.menu_helper.MenuHelperItemsProvider;
import ru.droogcompanii.application.util.ui.activity.menu_helper.menu_item_helper.MenuItemHelper;
import ru.droogcompanii.application.util.ui.activity.menu_helper.menu_item_helper.MenuItemHelpers;

/**
 * Created by ls on 11.03.14.
 */
public class SearchActivity extends ActionBarActivityWithUpButton
        implements CategoryListFragment.Callbacks {

    private static class Tag {
        public static final String CATEGORY_LIST_FRAGMENT = "CATEGORY_LIST_FRAGMENT";
    }


    private EditText searchQueryInput;


    public static void start(Context context) {
        Intent intent = new Intent(context, SearchActivity.class);
        ReuseAlreadyLaunchedActivityFlag.set(intent);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        if (savedInstanceState == null) {
            placeFragmentOnLayout();
        }

        initSearchPanel();
    }

    private void placeFragmentOnLayout() {
        CategoryListFragment.Mode mode = CategoryListFragment.Mode.WITHOUT_ALL_PARTNERS_AND_WITHOUT_SELECTION;
        CategoryListFragment fragment = CategoryListFragment.newInstance(mode);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.containerOfFragment, fragment, Tag.CATEGORY_LIST_FRAGMENT);
        transaction.commit();
    }


    @Override
    public void onReceivingCategoriesTaskCompleted() {
        // skip
    }

    @Override
    public void onListInitialized() {
        // skip
    }

    @Override
    public void onCurrentCategoryChanged() {
        PartnerListActivity.start(this, prepareInputProvider());
    }

    private PartnerListActivity.InputProvider prepareInputProvider() {
        CategoryListFragment categoryListFragment = findCategoryListFragment();
        String conditionToReceivePartners = categoryListFragment.getConditionToReceivePartners().get();
        String selectedCategoryName = categoryListFragment.getSelectedCategoryName().get();
        return new InputProviderByCondition(conditionToReceivePartners, selectedCategoryName);
    }

    private CategoryListFragment findCategoryListFragment() {
        return (CategoryListFragment) getSupportFragmentManager().findFragmentByTag(Tag.CATEGORY_LIST_FRAGMENT);
    }

    private static class InputProviderByCondition
            implements PartnerListActivity.InputProvider, Serializable {

        private final String conditionToReceivePartners;
        private final String title;

        InputProviderByCondition(String conditionToReceivePartners, String title) {
            this.conditionToReceivePartners = conditionToReceivePartners;
            this.title = title;
        }

        @Override
        public List<Partner> getPartners(Context context) {
            PartnersReader reader = new PartnersReader(DroogCompaniiApplication.getContext());
            return reader.getPartnersByCondition(conditionToReceivePartners);
        }

        @Override
        public String getTitle(Context context) {
            return title;
        }
    }

    private void initSearchPanel() {
        searchQueryInput = (EditText) findViewById(R.id.searchQueryInputEditText);
        findViewById(R.id.searchByQueryButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSearch();
            }
        });
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
        SearchResultListActivity.startBySearchQuery(this, searchQuery);
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