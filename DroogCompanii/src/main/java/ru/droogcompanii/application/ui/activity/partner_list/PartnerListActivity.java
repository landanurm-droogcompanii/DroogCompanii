package ru.droogcompanii.application.ui.activity.partner_list;

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
import ru.droogcompanii.application.ui.activity.menu_helper.MenuHelper;
import ru.droogcompanii.application.ui.activity.menu_helper.MenuHelperItemsProvider;
import ru.droogcompanii.application.ui.activity.menu_helper.menu_item_helper.MenuItemHelper;
import ru.droogcompanii.application.ui.activity.menu_helper.menu_item_helper.MenuItemHelpers;
import ru.droogcompanii.application.ui.activity.partner_details.PartnerDetailsActivity;
import ru.droogcompanii.application.ui.fragment.partner_list.PartnerListFragment;
import ru.droogcompanii.application.ui.fragment.partner_points_map.PartnerPointsProvider;
import ru.droogcompanii.application.ui.util.ActionBarActivityWithUpButtonAndGoToMapItem;
import ru.droogcompanii.application.ui.util.PartnerPointsProviderHolder;
import ru.droogcompanii.application.ui.util.able_to_start_task.TaskNotBeInterrupted;
import ru.droogcompanii.application.ui.util.able_to_start_task.TaskResultReceiver;

/**
 * Created by ls on 14.01.14.
 */
public class PartnerListActivity extends ActionBarActivityWithUpButtonAndGoToMapItem
                implements PartnerListFragment.Callbacks, PartnerPointsProviderHolder {

    public static interface InputProvider {
        List<Partner> getPartners(Context context);
        List<PartnerPoint> getAllPartnerPoints(Context context);
        String getTitle(Context context);
    }

    private static final int TASK_REQUEST_CODE_EXTRACT_SEARCH_RESULTS = 145;

    public static final int LOWER_BOUND_VALID_TASK_REQUEST_CODE =
                        TASK_REQUEST_CODE_EXTRACT_SEARCH_RESULTS + 1;

    private static final String KEY_INDEX_OF_CURRENT_COMPARATOR = "KEY_INDEX_OF_CURRENT_COMPARATOR";
    private static final String KEY_IS_GO_TO_MAP_ITEM_VISIBLE = "KEY_IS_GO_TO_MAP_ITEM_VISIBLE";

    private static final String KEY_INPUT_PROVIDER = "KEY_INPUT_PROVIDER";

    private boolean isGoToMapItemVisible;
    private int indexOfCurrentComparator;
    private InputProvider inputProvider;
    private PartnerListFragment searchResultFragment;


    public static void start(Context context, InputProvider inputProvider) {
        Intent intent = new Intent(context, PartnerListActivity.class);
        intent.putExtra(KEY_INPUT_PROVIDER, (Serializable) inputProvider);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner_list);

        if (savedInstanceState == null) {
            initStateByDefault();
        } else {
            restoreState(savedInstanceState);
        }

        searchResultFragment = (PartnerListFragment)
                getSupportFragmentManager().findFragmentById(R.id.searchResultFragment);

        initSpinnerOnActionBar();

        if (savedInstanceState == null) {
            actionsOnLaunchActivity();
        }
    }

    private void initStateByDefault() {
        indexOfCurrentComparator = 0;
        inputProvider = (InputProvider) getIntent().getSerializableExtra(KEY_INPUT_PROVIDER);
        updateGoToMapItemVisible(false);
    }

    private void restoreState(Bundle savedInstanceState) {
        indexOfCurrentComparator = savedInstanceState.getInt(KEY_INDEX_OF_CURRENT_COMPARATOR);
        inputProvider = (InputProvider) savedInstanceState.getSerializable(KEY_INPUT_PROVIDER);
        updateGoToMapItemVisible(savedInstanceState.getBoolean(KEY_IS_GO_TO_MAP_ITEM_VISIBLE));
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
                onComparatorChanged(itemPosition);
                return true;
            }
        };
        initSpinnerOnActionBar(spinnerAdapter, onNavigationListener);
    }

    private void onComparatorChanged(int comparatorPosition) {
        indexOfCurrentComparator = comparatorPosition;
        Comparator<Partner> newComparator = SpinnerAdapterPartnerImpl.getComparatorByPosition(comparatorPosition);
        searchResultFragment.setComparator(newComparator);
    }

    private void initSpinnerOnActionBar(SpinnerAdapter spinnerAdapter,
                                        ActionBar.OnNavigationListener listener) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        actionBar.setListNavigationCallbacks(spinnerAdapter, listener);
        actionBar.setSelectedNavigationItem(indexOfCurrentComparator);
    }

    private void actionsOnLaunchActivity() {
        searchResultFragment.hide();
        startTaskExtractSearchResults();
    }

    private void startTaskExtractSearchResults() {
        TaskNotBeInterrupted task = new TaskNotBeInterrupted() {
            @Override
            protected Serializable doInBackground(Void... voids) {
                return (Serializable) inputProvider.getPartners(PartnerListActivity.this);
            }
        };
        startTask(TASK_REQUEST_CODE_EXTRACT_SEARCH_RESULTS, task);
    }

    @Override
    public void onTaskResult(int requestCode, int resultCode, Serializable result) {
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
        if (resultCode != RESULT_OK) {
            finish();
            return;
        }
        List<Partner> searchResults = (List<Partner>) result;
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
        saveStateInto(outState);
    }

    private void saveStateInto(Bundle outState) {
        outState.putBoolean(KEY_IS_GO_TO_MAP_ITEM_VISIBLE, isGoToMapItemVisible);
        outState.putInt(KEY_INDEX_OF_CURRENT_COMPARATOR, indexOfCurrentComparator);
        outState.putSerializable(KEY_INPUT_PROVIDER, (Serializable) inputProvider);
    }

    @Override
    public void onPartnerClick(Partner partner) {
        PartnerDetailsActivity.start(this, partner);
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
        return new AllPartnerPointsProvider(inputProvider);
    }

    private static class AllPartnerPointsProvider implements PartnerPointsProvider, Serializable {
        private final InputProvider inputProvider;

        AllPartnerPointsProvider(InputProvider inputProvider) {
            this.inputProvider = inputProvider;
        }

        @Override
        public String getTitle(Context context) {
            return inputProvider.getTitle(context);
        }

        @Override
        public List<PartnerPoint> getPartnerPoints(Context context) {

            return inputProvider.getAllPartnerPoints(context);
        }
    }

}
