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
import ru.droogcompanii.application.ui.util.ActionBarActivityWithUpButton;

/**
 * Created by ls on 31.01.14.
 */
public class OfferListActivity extends ActionBarActivityWithUpButton
                            implements OfferListFragment.Callbacks {

    private static final String KEY_WHERE = "KEY_WHERE";
    private static final String KEY_INDEX_OF_CURRENT_OFFER_TYPE = "KEY_INDEX_OF_CURRENT_OFFER_TYPE";

    private static final String TAG_OFFER_LIST_FRAGMENT = "TAG_OFFER_LIST_FRAGMENT";

    private Optional<Integer> indexOfCurrentOfferType;
    private Optional<String> where;


    public static void start(Context context, Partner partner) {
        String where = OffersContract.COLUMN_NAME_PARTNER_ID + "=" + partner.getId();
        start(context, Optional.of(where));
    }

    public static void start(Context context) {
        start(context, Optional.<String>absent());
    }

    private static void start(Context context, Optional<String> where) {
        Intent intent = new Intent(context, OfferListActivity.class);
        intent.putExtra(KEY_WHERE, where);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_list);

        if (savedInstanceState == null) {
            initStateByDefault();
        } else {
            restoreState(savedInstanceState);
        }

        initSpinnerOnActionBar();
    }

    private void initStateByDefault() {
        where = (Optional<String>) getIntent().getSerializableExtra(KEY_WHERE);
        indexOfCurrentOfferType = Optional.absent();
    }

    private void restoreState(Bundle savedInstanceState) {
        where = (Optional<String>) savedInstanceState.getSerializable(KEY_WHERE);
        indexOfCurrentOfferType = (Optional<Integer>) savedInstanceState.getSerializable(KEY_INDEX_OF_CURRENT_OFFER_TYPE);
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
        final int defaultSelectedIndex = 0;
        actionBar.setSelectedNavigationItem(indexOfCurrentOfferType.or(defaultSelectedIndex));
        actionBar.setListNavigationCallbacks(spinnerAdapter, onNavigationListener);
    }

    private void onOfferTypeChanged(int index) {
        if (indexOfCurrentOfferType.isPresent() && indexOfCurrentOfferType.get() == index) {
            return;
        }
        this.indexOfCurrentOfferType = Optional.of(index);
        refreshOfferList();
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
        saveStateInto(outState);
    }

    private void saveStateInto(Bundle outState) {
        outState.putSerializable(KEY_WHERE, where);
        outState.putSerializable(KEY_INDEX_OF_CURRENT_OFFER_TYPE, indexOfCurrentOfferType);
    }

    @Override
    public void onOfferItemClick(Offer offer) {
        OfferDetailsActivity.start(this, offer);
    }
}
