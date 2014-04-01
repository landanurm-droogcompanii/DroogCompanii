package ru.droogcompanii.application.ui.screens.partner_list;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.widget.SpinnerAdapter;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.util.StateManager;
import ru.droogcompanii.application.util.ui.activity.ActionBarActivityWithUpButton;
import ru.droogcompanii.application.util.ui.activity.menu_helper.MenuHelper;
import ru.droogcompanii.application.util.ui.activity.menu_helper.MenuHelperItemsProvider;
import ru.droogcompanii.application.util.ui.activity.menu_helper.menu_item_helper.MenuItemHelper;
import ru.droogcompanii.application.util.ui.activity.menu_helper.menu_item_helper.MenuItemHelpers;

/**
 * Created by ls on 14.01.14.
 */
public class PartnerListActivity extends ActionBarActivityWithUpButton {

    public static interface InputProvider {
        List<Partner> getPartners(Context context);
        String getTitle(Context context);
    }

    private static class Key {
        public static final String INDEX_OF_CURRENT_COMPARATOR = "INDEX_OF_CURRENT_COMPARATOR";
        public static final String INPUT_PROVIDER = "INPUT_PROVIDER";
    }

    private static class Tag {
        public static final String PARTNER_LIST_FRAGMENT =
                "PARTNER_LIST_FRAGMENT" + PartnerListActivity.class.getName();
    }

    private int indexOfCurrentComparator;


    public static void start(Context context, InputProvider inputProvider) {
        Intent intent = new Intent(context, PartnerListActivity.class);
        intent.putExtra(Key.INPUT_PROVIDER, (Serializable) inputProvider);
        context.startActivity(intent);
    }

    private final StateManager STATE_MANAGER = new StateManager() {
        @Override
        public void initStateByDefault() {
            indexOfCurrentComparator = 0;
            placeFragmentOnLayout();
        }

        @Override
        public void restoreState(Bundle savedInstanceState) {
            indexOfCurrentComparator = savedInstanceState.getInt(Key.INDEX_OF_CURRENT_COMPARATOR);
        }

        @Override
        public void saveState(Bundle outState) {
            outState.putInt(Key.INDEX_OF_CURRENT_COMPARATOR, indexOfCurrentComparator);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner_list);
        STATE_MANAGER.initState(savedInstanceState);
        initSpinnerOnActionBar();
    }

    private void placeFragmentOnLayout() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        InputProvider inputProvider = (InputProvider) getIntent().getSerializableExtra(Key.INPUT_PROVIDER);
        PartnerListFragment fragment = PartnerListFragment.newInstance(inputProvider);
        transaction.add(R.id.containerOfFragment, fragment, Tag.PARTNER_LIST_FRAGMENT);
        transaction.commit();
    }

    private void initSpinnerOnActionBar() {
        SpinnerAdapter spinnerAdapter = new SpinnerAdapterImpl(this);
        ActionBar.OnNavigationListener onNavigationListener = new ActionBar.OnNavigationListener() {
            @Override
            public boolean onNavigationItemSelected(int itemPosition, long itemId) {
                onComparatorChanged(itemPosition);
                return true;
            }
        };
        initSpinnerOnActionBar(spinnerAdapter, onNavigationListener);
    }

    private void onComparatorChanged(int comparatorPosition) {
        indexOfCurrentComparator = comparatorPosition;
        Comparator<Partner> newComparator = SpinnerAdapterImpl.getComparatorByPosition(comparatorPosition);
        findFragment().setComparator(newComparator);
    }

    private PartnerListFragment findFragment() {
        return (PartnerListFragment) getSupportFragmentManager().findFragmentByTag(Tag.PARTNER_LIST_FRAGMENT);
    }

    private void initSpinnerOnActionBar(SpinnerAdapter spinnerAdapter,
                                        ActionBar.OnNavigationListener listener) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        actionBar.setListNavigationCallbacks(spinnerAdapter, listener);
        actionBar.setSelectedNavigationItem(indexOfCurrentComparator);
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
