package ru.droogcompanii.application.view.fragment.partner_points_map_fragment;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Set;

import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.data.searchable_sortable_listing.SearchableListing;

/**
 * Created by ls on 22.01.14.
 */


/*
    Необходимо использовать этот класс для вставки маркеров на карту
    и получения соответствующих пар "позиция - партнерские точки".
 */
public class MarkersIncluder {
    private final PositionsAndPartnerPoints grouped;

    public MarkersIncluder(SearchableListing<PartnerPoint> searchableListing) {
        grouped = new PositionsAndPartnerPoints(searchableListing);
    }

    public PositionsAndPartnerPoints includeIn(PartnerPointsMapFragment fragment) {
        PositionsAndPartnerPoints result = new PositionsAndPartnerPoints();
        for (LatLng position : grouped.getAllPositions()) {
            Marker marker = fragment.addMarker(prepareMarkerOptions(position));
            Set<PartnerPoint> partnerPoints = grouped.get(position);
            result.putAll(marker.getPosition(), partnerPoints);
        }
        return result;
    }

    private MarkerOptions prepareMarkerOptions(LatLng position) {
        return new MarkerOptions().position(position).icon(MarkerIcons.getUnselectedIcon());
    }

}
