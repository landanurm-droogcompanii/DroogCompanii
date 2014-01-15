package ru.droogcompanii.application.view.activity_3.partner_activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.Serializable;
import java.util.List;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.data_structure.Partner;
import ru.droogcompanii.application.data.data_structure.PartnerPoint;
import ru.droogcompanii.application.util.Keys;
import ru.droogcompanii.application.view.activity_3.search_result_map_activity.SearchResultMapActivity;
import ru.droogcompanii.application.view.fragment.partner_points_map_fragment.PartnerPointsProvider;

/**
 * Created by ls on 15.01.14.
 */
public class PartnerActivity extends Activity {

    private List<PartnerPoint> partnerPoints;
    private Partner partner;

    private ListView partnerPointListView;
    private View expandListButton;
    private View rollUpListButton;
    private View layoutToHideWhenListIsExpanded;
    private View partnerInfoContainer;
    private View goOnMapButton;


    public static void start(Context context, Partner partner, List<PartnerPoint> partnerPoints) {
        Intent intent = new Intent(context, PartnerActivity.class);
        intent.putExtra(Keys.partner, partner);
        intent.putExtra(Keys.partnerPoints, (Serializable) partnerPoints);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3_partner);

        if (savedInstanceState == null) {
            Bundle args = getIntent().getExtras();
            init(args);
        } else {
            init(savedInstanceState);
        }
        initView();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(Keys.partner, partner);
        outState.putSerializable(Keys.partnerPoints, (Serializable) partnerPoints);
    }

    private void init(Bundle bundle) {
        partner = (Partner) bundle.getSerializable(Keys.partner);
        partnerPoints = (List<PartnerPoint>) bundle.getSerializable(Keys.partnerPoints);
    }

    private void initView() {
        findViewsAndInitStartState();

        PartnerInfoFiller.fill(partnerInfoContainer, partner);

        if (partnerHasNoPoints()) {
            onPartnerHasNoPoints();
        } else {
            onPartnerHasPoints();
        }

    }

    private void findViewsAndInitStartState() {
        goOnMapButton = findViewById(R.id.goOnMapButton);
        layoutToHideWhenListIsExpanded = findViewById(R.id.layoutToHideWhenListIsExpanded);
        partnerInfoContainer = findViewById(R.id.layoutToHideWhenListIsExpanded);
        partnerPointListView = (ListView) findViewById(R.id.partnerPointListView);
        rollUpListButton = findViewById(R.id.rollUpListButton);
        expandListButton = findViewById(R.id.expandListButton);

        rollUpList();
    }

    private boolean partnerHasNoPoints() {
        return partnerPoints.isEmpty();
    }

    private void onPartnerHasNoPoints() {
        hide(expandListButton);
        hide(goOnMapButton);
    }

    private void onPartnerHasPoints() {
        initSinglePartnerPointListItemView();
        initAbilityToExpandPartnerPointList();
    }

    private void initSinglePartnerPointListItemView() {
        PartnerPointListItemViewInflater inflater = new PartnerPointListItemViewInflater(this);
        View singlePartnerPointListItemView = inflater.inflate();
        PartnerPoint partnerPoint = getPartnerPointForSingleListItem();
        PartnerPointInfoFiller.fill(singlePartnerPointListItemView, partnerPoint);
        FrameLayout container = (FrameLayout) findViewById(R.id.containerOfSinglePartnerPointListItem);
        container.addView(singlePartnerPointListItemView);
    }

    private PartnerPoint getPartnerPointForSingleListItem() {
        return partnerPoints.get(0);
    }

    private void initAbilityToExpandPartnerPointList() {
        if (partnerPoints.size() > 1) {
            enableAbilityToExpandPartnerPointList();
        } else {
            disableAbilityToExpandPartnerPointList();
        }
    }

    private void disableAbilityToExpandPartnerPointList() {
        hide(expandListButton);
    }

    private void enableAbilityToExpandPartnerPointList() {
        ListAdapter adapter = new PartnerPointListAdapter(this, partnerPoints);
        partnerPointListView.setAdapter(adapter);
        rollUpListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rollUpList();
            }
        });
        expandListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expandList();
            }
        });
        goOnMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goOnMap();
            }
        });
    }

    private void hide(View view) {
        view.setVisibility(View.INVISIBLE);
    }

    private void show(View view) {
        view.setVisibility(View.VISIBLE);
    }

    private void rollUpList() {
        show(expandListButton);
        hide(rollUpListButton);
        hide(partnerPointListView);
        show(layoutToHideWhenListIsExpanded);
    }

    private void expandList() {
        hide(expandListButton);
        show(rollUpListButton);
        show(partnerPointListView);
        hide(layoutToHideWhenListIsExpanded);
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
