package ru.droogcompanii.application.activities.partner_info_activity;

import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Leonid on 23.12.13.
 */
class VisibilityOfMarkers {

    private static class VisibilityOfMarker {
        public final Marker marker;
        public final Boolean visible;

        public VisibilityOfMarker(Marker marker, Boolean visible) {
            this.marker = marker;
            this.visible = visible;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof VisibilityOfMarker)) {
                return false;
            }
            VisibilityOfMarker other = (VisibilityOfMarker) obj;
            return marker.equals(other.marker) && visible.equals(other.visible);
        }

        @Override
        public int hashCode() {
            return marker.hashCode() + visible.hashCode();
        }
    }

    private final Collection<VisibilityOfMarker> visibilityOfMarkers;

    public VisibilityOfMarkers() {
        visibilityOfMarkers = new ArrayList<VisibilityOfMarker>();
    }

    public VisibilityOfMarkers(int capacity) {
        visibilityOfMarkers = new ArrayList<VisibilityOfMarker>(capacity);
    }

    public void add(Marker marker, Boolean visible) {
        VisibilityOfMarker visibilityOfMarker = new VisibilityOfMarker(marker, visible);
        visibilityOfMarkers.add(visibilityOfMarker);
    }

    public void update() {
        for (VisibilityOfMarker each : visibilityOfMarkers) {
            each.marker.setVisible(each.visible);
        }
    }
}