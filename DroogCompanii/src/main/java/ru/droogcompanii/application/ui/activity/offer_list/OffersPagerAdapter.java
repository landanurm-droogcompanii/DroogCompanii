package ru.droogcompanii.application.ui.activity.offer_list;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.Arrays;
import java.util.List;

import ru.droogcompanii.application.ui.fragment.offer_list.OfferListFragment;

/**
 * Created by ls on 07.03.14.
 */
public class OffersPagerAdapter extends FragmentStatePagerAdapter {

    private static interface OfferPageHelper {
        Fragment createFragment(String where);
    }

    private static class OfferPageHelperImpl implements OfferPageHelper {

        private final OfferListFragment.OfferType offerType;

        public OfferPageHelperImpl(OfferListFragment.OfferType offerType) {
            this.offerType = offerType;
        }

        @Override
        public Fragment createFragment(String where) {
            return OfferListFragment.newInstance(offerType, where);
        }
    }

    private static final List<? extends OfferPageHelper> OFFER_PAGE_HELPERS = Arrays.asList(
        new OfferPageHelperImpl(OfferListFragment.OfferType.SPECIAL),
        new OfferPageHelperImpl(OfferListFragment.OfferType.ACTUAL),
        new OfferPageHelperImpl(OfferListFragment.OfferType.PAST)
    );


    private final int viewPagerId;
    private final FragmentManager fragmentManager;
    private final String where;

    public OffersPagerAdapter(int viewPagerId, FragmentManager fragmentManager, String where) {
        super(fragmentManager);
        this.viewPagerId = viewPagerId;
        this.fragmentManager = fragmentManager;
        this.where = where;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = findFragmentByPosition(position);
        if (fragment != null) {
            return fragment;
        }
        OfferPageHelper pageHelper = OFFER_PAGE_HELPERS.get(position);
        return pageHelper.createFragment(where);
    }

    private Fragment findFragmentByPosition(int position) {
        return fragmentManager.findFragmentByTag(getFragmentTag(position));
    }

    private String getFragmentTag(int position) {
        return "android:switcher:" + viewPagerId + ":" + position;
    }

    @Override
    public int getCount() {
        return OFFER_PAGE_HELPERS.size();
    }
}
