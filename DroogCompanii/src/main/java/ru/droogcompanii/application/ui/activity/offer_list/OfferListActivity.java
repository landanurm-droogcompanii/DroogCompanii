package ru.droogcompanii.application.ui.activity.offer_list;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.db_util.offers.OffersContract;
import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.data.offers.Offer;
import ru.droogcompanii.application.ui.activity.offer_details.OfferDetailsActivity;
import ru.droogcompanii.application.ui.fragment.offer_list.OfferListFragment;
import ru.droogcompanii.application.ui.util.ActionBarActivityWithUpButton;

/**
 * Created by ls on 31.01.14.
 */
public class OfferListActivity extends ActionBarActivityWithUpButton
                            implements OfferListFragment.Callbacks {

    public static final String KEY_WHERE = "KEY_WHERE";

    private OffersPagerAdapter offersPageAdapter;
    private ViewPager viewPager;
    private String where;


    public static void start(Context context, Partner partner) {
        String where = OffersContract.COLUMN_NAME_PARTNER_ID + "=" + partner.getId();;
        start(context, where);
    }

    public static void start(Context context) {
        start(context, "");
    }

    public static void start(Context context, String where) {
        Intent intent = new Intent(context, OfferListActivity.class);
        intent.putExtra(KEY_WHERE, where);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_pager);

        if (savedInstanceState == null) {
            where = getIntent().getStringExtra(KEY_WHERE);
        } else {
            where = savedInstanceState.getString(KEY_WHERE);
        }

        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setOffscreenPageLimit(3);
        offersPageAdapter = new OffersPagerAdapter(viewPager.getId(), getSupportFragmentManager(), where);
        viewPager.setAdapter(offersPageAdapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_WHERE, where);
    }

    @Override
    public void onOfferItemClick(Offer offer) {
        OfferDetailsActivity.start(this, offer);
    }
}
