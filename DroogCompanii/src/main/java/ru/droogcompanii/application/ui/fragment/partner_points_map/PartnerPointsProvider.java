package ru.droogcompanii.application.ui.fragment.partner_points_map;

import android.content.Context;

import java.util.List;

import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;

/**
 * Created by ls on 14.01.14.
 */
public interface PartnerPointsProvider {
    String getTitle(Context context);
    List<PartnerPoint> getPartnerPoints(Context context);
}