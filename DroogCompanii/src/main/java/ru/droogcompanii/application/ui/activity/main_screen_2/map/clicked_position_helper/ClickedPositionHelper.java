package ru.droogcompanii.application.ui.activity.main_screen_2.map.clicked_position_helper;

import android.os.Bundle;

import com.google.android.gms.maps.model.LatLng;
import com.google.common.base.Optional;

import ru.droogcompanii.application.ui.fragment.partner_points_map.CustomMapFragment;

/**
 * Created by ls on 17.03.14.
 */
public class ClickedPositionHelper {

    private static final String KEY_CENTER = "KEY_CENTER" + ClickedPositionHelper.class.getName();

    private final ClickedPositionDrawer clickedPositionDrawer;
    private Optional<LatLng> center;

    public ClickedPositionHelper(CustomMapFragment mapFragment) {
        clickedPositionDrawer = new CustomIconDrawer(mapFragment);
        center = Optional.absent();
    }

    public void restoreFrom(Bundle savedInstanceState) {
        Optional<LatLng> restoredCenter = Optional.fromNullable((LatLng) savedInstanceState.getParcelable(KEY_CENTER));
        if (restoredCenter.isPresent()) {
            set(restoredCenter.get());
        }
    }

    public void saveInto(Bundle outState) {
        outState.putParcelable(KEY_CENTER, center.orNull());
    }

    public void set(LatLng newCenter) {
        remove();
        clickedPositionDrawer.draw(newCenter);
        center = Optional.of(newCenter);
    }

    public void remove() {
        if (center.isPresent()) {
            clickedPositionDrawer.remove();
            center = Optional.absent();
        }
    }

    public Optional<LatLng> getOptionalClickedPosition() {
        return center;
    }

    public LatLng getClickedPosition() {
        return center.get();
    }

    public boolean isClickedPositionPresent() {
        return center.isPresent();
    }

    public void updateOnCameraChange() {
        clickedPositionDrawer.updateOnCameraChange();
    }
}
