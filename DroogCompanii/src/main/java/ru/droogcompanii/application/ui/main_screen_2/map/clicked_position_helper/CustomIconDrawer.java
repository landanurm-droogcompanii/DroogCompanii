package ru.droogcompanii.application.ui.main_screen_2.map.clicked_position_helper;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.common.base.Optional;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.ui.fragment.partner_points_map.CustomMapFragment;

/**
 * Created by ls on 25.03.14.
 */
public class CustomIconDrawer implements ClickedPositionDrawer {

    private static final int ICON_ID = R.drawable.clicked_marker_icon;

    private static class Anchor {
        public final float U;
        public final float V;

        public Anchor(float u, float v) {
            this.U = u;
            this.V = v;
        }
    }

    private static final Anchor ANCHOR_DEFAULT = new Anchor(0.5f, 1.0f);
    private static final Anchor ANCHOR_CENTER = new Anchor(0.5f, 0.5f);

    private static final Anchor ACTUAL_ANCHOR = ANCHOR_CENTER;


    private final CustomMapFragment mapFragment;
    private Optional<Marker> marker;

    public CustomIconDrawer(CustomMapFragment mapFragment) {
        this.mapFragment = mapFragment;
        this.marker = Optional.absent();
    }

    @Override
    public void draw(LatLng center) {
        marker = Optional.of(includeMarker(center));
    }

    private Marker includeMarker(LatLng center) {
        MarkerOptions markerOptions = new MarkerOptions()
                .position(center)
                .icon(prepareBitmapDescriptor())
                .anchor(ACTUAL_ANCHOR.U, ACTUAL_ANCHOR.V);
        Marker includedMarker = mapFragment.getGoogleMap().addMarker(markerOptions);
        return includedMarker;
    }

    private BitmapDescriptor prepareBitmapDescriptor() {
        return BitmapDescriptorFactory.fromResource(ICON_ID);
    }

    @Override
    public void remove() {
        marker.get().remove();
        marker = Optional.absent();
    }

    @Override
    public void updateOnCameraChange() {
        // do nothing
    }
}
