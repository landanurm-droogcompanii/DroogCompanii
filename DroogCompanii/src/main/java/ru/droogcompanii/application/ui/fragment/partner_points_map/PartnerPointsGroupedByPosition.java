package ru.droogcompanii.application.ui.fragment.partner_points_map;

import com.google.android.gms.maps.model.LatLng;

import java.util.Collection;
import java.util.Set;

import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.data.searchable_sortable_listing.SearchableListing;
import ru.droogcompanii.application.util.MultiMap;

/**
 * Created by ls on 22.01.14.
 */
class PartnerPointsGroupedByPosition {

    private final MultiMap<LatLng, PartnerPoint> groups;

    public PartnerPointsGroupedByPosition(SearchableListing<PartnerPoint> searchableListing) {
        this(searchableListing.toList());
    }

    public PartnerPointsGroupedByPosition(Collection<PartnerPoint> partnerPoints) {
        groups = new MultiMap<LatLng, PartnerPoint>();
        putAll(partnerPoints);
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

    public Set<LatLng> getAllPositions() {
        return groups.keySet();
    }

    public static interface OnEachPositionHandler {
        void onEach(LatLng position, Set<PartnerPoint> partnerPoints);
    }

    public void forEach(final OnEachPositionHandler onEachPositionHandler) {
        groups.forEach(new MultiMap.OnEachHandler<LatLng, PartnerPoint>() {
            @Override
            public void onEach(MultiMap.Entry<LatLng, PartnerPoint> entry) {
                onEachPositionHandler.onEach(entry.getKey(), entry.getValues());
            }
        });
    }

}
