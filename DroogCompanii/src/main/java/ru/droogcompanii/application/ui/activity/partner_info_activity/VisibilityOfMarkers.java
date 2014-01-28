package ru.droogcompanii.application.ui.activity.partner_info_activity;

import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Leonid on 23.12.13.
 */
class VisibilityOfMarkers {

    private static class VisibilityOfMarker {
        final Marker marker;
        final boolean visible;

        VisibilityOfMarker(Marker marker, boolean visible) {
            this.marker = marker;
            this.visible = visible;
        }

        void update() {
            marker.setVisible(visible);
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
            return marker.equals(other.marker) && (visible == other.visible);
        }

        @Override
        public int hashCode() {
            return marker.hashCode() + ((Boolean) visible).hashCode();
        }
    }

    private final Collection<VisibilityOfMarker> visibilityOfMarkers;

    public VisibilityOfMarkers() {
        visibilityOfMarkers = new ArrayList<VisibilityOfMarker>();
    }

    public VisibilityOfMarkers(int capacity) {
        visibilityOfMarkers = new ArrayList<VisibilityOfMarker>(capacity);
    }

    public void add(Marker marker, boolean visible) {
        VisibilityOfMarker visibilityOfMarker = new VisibilityOfMarker(marker, visible);
        visibilityOfMarkers.add(visibilityOfMarker);
    }

    public void update() {
        for (VisibilityOfMarker each : visibilityOfMarkers) {
            each.update();
        }
    }
}