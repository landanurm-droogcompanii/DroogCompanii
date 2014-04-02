package ru.droogcompanii.application.ui.screens.search_result_list;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.util.StateManager;
import ru.droogcompanii.application.util.ui.activity.ReuseAlreadyLaunchedActivityFlag;
import ru.droogcompanii.application.util.ui.activity.menu_helper.MenuHelper;
import ru.droogcompanii.application.util.ui.activity.menu_helper.MenuHelperItemsProvider;
import ru.droogcompanii.application.util.ui.activity.menu_helper.menu_item_helper.MenuItemHelper;
import ru.droogcompanii.application.util.ui.activity.menu_helper.menu_item_helper.MenuItemHelpers;
import ru.droogcompanii.application.util.ui.activity.ActionBarActivityWithUpButton;
import ru.droogcompanii.application.util.ui.fragment.FragmentUtils;

/**
 * Created by ls on 11.03.14.
 */
public class SearchResultListActivity extends ActionBarActivityWithUpButton {

    private static final String TAG_SEARCH_RESULT_LIST_FRAGMENT = "TAG_SEARCH_RESULT_LIST_FRAGMENT";

    private static final String KEY_QUERY = "KEY_QUERY";

    private String query;

    private final StateManager STATE_MANAGER = new StateManager() {
        @Override
        public void initStateByDefault() {
            query = getIntent().getStringExtra(KEY_QUERY);
            placeFragmentOnLayout();
        }

        @Override
        public void restoreState(Bundle savedInstanceState) {
            query = savedInstanceState.getString(KEY_QUERY);
        }

        @Override
        public void saveState(Bundle outState) {
            outState.putString(KEY_QUERY, query);
        }
    };

    public static void start(Context context, String query) {
        Intent intent = new Intent(context, SearchResultListActivity.class);
        intent.putExtra(KEY_QUERY, query);
        ReuseAlreadyLaunchedActivityFlag.set(intent);
        context.startActivity(intent);
    }

    private void placeFragmentOnLayout() {
        Fragment fragment = SearchResultListFragment.newInstance(query);
        FragmentUtils.placeFragmentOnLayout(this,
                R.id.containerOfSearchResultList, fragment, TAG_SEARCH_RESULT_LIST_FRAGMENT);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result_list);
        STATE_MANAGER.initState(savedInstanceState);
        setTitle();
    }

    private void setTitle() {
        final String openQuote = "\u201C";
        final String closeQuote = "\u201D";
        String title = openQuote + query + closeQuote;
        setTitle(title);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        STATE_MANAGER.saveState(outState);
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