package ru.droogcompanii.application.activities.partner_info_activity;

import com.google.android.gms.maps.model.Marker;

import java.util.Map;

import ru.droogcompanii.application.activities.partner_info_activity.comparer_text_with_query.ComparerTextWithQuery;
import ru.droogcompanii.application.activities.partner_info_activity.comparer_text_with_query.ComparerTextWithQueryProvider;
import ru.droogcompanii.application.data.data_structure.PartnerPoint;

/**
 * Created by ls on 25.12.13.
 */
class CalculatorOfVisibilityOfMarkers {
    private final ComparerTextWithQuery comparerTextWithQuery;
    private final Map<Marker, PartnerPoint> markersAndPartnerPoints;

    public CalculatorOfVisibilityOfMarkers(Map<Marker, PartnerPoint> markersAndPartnerPoints) {
        this.comparerTextWithQuery = ComparerTextWithQueryProvider.get();
        this.markersAndPartnerPoints = markersAndPartnerPoints;
    }

    public VisibilityOfMarkers calculate(String query) {
        int numberOfMarkers = markersAndPartnerPoints.size();
        VisibilityOfMarkers visibilityOfMarkers = new VisibilityOfMarkers(numberOfMarkers);
        for (Map.Entry<Marker, PartnerPoint> each : markersAndPartnerPoints.entrySet()) {
            Marker marker = each.getKey();
            PartnerPoint partnerPoint = each.getValue();
            boolean visible = partnerPointMatchQuery(partnerPoint, query);
            visibilityOfMarkers.add(marker, visible);
        }
        return visibilityOfMarkers;
    }

    private boolean partnerPointMatchQuery(PartnerPoint partnerPoint, String query) {
        return comparerTextWithQuery.textMatchQuery(partnerPoint.title, query);
    }
}
