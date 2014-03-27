package ru.droogcompanii.application.ui.fragment.partner_details;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPointImpl;
import ru.droogcompanii.application.ui.activity.partner_details.PartnerDetailsActivity;
import ru.droogcompanii.application.ui.fragment.partner_points_map.PartnerPointsProvider;

/**
 * Created by ls on 22.01.14.
 */
public class PartnerDetailsFragment extends android.support.v4.app.Fragment
        implements PartnerPointsProvider {

    private Partner partner;
    private PartnerPoint partnerPoint;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = (savedInstanceState == null) ? getArguments() : savedInstanceState;
        initData(bundle);
    }

    private void initData(Bundle bundle) {
        partner = (Partner) bundle.getSerializable(PartnerDetailsActivity.Key.PARTNER);
        partnerPoint = (PartnerPointImpl) bundle.getSerializable(PartnerDetailsActivity.Key.PARTNER_POINT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        PartnerDetailsViewHelper viewHelper = new PartnerDetailsViewHelper(getActivity(), inflater, container);
        return viewHelper.getViewFilledBy(partner, partnerPoint);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(PartnerDetailsActivity.Key.PARTNER, (Serializable) partner);
        outState.putSerializable(PartnerDetailsActivity.Key.PARTNER_POINT, (Serializable) partnerPoint);
    }

    @Override
    public String getTitle(Context context) {
        return "";
    }

    @Override
    public List<PartnerPoint> getPartnerPoints(Context context) {
        return Arrays.asList(partnerPoint);
    }
}
