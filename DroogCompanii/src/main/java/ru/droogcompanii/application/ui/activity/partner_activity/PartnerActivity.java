package ru.droogcompanii.application.ui.activity.partner_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import java.io.Serializable;
import java.util.List;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.ui.activity.search_result_map_activity.SearchResultMapActivity;
import ru.droogcompanii.application.ui.fragment.partner_fragment.PartnerFragment;
import ru.droogcompanii.application.ui.fragment.partner_points_map_fragment.PartnerPointsProvider;
import ru.droogcompanii.application.ui.helpers.ActionBarActivityWithGoToMapItem;
import ru.droogcompanii.application.util.Keys;

/**
 * Created by ls on 15.01.14.
 */
public class PartnerActivity extends ActionBarActivityWithGoToMapItem {

    private Partner partner;
    private List<PartnerPoint> partnerPoints;


    public static void start(Context context, Partner partner, List<PartnerPoint> partnerPoints) {
        Intent intent = new Intent(context, PartnerActivity.class);
        intent.putExtra(Keys.partner, partner);
        intent.putExtra(Keys.partnerPoints, (Serializable) partnerPoints);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner);

        if (savedInstanceState == null) {
            onFirstLaunch();
        } else {
            onRelaunch(savedInstanceState);
        }

    }

    private void onFirstLaunch() {
        Bundle bundle = getPassedBundle();
        initState(bundle);
        startPartnerFragment(bundle);
    }

    private Bundle getPassedBundle() {
        return getIntent().getExtras();
    }

    private void onRelaunch(Bundle savedInstanceState) {
        initState(savedInstanceState);
    }

    private void initState(Bundle bundle) {
        partner = (Partner) bundle.getSerializable(Keys.partner);
        partnerPoints = (List<PartnerPoint>) bundle.getSerializable(Keys.partnerPoints);
        setTitle(prepareTitle());
        updateGoToMapItemVisible();
    }

    private String prepareTitle() {
        return partner.title;
    }

    private void startPartnerFragment(Bundle args) {
        PartnerFragment partnerFragment = new PartnerFragment();
        partnerFragment.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.containerOfPartnerFragment, partnerFragment);
        transaction.commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putSerializable(Keys.partner, partner);
        savedInstanceState.putSerializable(Keys.partnerPoints, (Serializable) partnerPoints);
    }

    @Override
    protected boolean isGoToMapItemVisible() {
        return (partnerPoints != null) && !partnerPoints.isEmpty();
    }

    @Override
    protected void onNeedToGoToMap() {
        Intent intent = new Intent(this, SearchResultMapActivity.class);
        intent.putExtra(Keys.partnerPointsProvider, new PartnerPointsProviderImpl(prepareTitle(), partnerPoints));
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
