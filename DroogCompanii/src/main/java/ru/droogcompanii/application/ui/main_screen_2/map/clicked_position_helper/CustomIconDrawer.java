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
        MarkerOptions markerOptions = new MarkerOptions().position(center).icon(prepareBitmapDescriptor());
        Marker includedMarker = mapFragment.getGoogleMap().addMarker(markerOptions);
        return includedMarker;
    }

    private BitmapDescriptor prepareBitmapDescriptor() {
        return BitmapDescriptorFactory.fromResource(R.drawable.clicked_circle);
        //return BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE);
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
