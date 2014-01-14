package ru.droogcompanii.application.view.activity_3.search_activity;

import android.content.Context;

import java.util.List;

import ru.droogcompanii.application.data.data_structure.Partner;
import ru.droogcompanii.application.data.data_structure.PartnerPoint;
import ru.droogcompanii.application.view.fragment.partner_points_map_fragment.PartnerPointsProvider;

/**
 * Created by ls on 14.01.14.
 */
public interface SearchResultProvider {
    List<Partner> getPartners(Context context);
    List<PartnerPoint> getPartnerPoints(Context context, Partner partner);
}
