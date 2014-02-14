package ru.droogcompanii.application.ui.fragment.partner_point_list;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.AdapterView;

import java.io.Serializable;
import java.util.List;

import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.ui.activity.partner_details.PartnerDetailsActivity;
import ru.droogcompanii.application.ui.fragment.partner_points_map.PartnerPointsProvider;

/**
 * Created by ls on 12.02.14.
 */
public class PartnerPointListFragment extends ListFragment
        implements AdapterView.OnItemClickListener, PartnerPointsProvider {

    public static interface Callbacks {
        void onPartnerPointClick(PartnerPoint partnerPoint);
    }


    private Callbacks callbacks;
    private List<PartnerPoint> partnerPoints;
    private PartnerPointListAdapter adapter;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callbacks = (Callbacks) activity;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState == null) {
            init(getArguments());
        } else {
            init(savedInstanceState);
        }
    }

    private void init(Bundle bundle) {
        partnerPoints = (List<PartnerPoint>) bundle.getSerializable(PartnerDetailsActivity.Key.PARTNER_POINTS);
        adapter = new PartnerPointListAdapter(getActivity(), partnerPoints);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(PartnerDetailsActivity.Key.PARTNER_POINTS, (Serializable) partnerPoints);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        PartnerPoint clickedPartnerPoint = adapter.getItem(position);
        callbacks.onPartnerPointClick(clickedPartnerPoint);
    }

    @Override
    public String getTitle(Context context) {
        return "";
    }

    @Override
    public List<PartnerPoint> getPartnerPoints(Context context) {
        return partnerPoints;
    }
}
