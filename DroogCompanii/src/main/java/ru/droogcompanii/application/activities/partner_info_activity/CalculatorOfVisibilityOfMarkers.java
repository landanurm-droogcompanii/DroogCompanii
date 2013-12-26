package ru.droogcompanii.application.activities.partner_info_activity;

import com.google.android.gms.maps.model.Marker;

import java.util.List;

import ru.droogcompanii.application.activities.partner_info_activity.comparer_text_with_query.ComparerPartnerPointWithQuery;
import ru.droogcompanii.application.activities.partner_info_activity.comparer_text_with_query.ComparerPartnerPointWithQueryProvider;
import ru.droogcompanii.application.data.data_structure.PartnerPoint;

/**
 * Created by ls on 25.12.13.
 */
class CalculatorOfVisibilityOfMarkers {
    private final ComparerPartnerPointWithQuery comparerPartnerPointWithQuery;
    private final List<Marker> markers;
    private final List<PartnerPoint> partnerPoints;

    public CalculatorOfVisibilityOfMarkers(List<Marker> markers, List<PartnerPoint> partnerPoints) {
        this.comparerPartnerPointWithQuery = ComparerPartnerPointWithQueryProvider.get();
        this.markers = markers;
        this.partnerPoints = partnerPoints;
    }

    public VisibilityOfMarkers calculate(String query) {
        int numberOfMarkers = markers.size();
        VisibilityOfMarkers visibilityOfMarkers = new VisibilityOfMarkers(numberOfMarkers);
        for (int i = 0; i < numberOfMarkers; ++i) {
            Marker marker = markers.get(i);
            PartnerPoint partnerPoint = partnerPoints.get(i);
            boolean visible = comparerPartnerPointWithQuery.partnerPointMatchQuery(partnerPoint, query);
            visibilityOfMarkers.add(marker, visible);
        }
        return visibilityOfMarkers;
    }

}
