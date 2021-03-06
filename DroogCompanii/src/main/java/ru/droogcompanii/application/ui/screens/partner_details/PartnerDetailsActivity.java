package ru.droogcompanii.application.ui.screens.partner_details;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.google.common.base.Optional;

import java.io.Serializable;
import java.util.List;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.db_util.contracts.PartnerPointsContract;
import ru.droogcompanii.application.data.db_util.hierarchy_of_partners.PartnersReader;
import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.data.offers.Offer;
import ru.droogcompanii.application.ui.screens.main.map.CustomMapFragmentWithBaseLocation;
import ru.droogcompanii.application.ui.screens.main.map.PartnerPointsMapFragment;
import ru.droogcompanii.application.ui.screens.main.partner_point_details.PartnerPointDetailsFragment;
import ru.droogcompanii.application.util.StateManager;
import ru.droogcompanii.application.util.ui.activity.ActionBarActivityWithUpButton;
import ru.droogcompanii.application.util.ui.activity.ReuseAlreadyLaunchedActivityFlag;
import ru.droogcompanii.application.util.ui.activity.menu_helper.MenuHelper;
import ru.droogcompanii.application.util.ui.activity.menu_helper.MenuHelperItemsProvider;
import ru.droogcompanii.application.util.ui.activity.menu_helper.menu_item_helper.MenuItemHelper;
import ru.droogcompanii.application.util.ui.activity.menu_helper.menu_item_helper.MenuItemHelpers;

/**
 * Created by ls on 15.01.14.
 */
public class PartnerDetailsActivity extends ActionBarActivityWithUpButton
                            implements PartnerDetailsFragment.Callbacks,
                                       PartnerPointsMapFragment.Callbacks,
                                       CustomMapFragmentWithBaseLocation.Callbacks {


    public static interface InputProvider {
        Partner getPartner(Context context);
        String getConditionToReceivePartnerPoints();
        Optional<PartnerPoint> getPartnerPointToBeSelected();
    }

    private static class Key {
        public static final String INPUT_PROVIDER = "INPUT_PROVIDER";
        public static final String IS_DETAILS_LOADED = "IS_DETAILS_LOADED";
        public static final String IS_MAP_INITIALIZED = "IS_MAP_INITIALIZED";
        public static final String IS_FIRST_DISPLAYING = "IS_FIRST_DISPLAYING";
        public static final String WITH_FILTERS = "WITH_FILTERS";
    }

    private static class FragmentTag {
        public static final String PARTNER_DETAILS = "PARTNER_DETAILS";
        public static final String PARTNER_POINT_DETAILS = "PARTNER_POINT_DETAILS";
        public static final String PARTNER_POINTS_MAP = "PARTNER_POINTS_MAP";
    }


    public static class InputProviderByPartnerId implements InputProvider, Serializable {
        private final int partnerId;

        public InputProviderByPartnerId(int partnerId) {
            this.partnerId = partnerId;
        }

        @Override
        public Partner getPartner(Context context) {
            PartnersReader reader = new PartnersReader(context);
            return reader.getPartnerById(partnerId);
        }

        @Override
        public String getConditionToReceivePartnerPoints() {
            String columnPartnerId = PartnerPointsContract.COLUMN_PARTNER_ID;
            return columnPartnerId + " = " + partnerId;
        }

        @Override
        public Optional<PartnerPoint> getPartnerPointToBeSelected() {
            return Optional.absent();
        }
    }

    public static class InputProviderByPartnerPoint implements InputProvider, Serializable {
        private final PartnerPoint partnerPoint;

        public InputProviderByPartnerPoint(PartnerPoint partnerPoint) {
            this.partnerPoint = partnerPoint;
        }

        @Override
        public Partner getPartner(Context context) {
            PartnersReader reader = new PartnersReader(context);
            return reader.getPartnerByPointId(partnerPoint.getId());
        }

        @Override
        public String getConditionToReceivePartnerPoints() {
            return PartnerPointsContract.COLUMN_PARTNER_ID + " IN ( " +
                    "SELECT " + PartnerPointsContract.COLUMN_PARTNER_ID +
                    " FROM " + PartnerPointsContract.TABLE_NAME +
                    " WHERE " + PartnerPointsContract.COLUMN_ID + "=" + partnerPoint.getId() +
            ")";
        }

        @Override
        public Optional<PartnerPoint> getPartnerPointToBeSelected() {
            return Optional.of(partnerPoint);
        }
    }

    public static void startWithoutFilters(Context context, Partner partner) {
        start(context, new InputProviderByPartnerId(partner.getId()), false);
    }


    public static void startWithoutFilters(Context context, PartnerPoint partnerPoint) {
        start(context, new InputProviderByPartnerPoint(partnerPoint), false);
    }

    public static void startWithoutFilters(Context context, Offer offer) {
        PartnerDetailsActivity.InputProviderByPartnerId inputProvider =
                new PartnerDetailsActivity.InputProviderByPartnerId(offer.getPartnerId());
        start(context, inputProvider, false);
    }

    public static void startWithFilters(Context context, Partner partner) {
        start(context, new InputProviderByPartnerId(partner.getId()), true);
    }


    public static void startWithFilters(Context context, PartnerPoint partnerPoint) {
        start(context, new InputProviderByPartnerPoint(partnerPoint), true);
    }

    public static void startWithFilters(Context context, Offer offer) {
        InputProviderByPartnerId inputProvider =
                new InputProviderByPartnerId(offer.getPartnerId());
        start(context, inputProvider, true);
    }


    public static void start(Context context, InputProvider inputProvider, boolean withFilters) {
        Intent intent = new Intent(context, PartnerDetailsActivity.class);
        intent.putExtra(Key.INPUT_PROVIDER, (Serializable) inputProvider);
        intent.putExtra(Key.WITH_FILTERS, withFilters);
        ReuseAlreadyLaunchedActivityFlag.set(intent);
        context.startActivity(intent);
    }


    private boolean isFirstDisplaying;
    private boolean isMapInitialized;
    private boolean isDetailsLoaded;
    private InputProvider inputProvider;


    private final StateManager STATE_MANAGER = new StateManager() {
        @Override
        public void initStateByDefault() {
            isFirstDisplaying = true;
            isMapInitialized = false;
            inputProvider = (InputProvider) getIntent().getSerializableExtra(Key.INPUT_PROVIDER);
            isDetailsLoaded = false;
            placeFragmentsOnActivity();
        }

        @Override
        public void restoreState(Bundle savedInstanceState) {
            isFirstDisplaying = savedInstanceState.getBoolean(Key.IS_FIRST_DISPLAYING);
            if (!isMapInitialized) {
                isMapInitialized = savedInstanceState.getBoolean(Key.IS_MAP_INITIALIZED);
            }
            inputProvider = (InputProvider) savedInstanceState.getSerializable(Key.INPUT_PROVIDER);
            isDetailsLoaded = savedInstanceState.getBoolean(Key.IS_DETAILS_LOADED);
        }

        @Override
        public void saveState(Bundle outState) {
            outState.putBoolean(Key.IS_FIRST_DISPLAYING, isFirstDisplaying);
            outState.putBoolean(Key.IS_MAP_INITIALIZED, isMapInitialized);
            outState.putSerializable(Key.INPUT_PROVIDER, (Serializable) inputProvider);
            outState.putBoolean(Key.IS_DETAILS_LOADED, isDetailsLoaded);
        }
    };


    private void placeFragmentsOnActivity() {
        boolean withFilters = getIntent().getBooleanExtra(Key.WITH_FILTERS, true);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.partnerDetailsFragment,
                        PartnerDetailsFragment.newInstance(inputProvider),
                        FragmentTag.PARTNER_DETAILS);
        transaction.add(R.id.partnerPointsMapFragment,
                        PartnerPointsMapFragment.newInstance(withFilters),
                        FragmentTag.PARTNER_POINTS_MAP);
        transaction.add(R.id.partnerPointDetailsFragment,
                        PartnerPointDetailsFragment.newInstanceWithoutGoToPartnerButton(),
                        FragmentTag.PARTNER_POINT_DETAILS);
        transaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner_details);
        STATE_MANAGER.initState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        STATE_MANAGER.saveState(outState);
    }

    @Override
    public void onPartnerDetailsLoaded() {
        isDetailsLoaded = true;
        initMapIfNeed();
    }

    private void initMapIfNeed() {
        if (!isMapInitialized) {
            initMap();
            isMapInitialized = true;
        }
    }

    private void initMap() {
        String condition = inputProvider.getConditionToReceivePartnerPoints();
        findMapFragment().updateCondition(Optional.of(condition));
    }

    private PartnerPointsMapFragment findMapFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        return (PartnerPointsMapFragment) fragmentManager.findFragmentByTag(FragmentTag.PARTNER_POINTS_MAP);
    }

    @Override
    public void onMarkerClicked(List<PartnerPoint> partnerPoints) {
        findPartnerPointDetailsFragment().setPartnerPoints(partnerPoints);
    }

    private PartnerPointDetailsFragment findPartnerPointDetailsFragment() {
        return (PartnerPointDetailsFragment)
                getSupportFragmentManager().findFragmentByTag(FragmentTag.PARTNER_POINT_DETAILS);
    }

    @Override
    public void onNoClickedMarker() {
        findPartnerPointDetailsFragment().hide();
    }

    @Override
    public void onMapInitialized() {
        if (isDetailsLoaded) {
            initMapIfNeed();
        }
    }

    @Override
    public void onDisplayingIsStarting() {
        // skip
    }

    @Override
    public void onDisplayingIsCompleted(int numberOfDisplayedPartnerPoints) {
        if (isFirstDisplaying) {
            selectPartnerPointIdNeed();
            isFirstDisplaying = false;
        }
    }

    private void selectPartnerPointIdNeed() {
        Optional<PartnerPoint> partnerPointToBeSelected = inputProvider.getPartnerPointToBeSelected();
        if (partnerPointToBeSelected.isPresent()) {
            findMapFragment().selectPartnerPoint(partnerPointToBeSelected.get());
        }
    }

    @Override
    public void onCustomBaseLocationIsSet() {
        // skip
    }

    @Override
    public void onCustomBaseLocationIsDismissed() {
        // skip
    }

    @Override
    public void onNeedToEnableLocationService() {
        // do nothing
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