package ru.droogcompanii.application.activity.partner_info_activity;

import com.google.android.gms.maps.model.Marker;

import java.util.List;

import ru.droogcompanii.application.activity.partner_info_activity.comparer_with_query.ComparerWithQuery;
import ru.droogcompanii.application.activity.partner_info_activity.comparer_with_query.ComparerWithQueryProvider;
import ru.droogcompanii.application.data.data_structure.PartnerPoint;

/**
 * Created by ls on 25.12.13.
 */
class CalculatorOfVisibilityOfMarkers {
    private final ComparerWithQuery comparerWithQuery;
    private final List<Marker> markers;
    private final List<PartnerPoint> partnerPoints;

    public CalculatorOfVisibilityOfMarkers(List<Marker> markers, List<PartnerPoint> partnerPoints) {
        this.comparerWithQuery = ComparerWithQueryProvider.get();
        this.markers = markers;
        this.partnerPoints = partnerPoints;
    }

    public VisibilityOfMarkers calculate(String query) {
        int numberOfMarkers = markers.size();
        VisibilityOfMarkers visibilityOfMarkers = new VisibilityOfMarkers(numberOfMarkers);
        for (int i = 0; i < numberOfMarkers; ++i) {
            Marker marker = markers.get(i);
            PartnerPoint partnerPoint = partnerPoints.get(i);
            boolean markerIsVisible = comparerWithQuery.partnerPointMatchQuery(partnerPoint, query);
            visibilityOfMarkers.add(marker, markerIsVisible);
        }
        return visibilityOfMarkers;
    }

}
