package ru.droogcompanii.application.ui.activity.offer_list;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.widget.SpinnerAdapter;

import com.google.common.base.Optional;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.db_util.offers.OffersContract;
import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.data.offers.Offer;
import ru.droogcompanii.application.ui.activity.offer_details.OfferDetailsActivity;
import ru.droogcompanii.application.ui.fragment.offer_list.OfferListFragment;
import ru.droogcompanii.application.ui.fragment.offer_list.OfferType;
import ru.droogcompanii.application.util.StateManager;
import ru.droogcompanii.application.util.activity.ActionBarActivityWithUpButton;

/**
 * Created by ls on 31.01.14.
 */
public class OfferListActivity extends ActionBarActivityWithUpButton
                            implements OfferListFragment.Callbacks {

    private static class Key {
        public static final String ARE_OFFERS_BY_ONE_PARTNER = "ARE_OFFERS_BY_ONE_PARTNER";
        public static final String WHERE = "WHERE";
        public static final String INDEX_OF_CURRENT_OFFER_TYPE = "INDEX_OF_CURRENT_OFFER_TYPE";
    }

    private static final String TAG_OFFER_LIST_FRAGMENT = "TAG_OFFER_LIST_FRAGMENT";

    private boolean areOffersByOnePartner;
    private Optional<Integer> indexOfCurrentOfferType;
    private Optional<String> where;

    private final StateManager STATE_MANAGER = new StateManager() {
        @Override
        public void initStateByDefault() {
            areOffersByOnePartner = getIntent().getBooleanExtra(Key.ARE_OFFERS_BY_ONE_PARTNER, false);
            where = (Optional<String>) getIntent().getSerializableExtra(Key.WHERE);
            indexOfCurrentOfferType = Optional.absent();
        }

        @Override
        public void restoreState(Bundle savedInstanceState) {
            areOffersByOnePartner = savedInstanceState.getBoolean(Key.ARE_OFFERS_BY_ONE_PARTNER);
            where = (Optional<String>) savedInstanceState.getSerializable(Key.WHERE);
            indexOfCurrentOfferType = (Optional<Integer>) savedInstanceState.getSerializable(Key.INDEX_OF_CURRENT_OFFER_TYPE);
        }

        @Override
        public void saveState(Bundle outState) {
            outState.putBoolean(Key.ARE_OFFERS_BY_ONE_PARTNER, areOffersByOnePartner);
            outState.putSerializable(Key.WHERE, where);
            outState.putSerializable(Key.INDEX_OF_CURRENT_OFFER_TYPE, indexOfCurrentOfferType);
        }
    };


    public static void start(Context context, Partner partner) {
        String where = OffersContract.COLUMN_NAME_PARTNER_ID + "=" + partner.getId();
        start(context, Optional.of(where), true);
    }

    public static void start(Context context) {
        start(context, Optional.<String>absent(), false);
    }

    private static void start(Context context, Optional<String> where, boolean areOffersByOnePartner) {
        Intent intent = new Intent(context, OfferListActivity.class);
        intent.putExtra(Key.WHERE, where);
        intent.putExtra(Key.ARE_OFFERS_BY_ONE_PARTNER, areOffersByOnePartner);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_list);
        STATE_MANAGER.initState(savedInstanceState);
        initSpinnerOnActionBar();
    }

    private void initSpinnerOnActionBar() {
        SpinnerAdapter spinnerAdapter = new SpinnerAdapterOfferType(this);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        ActionBar.OnNavigationListener onNavigationListener = new ActionBar.OnNavigationListener() {
            @Override
            public boolean onNavigationItemSelected(int itemPosition, long itemId) {
                onOfferTypeChanged(itemPosition);
                return true;
            }
        };
        actionBar.setSelectedNavigationItem(indexOfCurrentOfferType.or(0));
        actionBar.setListNavigationCallbacks(spinnerAdapter, onNavigationListener);
    }

    private void onOfferTypeChanged(int index) {
        if (isIndexOfCurrentOfferType(index)) {
            return;
        }
        indexOfCurrentOfferType = Optional.of(index);
        refreshOfferList();
    }

    private boolean isIndexOfCurrentOfferType(int index) {
        return indexOfCurrentOfferType.isPresent() && (indexOfCurrentOfferType.get() == index);
    }

    private void refreshOfferList() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment previousFragment = fragmentManager.findFragmentByTag(TAG_OFFER_LIST_FRAGMENT);
        if (previousFragment != null) {
            transaction.remove(previousFragment);
        }
        OfferType offerType = SpinnerAdapterOfferType.getOfferTypeByPosition(indexOfCurrentOfferType.get());
        Fragment actualFragment = OfferListFragment.newInstance(offerType, where);
        transaction.add(R.id.containerOfOfferListFragment, actualFragment, TAG_OFFER_LIST_FRAGMENT);
        transaction.commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        STATE_MANAGER.saveState(outState);
    }

    @Override
    public void onOfferItemClick(Offer offer) {
        OfferDetailsActivity.start(this, offer, areOffersByOnePartner);
    }
}
