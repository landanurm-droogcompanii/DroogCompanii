package ru.droogcompanii.application.ui.fragment.partner_point_list;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.AdapterView;

import java.io.Serializable;
import java.util.List;

import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPointImpl;
import ru.droogcompanii.application.ui.activity.partner_details.PartnerDetailsActivity;

/**
 * Created by ls on 12.02.14.
 */
public class PartnerPointListFragment extends ListFragment implements AdapterView.OnItemClickListener {

    public static interface Callbacks {
        void onPartnerPointClick(PartnerPointImpl partnerPoint);
    }


    private Callbacks callbacks;
    private List<PartnerPointImpl> partnerPoints;
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
        partnerPoints = (List<PartnerPointImpl>) bundle.getSerializable(PartnerDetailsActivity.Key.PARTNER_POINTS);
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
        PartnerPointImpl clickedPartnerPoint = adapter.getItem(position);
        callbacks.onPartnerPointClick(clickedPartnerPoint);
    }
}
