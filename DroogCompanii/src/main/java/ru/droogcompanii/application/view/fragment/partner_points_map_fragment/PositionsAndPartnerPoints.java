package ru.droogcompanii.application.view.fragment.partner_points_map_fragment;

import com.google.android.gms.maps.model.LatLng;

import java.util.Collection;
import java.util.Set;

import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.data.searchable_sortable_listing.SearchableListing;
import ru.droogcompanii.application.util.MultiMap;

/**
 * Created by ls on 22.01.14.
 */
public class PositionsAndPartnerPoints {
    private final MultiMap<LatLng, PartnerPoint> multiMap;

    public PositionsAndPartnerPoints(SearchableListing<PartnerPoint> searchableListing) {
        this(searchableListing.toList());
    }

    public PositionsAndPartnerPoints(Collection<PartnerPoint> partnerPoints) {
        multiMap = new MultiMap<LatLng, PartnerPoint>();
        putAll(partnerPoints);
    }

    public PositionsAndPartnerPoints() {
        multiMap = new MultiMap<LatLng, PartnerPoint>();
    }

    public void putAll(LatLng position, Collection<PartnerPoint> partnerPoints) {
        multiMap.putAll(position, partnerPoints);
    }

    public void putAll(Collection<PartnerPoint> partnerPoints) {
        for (PartnerPoint partnerPoint : partnerPoints) {
            put(partnerPoint);
        }
    }

    public void put(PartnerPoint partnerPoint) {
        LatLng position = new LatLng(partnerPoint.latitude, partnerPoint.longitude);
        multiMap.put(position, partnerPoint);
    }

    public Set<PartnerPoint> get(LatLng position) {
        return multiMap.get(position);
    }

    public Set<LatLng> getAllPositions() {
        return multiMap.keySet();
    }

}
