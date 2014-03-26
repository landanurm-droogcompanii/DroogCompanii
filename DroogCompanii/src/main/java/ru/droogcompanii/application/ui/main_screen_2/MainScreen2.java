package ru.droogcompanii.application.ui.main_screen_2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.google.common.base.Optional;

import java.util.List;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.ui.activity.menu_helper.MenuHelper;
import ru.droogcompanii.application.ui.activity.menu_helper.MenuHelperItemsProvider;
import ru.droogcompanii.application.ui.activity.menu_helper.menu_item_helper.MenuItemHelper;
import ru.droogcompanii.application.ui.activity.menu_helper.menu_item_helper.MenuItemHelpers;
import ru.droogcompanii.application.ui.activity.synchronization.SynchronizationActivity;
import ru.droogcompanii.application.ui.main_screen_2.category_list.CategoryListFragment;
import ru.droogcompanii.application.ui.main_screen_2.details.PartnerPointDetailsFragment;
import ru.droogcompanii.application.ui.main_screen_2.filters_dialog.FiltersDialogFragment;
import ru.droogcompanii.application.ui.main_screen_2.map.CustomMapFragmentWithBaseLocation;
import ru.droogcompanii.application.ui.main_screen_2.map.NewPartnerPointsMapFragment;
import ru.droogcompanii.application.util.activity.ActivityWithNavigationDrawer;
import ru.droogcompanii.application.util.location.CustomBaseLocationUtils;

/**
 * Created by ls on 14.03.14.
 */
public class MainScreen2 extends ActivityWithNavigationDrawer
        implements CategoryListFragment.Callbacks,
                   NewPartnerPointsMapFragment.Callbacks,
                   CustomMapFragmentWithBaseLocation.Callbacks,
                   FiltersDialogFragment.Callbacks {

    private static class Tag {
        public static final String CATEGORY_LIST = "TAG_CATEGORY_LIST";
        public static final String MAP = "TAG_MAP";
        public static final String PARTNER_POINT_DETAILS = "TAG_PARTNER_POINT_DETAILS";
    }

    private View dismissCustomBaseLocation;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main_screen_2;
    }

    @Override
    protected int getDrawerLayoutId() {
        return R.id.drawerLayout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            placeFragmentsOnLayout();
        }
        initDismissCustomBaseLocationAction();
    }

    private void initDismissCustomBaseLocationAction() {
        dismissCustomBaseLocation = findViewById(R.id.dismissCustomBaseLocation);
        dismissCustomBaseLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDismissCustomBaseLocation();
            }
        });
        int visibility = CustomBaseLocationUtils.isBasePositionSet() ? View.VISIBLE : View.INVISIBLE;
        dismissCustomBaseLocation.setVisibility(visibility);
    }

    private void placeFragmentsOnLayout() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.containerOfMapFragment, new NewPartnerPointsMapFragment(), Tag.MAP);
        transaction.add(R.id.leftDrawer, new CategoryListFragment(), Tag.CATEGORY_LIST);
        transaction.add(R.id.containerOfPartnerPointDetailsFragment,
                new PartnerPointDetailsFragment(), Tag.PARTNER_POINT_DETAILS);
        transaction.commit();
    }

    @Override
    public void onReceivingCategoriesTaskCompleted() {
        // skip
    }

    @Override
    public void onListInitialized() {
        updateMapFragment();
    }

    @Override
    public void onCurrentCategoryChanged() {
        updateMapFragment();
    }

    private void updateMapFragment() {
        CategoryListFragment categoryListFragment = findCategoryListFragment();
        Optional<String> condition = categoryListFragment.getConditionToReceivePartners();
        NewPartnerPointsMapFragment mapFragment = findMapFragment();
        mapFragment.updateCondition(condition);
        if (condition.isPresent()) {
            setTitle(categoryListFragment.getSelectedCategoryName());
        }
    }

    private CategoryListFragment findCategoryListFragment() {
        return (CategoryListFragment) getSupportFragmentManager().findFragmentByTag(Tag.CATEGORY_LIST);
    }

    private NewPartnerPointsMapFragment findMapFragment() {
        return (NewPartnerPointsMapFragment) getSupportFragmentManager().findFragmentByTag(Tag.MAP);
    }

    @Override
    public void onDisplayDetails(List<PartnerPoint> partnerPoints) {
        findPartnerPointDetailsFragment().setPartnerPoints(partnerPoints);
    }

    private PartnerPointDetailsFragment findPartnerPointDetailsFragment() {
        return (PartnerPointDetailsFragment) getSupportFragmentManager().findFragmentByTag(Tag.PARTNER_POINT_DETAILS);
    }

    @Override
    public void onHideDetails() {
        findPartnerPointDetailsFragment().hide();
    }

    @Override
    public void onCustomBaseLocationIsSet() {
        dismissCustomBaseLocation.setVisibility(View.VISIBLE);
    }

    private void onDismissCustomBaseLocation() {
        findMapFragment().dismissCustomBaseLocation();
    }

    @Override
    public void onCustomBaseLocationIsDismissed() {
        dismissCustomBaseLocation.setVisibility(View.INVISIBLE);
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
                                onFilterAction();
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

    private void onFilterAction() {
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
    public void onDisplayingIsStarted() {
        findCategoryListFragment().setCategorySizeIsUnknown();
    }

    @Override
    public void onDisplayingIsCompleted(int numberOfDisplayedPartnerPoints) {
        findCategoryListFragment().setCategorySize(numberOfDisplayedPartnerPoints);
    }

}
