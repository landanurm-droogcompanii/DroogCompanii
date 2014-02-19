package ru.droogcompanii.application.ui.activity.main_screen;

import android.content.Context;

import java.util.List;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.db_util.hierarchy_of_partners.PartnerPointsReader;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.ui.fragment.partner_points_map.PartnerPointsProvider;

/**
 * Created by ls on 29.01.14.
 */
class AllPartnerPointsProvider implements PartnerPointsProvider {

    @Override
    public String getTitle(Context context) {
        return context.getString(R.string.titleOfClosestPartnerPoints);
    }

    @Override
    public List<PartnerPoint> getPartnerPoints(Context context) {
        PartnerPointsReader reader = new PartnerPointsReader(context);
        return reader.getAllPartnerPoints();
    }
}
