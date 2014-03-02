package ru.droogcompanii.application.ui.activity.search_result_list;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.widget.SpinnerAdapter;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.data.searchable_sortable_listing.SearchResult;
import ru.droogcompanii.application.ui.activity.able_to_start_task.TaskResultReceiver;
import ru.droogcompanii.application.ui.activity.base_menu_helper.MenuHelper;
import ru.droogcompanii.application.ui.activity.base_menu_helper.MenuHelperItemsProvider;
import ru.droogcompanii.application.ui.activity.base_menu_helper.menu_item_helper.MenuItemHelper;
import ru.droogcompanii.application.ui.activity.base_menu_helper.menu_item_helper.MenuItemHelpers;
import ru.droogcompanii.application.ui.activity.partner_details.PartnerDetailsActivity;
import ru.droogcompanii.application.ui.activity.search.SearchResultProvider;
import ru.droogcompanii.application.ui.activity.search_result_list.spinner_util.SpinnerAdapterPartnerImpl;
import ru.droogcompanii.application.ui.fragment.partner_points_map.PartnerPointsProvider;
import ru.droogcompanii.application.ui.fragment.search_result_list.SearchResultListFragment;
import ru.droogcompanii.application.ui.helpers.ActionBarActivityWithUpButtonAndGoToMapItem;
import ru.droogcompanii.application.ui.helpers.task.TaskFragmentHolder;
import ru.droogcompanii.application.ui.helpers.task.TaskNotBeInterrupted;
import ru.droogcompanii.application.ui.util.PartnerPointsProviderHolder;
import ru.droogcompanii.application.util.Keys;

/**
 * Created by ls on 14.01.14.
 */
public class SearchResultListActivity extends ActionBarActivityWithUpButtonAndGoToMapItem
                implements SearchResultListFragment.Callbacks,
                           TaskFragmentHolder.Callbacks,
                           PartnerPointsProviderHolder {

    private static final int TASK_REQUEST_CODE_EXTRACT_SEARCH_RESULTS = 145;

    public static final int LOWER_BOUND_VALID_TASK_REQUEST_CODE =
                        TASK_REQUEST_CODE_EXTRACT_SEARCH_RESULTS + 1;

    private boolean isGoToMapItemVisible;
    private SearchResultListFragment searchResultFragment;
    private SearchResultProvider searchResultProvider;


    public static void start(Context context, SearchResultProvider searchResultProvider) {
        Intent intent = new Intent(context, SearchResultListActivity.class);
        intent.putExtra(Keys.searchResultProvider, (Serializable) searchResultProvider);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        init(savedInstanceState);

        searchResultFragment = (SearchResultListFragment)
                getSupportFragmentManager().findFragmentById(R.id.searchResultFragment);

        initSpinnerOnActionBar();

        if (savedInstanceState == null) {
            actionsOnLaunchActivity();
        }
    }

    private void init(Bundle savedInstanceState) {
        searchResultProvider = getSearchResultProvider(savedInstanceState);
        updateGoToMapItemVisible(getIsGoToMapItemVisible(savedInstanceState));
    }

    private SearchResultProvider getSearchResultProvider(Bundle savedInstanceState) {
        Bundle bundleWithResult = (savedInstanceState != null) ? savedInstanceState : getIntent().getExtras();
        return (SearchResultProvider) bundleWithResult.getSerializable(Keys.searchResultProvider);
    }

    private boolean getIsGoToMapItemVisible(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            return false;
        }
        return savedInstanceState.getBoolean(Keys.isGoToMapItemVisible);
    }

    private void updateGoToMapItemVisible(boolean visible) {
        isGoToMapItemVisible = visible;
        updateGoToMapItemVisible();
    }

    @Override
    protected boolean isGoToMapItemVisible() {
        return isGoToMapItemVisible;
    }


    private void initSpinnerOnActionBar() {
        SpinnerAdapter spinnerAdapter = new SpinnerAdapterPartnerImpl(this);
        ActionBar.OnNavigationListener onNavigationListener = new ActionBar.OnNavigationListener() {
            @Override
            public boolean onNavigationItemSelected(int itemPosition, long itemId) {
                Comparator<Partner> newComparator = SpinnerAdapterPartnerImpl.getComparatorByPosition(itemPosition);
                onComparatorChanged(newComparator);
                return true;
            }
        };
        initSpinnerOnActionBar(spinnerAdapter, onNavigationListener);
    }

    private void initSpinnerOnActionBar(SpinnerAdapter spinnerAdapter,
                                        ActionBar.OnNavigationListener listener) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        actionBar.setListNavigationCallbacks(spinnerAdapter, listener);
    }

    private void onComparatorChanged(Comparator<Partner> newComparator) {
        searchResultFragment.setComparator(newComparator);
    }

    private void actionsOnLaunchActivity() {
        searchResultFragment.hide();
        startTaskExtractSearchResults();
    }

    private void startTaskExtractSearchResults() {
        TaskNotBeInterrupted task = new SearchResultsExtractorTask(searchResultProvider, this);
        startTask(TASK_REQUEST_CODE_EXTRACT_SEARCH_RESULTS, task);
    }

    @Override
    protected void onReceiveResult(int requestCode, int resultCode, Serializable result) {
        switch (requestCode) {
            case TASK_REQUEST_CODE_EXTRACT_SEARCH_RESULTS:
                onReceiveResultFromExtractSearchResultsTask(resultCode, result);
                break;

            default:
                onReceiveResultFromTaskLaunchedByFragment(requestCode, resultCode, result);
                break;
        }
    }

    private void onReceiveResultFromExtractSearchResultsTask(int resultCode, Serializable result) {
        if (resultCode == RESULT_OK) {
            showSearchResult(result);
        } else {
            finish();
        }
    }

    private void showSearchResult(Serializable result) {
        List<SearchResult<Partner>> searchResults = (List<SearchResult<Partner>>) result;
        searchResultFragment.show();
        searchResultFragment.setSearchResult(searchResults);
    }

    private void onReceiveResultFromTaskLaunchedByFragment(int requestCode,
                                          int resultCode, Serializable result) {
        TaskResultReceiver resultReceiver = (TaskResultReceiver) getCurrentFragment();
        resultReceiver.onTaskResult(requestCode, resultCode, result);
    }

    private Fragment getCurrentFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.searchResultFragment);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(Keys.searchResultProvider, (Serializable) searchResultProvider);
        outState.putBoolean(Keys.isGoToMapItemVisible, isGoToMapItemVisible);
    }

    @Override
    public void onPartnerClick(Partner partner) {
        PartnerDetailsActivity.start(this, new PartnerAndPartnerPointsProviderImpl(partner, searchResultProvider));
    }

    private static class PartnerAndPartnerPointsProviderImpl
            implements PartnerDetailsActivity.PartnerAndPartnerPointsProvider, Serializable{
        private final Partner partner;
        private final SearchResultProvider searchResultProvider;

        PartnerAndPartnerPointsProviderImpl(Partner partner, SearchResultProvider searchResultProvider) {
            this.partner = partner;
            this.searchResultProvider = searchResultProvider;
        }

        @Override
        public Partner getPartner(Context context) {
            return partner;
        }

        @Override
        public List<PartnerPoint> getPartnerPoints(Context context) {
            return searchResultProvider.getPointsOfPartner(context, partner);
        }
    }

    @Override
    public void onNotFound() {
        updateGoToMapItemVisible(false);
    }

    @Override
    public void onFound() {
        updateGoToMapItemVisible(true);
    }

    @Override
    protected MenuHelper getMenuHelper() {
        return new MenuHelperItemsProvider(this) {
            @Override
            protected MenuItemHelper[] getMenuItemHelpers() {
                return new MenuItemHelper[] {
                        MenuItemHelpers.GO_TO_MAP,
                        MenuItemHelpers.PERSONAL_ACCOUNT,
                        MenuItemHelpers.SETTINGS,
                        MenuItemHelpers.HELP
                };
            }
        };
    }

    @Override
    public PartnerPointsProvider getPartnerPointsProvider() {
        return new AllPartnerPointsFromSearchResultProvider(searchResultProvider);
    }

    private static class AllPartnerPointsFromSearchResultProvider implements PartnerPointsProvider, Serializable {
        private final SearchResultProvider searchResultProvider;

        AllPartnerPointsFromSearchResultProvider(SearchResultProvider searchResultProvider) {
            this.searchResultProvider = searchResultProvider;
        }

        @Override
        public String getTitle(Context context) {
            return searchResultProvider.getTitle(context);
        }

        @Override
        public List<PartnerPoint> getPartnerPoints(Context context) {
            return searchResultProvider.getAllPartnerPoints(context);
        }
    }

}
