package ru.droogcompanii.application.ui.activity.partner_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import java.io.Serializable;
import java.util.List;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.ui.activity.search_result_map_activity.SearchResultMapActivity;
import ru.droogcompanii.application.ui.fragment.partner_fragment.PartnerFragment;
import ru.droogcompanii.application.ui.fragment.partner_points_map_fragment.PartnerPointsProvider;
import ru.droogcompanii.application.util.Keys;

/**
 * Created by ls on 15.01.14.
 */
public class PartnerActivity extends android.support.v4.app.FragmentActivity {

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

        findViewById(R.id.goOnMapButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goOnMap();
            }
        });
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
        partnerPoints = (List<PartnerPoint>) bundle.getSerializable(Keys.partnerPoints);
        if (partnerPoints.isEmpty()) {
            findViewById(R.id.goOnMapButton).setVisibility(View.INVISIBLE);
        }
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
        savedInstanceState.putSerializable(Keys.partnerPoints, (Serializable) partnerPoints);
    }

    private void goOnMap() {
        Intent intent = new Intent(this, SearchResultMapActivity.class);
        intent.putExtra(Keys.partnerPointsProvider, new PartnerPointsProviderImpl(partnerPoints));
        startActivity(intent);
    }

    private static class PartnerPointsProviderImpl implements PartnerPointsProvider, Serializable {
        private List<PartnerPoint> partnerPoints;

        PartnerPointsProviderImpl(List<PartnerPoint> partnerPoints) {
            this.partnerPoints = partnerPoints;
        }

        @Override
        public List<PartnerPoint> getPartnerPoints(Context context) {
            return partnerPoints;
        }
    }

}
