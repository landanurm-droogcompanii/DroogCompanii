package ru.droogcompanii.application.view.fragment.partner_points_map_fragment;

import android.content.Context;

import java.util.List;

import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;

/**
 * Created by ls on 14.01.14.
 */
public interface PartnerPointsProvider {
    List<PartnerPoint> getPartnerPoints(Context context);
}
