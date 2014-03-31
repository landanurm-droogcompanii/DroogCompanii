package ru.droogcompanii.application.ui.main_screen_2.map;

import com.google.android.gms.maps.model.LatLng;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.util.MultiMap;

/**
 * Created by ls on 22.01.14.
 */
public class PartnerPointsGroupedByPosition {

    private final MultiMap<LatLng, PartnerPoint> groups;

    public PartnerPointsGroupedByPosition(Collection<PartnerPoint> partnerPoints) {
        groups = new MultiMap<LatLng, PartnerPoint>();
        putAll(partnerPoints);
    }

    public PartnerPointsGroupedByPosition() {
        this(Collections.EMPTY_LIST);
    }

    public void putAll(Collection<PartnerPoint> partnerPoints) {
        for (PartnerPoint partnerPoint : partnerPoints) {
            put(partnerPoint);
        }
    }

    public void put(PartnerPoint partnerPoint) {
        groups.put(partnerPoint.getPosition(), partnerPoint);
    }

    public Set<PartnerPoint> get(LatLng position) {
        return groups.get(position);
    }

    public boolean isContainPosition(LatLng position) {
        return groups.isContainKey(position);
    }

}
