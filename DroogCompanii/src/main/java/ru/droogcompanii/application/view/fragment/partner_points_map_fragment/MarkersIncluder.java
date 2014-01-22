package ru.droogcompanii.application.view.fragment.partner_points_map_fragment;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Set;

import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.data.searchable_sortable_listing.SearchableListing;
import ru.droogcompanii.application.util.MultiMap;

/**
 * Created by ls on 22.01.14.
 */
public class MarkersIncluder {
    private final MarkerIconsProvider markerIconsProvider;
    private final PositionsAndPartnerPoints positionsAndPartnerPoints;

    public MarkersIncluder(SearchableListing<PartnerPoint> searchableListing) {
        positionsAndPartnerPoints = new PositionsAndPartnerPoints(searchableListing);
        markerIconsProvider = new MarkerIconsProvider();
    }

    public MultiMap<Marker, PartnerPoint> includeIn(GoogleMap googleMap) {
        MultiMap<Marker, PartnerPoint> result = new MultiMap<Marker, PartnerPoint>();
        Set<LatLng> positions = positionsAndPartnerPoints.getAllPositions();
        for (LatLng position : positions) {
            Set<PartnerPoint> partnerPoints = positionsAndPartnerPoints.get(position);
            Marker marker = includeIn(googleMap, position, partnerPoints);
            result.putAll(marker, partnerPoints);
        }
        return result;
    }

    private Marker includeIn(GoogleMap googleMap, LatLng position, Set<PartnerPoint> partnerPoints) {
        boolean singlePoint = partnerPoints.size() == 1;
        BitmapDescriptor icon = markerIconsProvider.prepareIcon(singlePoint);
        MarkerOptions markerOptions = new MarkerOptions().position(position).icon(icon);
        return googleMap.addMarker(markerOptions);
    }

    private static class MarkerIconsProvider {
        public BitmapDescriptor prepareIcon(boolean singlePoint) {
            return BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE);
        }
    }

}
