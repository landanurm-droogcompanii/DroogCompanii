package ru.droogcompanii.application.ui.screens.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.google.common.base.Optional;

import java.util.List;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.ui.screens.main.category_list.CategoryListFragment;
import ru.droogcompanii.application.ui.screens.main.filters_dialog.FiltersDialogFragment;
import ru.droogcompanii.application.ui.screens.main.map.CustomMapFragmentWithBaseLocation;
import ru.droogcompanii.application.ui.screens.main.map.PartnerPointsMapFragment;
import ru.droogcompanii.application.ui.screens.main.map.notifier_location_service.NotifierLocationServiceDialogFragment;
import ru.droogcompanii.application.ui.screens.main.partner_point_details.PartnerPointDetailsFragment;
import ru.droogcompanii.application.ui.screens.synchronization.SynchronizationActivity;
import ru.droogcompanii.application.util.location.CustomBaseLocationUtils;
import ru.droogcompanii.application.util.ui.activity.ActivityWithNavigationDrawer;
import ru.droogcompanii.application.util.ui.activity.menu_helper.MenuHelper;
import ru.droogcompanii.application.util.ui.activity.menu_helper.MenuHelperItemsProvider;
import ru.droogcompanii.application.util.ui.activity.menu_helper.menu_item_helper.MenuItemHelper;
import ru.droogcompanii.application.util.ui.activity.menu_helper.menu_item_helper.MenuItemHelpers;

/**
 * Created by ls on 14.03.14.
 */
public class MainScreen extends ActivityWithNavigationDrawer
                        implements CategoryListFragment.Callbacks,
                                   PartnerPointsMapFragment.Callbacks,
                                   CustomMapFragmentWithBaseLocation.Callbacks,
                                   FiltersDialogFragment.Callbacks {

    private static class Tag {
        public static final String CATEGORY_LIST = "CATEGORY_LIST";
        public static final String MAP = "MAP";
        public static final String PARTNER_POINT_DETAILS = "PARTNER_POINT_DETAILS";
    }

    private View buttonDismissCustomBaseLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        initNavigationDrawer(R.id.drawerLayout);
        if (savedInstanceState == null) {
            placeFragmentsOnLayout();
        } else {
            restoreNavigationDrawerState(savedInstanceState);
        }
        initDismissCustomBaseLocationAction();
    }

    private void placeFragmentsOnLayout() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.partnerPointsMapFragment,
                PartnerPointsMapFragment.newInstance(true),
                Tag.MAP);
        transaction.add(R.id.leftDrawer,
                        new CategoryListFragment(),
                        Tag.CATEGORY_LIST);
        transaction.add(R.id.partnerPointDetailsFragment,
                        PartnerPointDetailsFragment.newInstance(),
                        Tag.PARTNER_POINT_DETAILS);
        transaction.commit();
    }

    private void initDismissCustomBaseLocationAction() {
        buttonDismissCustomBaseLocation = findViewById(R.id.dismissCustomBaseLocation);
        buttonDismissCustomBaseLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDismissCustomBaseLocation();
            }
        });
        int visibility = CustomBaseLocationUtils.isBasePositionSet() ? View.VISIBLE : View.INVISIBLE;
        buttonDismissCustomBaseLocation.setVisibility(visibility);
    }

    @Override
    public void onReceivingCategoriesTaskCompleted() {
        updateMapFragment();
    }

    @Override
    public void onListInitialized() {
        // do nothing
    }

    @Override
    public void onCurrentCategoryChanged() {
        updateMapFragment();
    }

    private void updateMapFragment() {
        CategoryListFragment categoryListFragment = findCategoryListFragment();
        Optional<String> conditionToReceivePartners = categoryListFragment.getConditionToReceivePartners();
        Optional<String> conditionToReceivePartnerPoints =
                ConditionConverter.ConditionToReceivePartnerPoints.from(conditionToReceivePartners);
        findMapFragment().updateCondition(conditionToReceivePartnerPoints);
    }

    private CategoryListFragment findCategoryListFragment() {
        return (CategoryListFragment) findFragmentByTag(Tag.CATEGORY_LIST);
    }

    private PartnerPointsMapFragment findMapFragment() {
        return (PartnerPointsMapFragment) findFragmentByTag(Tag.MAP);
    }

    @Override
    public void onMarkerClicked(List<PartnerPoint> partnerPoints) {
        findPartnerPointDetailsFragment().setPartnerPoints(partnerPoints);
    }

    private PartnerPointDetailsFragment findPartnerPointDetailsFragment() {
        return (PartnerPointDetailsFragment) findFragmentByTag(Tag.PARTNER_POINT_DETAILS);
    }

    private Fragment findFragmentByTag(String tag) {
        return getSupportFragmentManager().findFragmentByTag(tag);
    }

    @Override
    public void onNoClickedMarker() {
        findPartnerPointDetailsFragment().hide();
    }

    @Override
    public void onCustomBaseLocationIsSet() {
        buttonDismissCustomBaseLocation.setVisibility(View.VISIBLE);
    }

    private void onDismissCustomBaseLocation() {
        findMapFragment().dismissCustomBaseLocation();
    }

    @Override
    public void onCustomBaseLocationIsDismissed() {
        buttonDismissCustomBaseLocation.setVisibility(View.INVISIBLE);
    }

    @Override
    protected MenuHelper getMenuHelper() {
        return new MenuHelperItemsProvider(this) {
            @Override
            protected MenuItemHelper[] getMenuItemHelpers() {
                return new MenuItemHelper[] {
                        MenuItemHelpers.SEARCH,
                        MenuItemHelpers.FILTER.withAction(new MenuItemHelper.Action() {
                            @Override
                            public void run(Activity activity) {
                                onActionFilter();
                            }
                        }),
                        MenuItemHelpers.OFFERS,
                        MenuItemHelpers.PERSONAL_ACCOUNT,
                        MenuItemHelpers.SYNCHRONIZATION,
                        MenuItemHelpers.SETTINGS,
                        MenuItemHelpers.HELP
                };
            }
        };
    }

    private void onActionFilter() {
        FiltersDialogFragment.show(this);
    }

    @Override
    public void onFiltersDone() {
        updateMapFragment();
    }

    @Override
    public void onFiltersCancel() {
        // do nothing
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SynchronizationActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            onSynchronizationCompleted();
        }
    }

    private void onSynchronizationCompleted() {
        refresh();
    }

    @Override
    public void onDisplayingIsStarting() {
        setNumberOfDisplayedPartnerPointsIsUnknown();
    }

    private void setNumberOfDisplayedPartnerPointsIsUnknown() {
        findCategoryListFragment().setCategorySizeIsUnknown();

        Optional<String> categoryName = findCategoryListFragment().getSelectedCategoryName();
        setTitle(categoryName.or(""));
    }

    @Override
    public void onDisplayingIsCompleted(int numberOfDisplayedPartnerPoints) {
        setNumberOfDisplayedPartnerPoints(numberOfDisplayedPartnerPoints);
    }

    private void setNumberOfDisplayedPartnerPoints(int numberOfDisplayedPartnerPoints) {
        // findCategoryListFragment().setCategorySize(numberOfDisplayedPartnerPoints);

        Optional<String> categoryName = findCategoryListFragment().getSelectedCategoryName();
        if (!categoryName.isPresent()) {
            return;
        }
        String title = categoryName.get() + " (" + numberOfDisplayedPartnerPoints + ")";
        setTitle(title);
    }

    @Override
    public void onMapInitialized() {
        // skip
    }

    @Override
    public void onNeedToEnableLocationService() {
        NotifierLocationServiceDialogFragment.showIfNeed(this);
    }

}
