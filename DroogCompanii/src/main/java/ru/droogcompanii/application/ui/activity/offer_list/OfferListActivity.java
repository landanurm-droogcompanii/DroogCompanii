package ru.droogcompanii.application.ui.activity.offer_list;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import ru.droogcompanii.application.ui.util.FragmentUtils;

/**
 * Created by ls on 31.01.14.
 */
public class OfferListActivity extends ActionBarActivityWithUpButton
                            implements OfferListFragment.Callbacks {

    private static final String KEY_WHERE = "KEY_WHERE";
    private static final String KEY_INDEX_OF_CURRENT_OFFER_TYPE = "KEY_INDEX_OF_CURRENT_OFFER_TYPE";

    private static final String TAG_OFFER_LIST_FRAGMENT = "TAG_OFFER_LIST_FRAGMENT";


    private boolean isNeedToRefreshOfferList;
    private int indexOfCurrentOfferType;
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

        isNeedToRefreshOfferList = (savedInstanceState == null);

        if (savedInstanceState == null) {
            initStateByDefault();
        } else {
            restoreState(savedInstanceState);
        }

        initSpinnerOnActionBar();
    }

    private void initStateByDefault() {
        where = (Optional<String>) getIntent().getSerializableExtra(KEY_WHERE);
        indexOfCurrentOfferType = 0;
    }

    private void restoreState(Bundle savedInstanceState) {
        where = (Optional<String>) savedInstanceState.getSerializable(KEY_WHERE);
        indexOfCurrentOfferType = savedInstanceState.getInt(KEY_INDEX_OF_CURRENT_OFFER_TYPE);
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
        actionBar.setSelectedNavigationItem(indexOfCurrentOfferType);
        actionBar.setListNavigationCallbacks(spinnerAdapter, onNavigationListener);
    }

    private void onOfferTypeChanged(int index) {
        this.indexOfCurrentOfferType = index;
        if (isNeedToRefreshOfferList) {
            refreshOfferList();
        }
        isNeedToRefreshOfferList = true;
    }

    private void refreshOfferList() {
        FragmentUtils.removeFragmentByTag(this, TAG_OFFER_LIST_FRAGMENT);
        OfferType offerType = SpinnerAdapterOfferType.getOfferTypeByPosition(indexOfCurrentOfferType);
        Fragment fragment = OfferListFragment.newInstance(offerType, where);
        FragmentUtils.placeFragmentOnLayout(
                this, R.id.containerOfOfferListFragment, fragment, TAG_OFFER_LIST_FRAGMENT);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveStateInto(outState);
    }

    private void saveStateInto(Bundle outState) {
        outState.putSerializable(KEY_WHERE, where);
        outState.putInt(KEY_INDEX_OF_CURRENT_OFFER_TYPE, indexOfCurrentOfferType);
    }

    @Override
    public void onOfferItemClick(Offer offer) {
        OfferDetailsActivity.start(this, offer);
    }
}
