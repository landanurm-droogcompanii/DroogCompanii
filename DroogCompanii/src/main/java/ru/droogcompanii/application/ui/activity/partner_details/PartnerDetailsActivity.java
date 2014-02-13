package ru.droogcompanii.application.ui.activity.partner_details;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import java.io.Serializable;
import java.util.List;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPointImpl;
import ru.droogcompanii.application.ui.activity.search_result_map.SearchResultMapActivity;
import ru.droogcompanii.application.ui.fragment.partner_details.PartnerDetailsFragment;
import ru.droogcompanii.application.ui.fragment.partner_point_list.PartnerPointListFragment;
import ru.droogcompanii.application.ui.fragment.partner_points_map.PartnerPointsProvider;
import ru.droogcompanii.application.ui.helpers.ActionBarActivityWithGoToMapItem;
import ru.droogcompanii.application.util.Keys;

/**
 * Created by ls on 15.01.14.
 */
public class PartnerDetailsActivity extends ActionBarActivityWithGoToMapItem
                                    implements PartnerPointListFragment.Callbacks {

    public static class Key {
        public static final String PARTNER = "partner";
        public static final String PARTNER_POINT = "partner_point";
        public static final String PARTNER_POINTS = PARTNER_POINT + "s";
    }

    private static final String LIST_FRAGMENT_TAG = "List Fragment Tag";
    private static final String DETAILS_FRAGMENT_TAG = "Details Fragment Tag";

    private Partner partner;
    private List<PartnerPoint> partnerPoints;


    public static void start(Context context, Partner partner, List<PartnerPoint> partnerPoints) {
        Intent intent = new Intent(context, PartnerDetailsActivity.class);
        intent.putExtra(Key.PARTNER, (Serializable) partner);
        intent.putExtra(Key.PARTNER_POINTS, (Serializable) partnerPoints);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner);

        if (savedInstanceState == null) {
            initState(getPassedBundle());
            init();
        } else {
            initState(savedInstanceState);
        }

        setTitle(prepareTitle());
    }

    private void initState(Bundle bundle) {
        partner = (Partner) bundle.getSerializable(Key.PARTNER);
        partnerPoints = (List<PartnerPoint>) bundle.getSerializable(Key.PARTNER_POINTS);
        updateGoToMapItemVisible();
    }

    @Override
    public void onPartnerPointClick(PartnerPointImpl partnerPoint) {
        startPartnerDetailsFragment(partnerPoint);
    }

    private void init() {
        if (isNeedToShowPartnerPointListFirst()) {
            startPartnerPointListFragment();
        } else {
            startPartnerDetailsFragment(onlyExistingPartnerPoint());
        }
    }
    private Bundle getPassedBundle() {
        return getIntent().getExtras();
    }

    private boolean isNeedToShowPartnerPointListFirst() {
        return (partnerPoints != null) && (partnerPoints.size() > 1);
    }

    private void startPartnerPointListFragment() {
        PartnerPointListFragment partnerPointListFragment = preparePartnerPointListFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.containerOfFragment, partnerPointListFragment, LIST_FRAGMENT_TAG);
        transaction.commit();
    }

    private PartnerPointListFragment preparePartnerPointListFragment() {
        PartnerPointListFragment fragment = new PartnerPointListFragment();
        Bundle args = new Bundle();
        args.putSerializable(Key.PARTNER_POINTS, (Serializable) partnerPoints);
        fragment.setArguments(args);
        return fragment;
    }

    private PartnerPoint onlyExistingPartnerPoint() {
        return partnerPoints.get(0);
    }

    private void startPartnerDetailsFragment(PartnerPoint partnerPoint) {
        PartnerDetailsFragment partnerDetailsFragment = preparePartnerDetailsFragment(partnerPoint);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (isListFragmentDisplayed()) {
            transaction.replace(R.id.containerOfFragment, partnerDetailsFragment, DETAILS_FRAGMENT_TAG);
            transaction.addToBackStack(null);
        } else {
            transaction.add(R.id.containerOfFragment, partnerDetailsFragment, DETAILS_FRAGMENT_TAG);
        }
        transaction.commit();
    }

    private PartnerDetailsFragment preparePartnerDetailsFragment(PartnerPoint partnerPoint) {
        PartnerDetailsFragment partnerDetailsFragment = new PartnerDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(Key.PARTNER, (Serializable) partner);
        args.putSerializable(Key.PARTNER_POINT, (Serializable) partnerPoint);
        partnerDetailsFragment.setArguments(args);
        return partnerDetailsFragment;
    }

    private boolean isListFragmentDisplayed() {
        return getSupportFragmentManager().findFragmentByTag(LIST_FRAGMENT_TAG) != null;
    }

    private String prepareTitle() {
        return partner.getTitle();
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putSerializable(Key.PARTNER, (Serializable) partner);
        savedInstanceState.putSerializable(Key.PARTNER_POINTS, (Serializable) partnerPoints);
    }

    @Override
    protected boolean isGoToMapItemVisible() {
        return (partnerPoints != null) && !partnerPoints.isEmpty();
    }

    @Override
    protected void onNeedToGoToMap() {
        Intent intent = new Intent(this, SearchResultMapActivity.class);
        PartnerPointsProviderImpl partnerPointsProvider =
                new PartnerPointsProviderImpl(prepareTitle(), partnerPoints);
        intent.putExtra(Keys.partnerPointsProvider, partnerPointsProvider);
        startActivity(intent);
    }

    private static class PartnerPointsProviderImpl implements PartnerPointsProvider, Serializable {
        private final String title;
        private final List<PartnerPoint> partnerPoints;

        PartnerPointsProviderImpl(String title, List<PartnerPoint> partnerPoints) {
            this.title = title;
            this.partnerPoints = partnerPoints;
        }

        @Override
        public String getTitle(Context context) {
            return title;
        }

        @Override
        public List<PartnerPoint> getPartnerPoints(Context context) {
            return partnerPoints;
        }
    }

}
