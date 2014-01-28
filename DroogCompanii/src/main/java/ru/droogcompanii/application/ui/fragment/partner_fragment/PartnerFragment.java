package ru.droogcompanii.application.ui.fragment.partner_fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.Serializable;
import java.util.List;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.util.Keys;
import ru.droogcompanii.application.ui.activity_3.partner_activity.PartnerInfoFiller;
import ru.droogcompanii.application.ui.activity_3.partner_activity.PartnerPointInfoFiller;
import ru.droogcompanii.application.ui.activity_3.partner_activity.PartnerPointListAdapter;
import ru.droogcompanii.application.ui.activity_3.partner_activity.PartnerPointListItemViewInflater;

/**
 * Created by ls on 22.01.14.
 */
public class PartnerFragment extends android.support.v4.app.Fragment {

    private boolean needToExpandList;
    private List<PartnerPoint> partnerPoints;
    private ListView partnerPointListView;
    private Partner partner;
    private View expandListButton;
    private View rollUpListButton;
    private View layoutToHideWhenListIsExpanded;
    private View partnerInfoContainer;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_partner, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState == null) {
            init(getArguments());
        } else {
            init(savedInstanceState);
        }

        initView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(Keys.partner, partner);
        outState.putSerializable(Keys.partnerPoints, (Serializable) partnerPoints);
        outState.putBoolean(Keys.needToExpandList, needToExpandList);
    }

    private void init(Bundle bundle) {
        partner = (Partner) bundle.getSerializable(Keys.partner);
        partnerPoints = (List<PartnerPoint>) bundle.getSerializable(Keys.partnerPoints);
        needToExpandList = bundle.getBoolean(Keys.needToExpandList);
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
        layoutToHideWhenListIsExpanded = findViewById(R.id.layoutToHideWhenListIsExpanded);
        partnerInfoContainer = findViewById(R.id.layoutToHideWhenListIsExpanded);
        partnerPointListView = (ListView) findViewById(R.id.partnerPointListView);
        rollUpListButton = findViewById(R.id.rollUpListButton);
        expandListButton = findViewById(R.id.expandListButton);

        if (needToExpandList) {
            expandList();
        } else {
            rollUpList();
        }
    }

    private boolean partnerHasNoPoints() {
        return partnerPoints.isEmpty();
    }

    private void onPartnerHasNoPoints() {
        disableAbilityToExpandPartnerPointList();
    }

    private void onPartnerHasPoints() {
        initSingleItemOfPartnerPointList();
        initAbilityToExpandPartnerPointList();
    }

    private void initSingleItemOfPartnerPointList() {
        PartnerPointListItemViewInflater inflater = new PartnerPointListItemViewInflater(getActivity());
        View singlePartnerPointListItemView = inflater.inflate();
        PartnerPoint partnerPoint = getPartnerPointForSingleListItem();
        PartnerPointInfoFiller.fill(singlePartnerPointListItemView, partnerPoint);
        FrameLayout container = (FrameLayout) findViewById(R.id.containerOfSinglePartnerPointListItem);
        container.addView(singlePartnerPointListItemView);
    }

    private View findViewById(int id) {
        return getActivity().findViewById(id);
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

    private void enableAbilityToExpandPartnerPointList() {
        ListAdapter adapter = new PartnerPointListAdapter(getActivity(), partnerPoints);
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
    }

    private void disableAbilityToExpandPartnerPointList() {
        hide(expandListButton);
    }

    private static void hide(View view) {
        view.setVisibility(View.INVISIBLE);
    }

    private static void show(View view) {
        view.setVisibility(View.VISIBLE);
    }

    private void rollUpList() {
        show(expandListButton);
        hide(rollUpListButton);
        hide(partnerPointListView);
        show(layoutToHideWhenListIsExpanded);
        needToExpandList = false;
    }

    private void expandList() {
        hide(expandListButton);
        show(rollUpListButton);
        show(partnerPointListView);
        hide(layoutToHideWhenListIsExpanded);
        needToExpandList = true;
    }
}
