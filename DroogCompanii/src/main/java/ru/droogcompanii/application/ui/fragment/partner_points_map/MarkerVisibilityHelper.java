package ru.droogcompanii.application.ui.fragment.partner_points_map;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Collection;

/**
 * Created by ls on 12.03.14.
 */
public class MarkerVisibilityHelper {

    private final GoogleMap googleMap;
    private LatLngBounds bounds;

    public MarkerVisibilityHelper(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }

    private LatLngBounds getBounds() {
        if (bounds == null) {
            bounds = googleMap.getProjection().getVisibleRegion().latLngBounds;
        }
        return bounds;
    }

    public void updateVisibility(Collection<Marker> markers) {
        LatLngBounds bounds = getBounds();
        for (Marker marker : markers) {
            boolean visible = bounds.contains(marker.getPosition());
            marker.setVisible(visible);
        }
        reset();
    }

    public void reset() {
        bounds = null;
    }

    public boolean isMustBeVisible(MarkerOptions markerOptions) {
        return isMustBeVisible(markerOptions.getPosition());
    }

    public boolean isMustBeVisible(LatLng position) {
        return getBounds().contains(position);
    }
}

