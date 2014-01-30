package ru.droogcompanii.application.ui.activity_3.main_screen;

import android.content.Context;
import android.location.Location;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ru.droogcompanii.application.DroogCompaniiSettings;
import ru.droogcompanii.application.data.db_util.readers_from_database.PartnerPointsReader;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.ui.fragment.partner_points_map_fragment.PartnerPointsProvider;

/**
 * Created by ls on 29.01.14.
 */
class ClosePartnerPointsProvider implements PartnerPointsProvider {
    private static final float MAX_DISTANCE_IN_METERS = 8000.0f;

    private final Location baseLocation;

    public ClosePartnerPointsProvider() {
        baseLocation = DroogCompaniiSettings.getBaseLocation();
    }

    @Override
    public List<PartnerPoint> getPartnerPoints(Context context) {
        PartnerPointsReader reader = new PartnerPointsReader(context);
        Set<PartnerPoint> closePartnerPoints = new HashSet<PartnerPoint>();
        for (PartnerPoint partnerPoint : reader.getAllPartnerPoints()) {
            if (isClosePartnerPoint(baseLocation, partnerPoint)) {
                closePartnerPoints.add(partnerPoint);
            }
        }
        return new ArrayList<PartnerPoint>(closePartnerPoints);
    }

    private boolean isClosePartnerPoint(Location baseLocation, PartnerPoint partnerPoint) {
        if (baseLocation == null) {
            return true;
        }
        return partnerPoint.distanceTo(baseLocation) <= MAX_DISTANCE_IN_METERS;
    }
}
