package ru.droogcompanii.application.ui.fragment.partner_points_map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.ui.util.able_to_start_task.FragmentAbleToStartTask;

/**
 * Created by ls on 14.03.14.
 */
public class CustomMapFragment extends FragmentAbleToStartTask implements GoogleMapProvider {

    private static final Runnable DUMMY_RUNNABLE_ON_RESUME = new Runnable() {
        @Override
        public void run() {
            // do nothing
        }
    };

    private Runnable runnableOnResume;

    private Collection<Marker> markers;
    private GoogleMap map;

    public CustomMapFragment() {
        map = null;
        markers = new ArrayList<Marker>();
        runnableOnResume = DUMMY_RUNNABLE_ON_RESUME;
    }

    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_custom_map, container, false);
    }

    public final void callOnceOnResume(Runnable runnable) {
        runnableOnResume = runnable;
    }

    @Override
    public void onResume() {
        super.onResume();
        runnableOnResume.run();
        runnableOnResume = DUMMY_RUNNABLE_ON_RESUME;
    }

    @Override
    public final GoogleMap getGoogleMap() {
        if (isNeedToInitMap()) {
            initMap();
        }
        return map;
    }

    private boolean isNeedToInitMap() {
        return (map == null) && (getActivity() != null) &&
                (getActivity().getSupportFragmentManager() != null);
    }

    public final LatLngBounds getBounds() {
        return getGoogleMap().getProjection().getVisibleRegion().latLngBounds;
    }

    private void initMap() {
        SupportMapFragment mapFragment = getNestedSupportMapFragment();
        if (mapFragment != null) {
            map = mapFragment.getMap();
        }
    }

    private SupportMapFragment getNestedSupportMapFragment() {
        return (SupportMapFragment)
                getActivity().getSupportFragmentManager().findFragmentById(R.id.mapView);
    }

    public final View getMapView() {
        return getNestedSupportMapFragment().getView();
    }

    public final float getCurrentZoom() {
        return getGoogleMap().getCameraPosition().zoom;
    }

    public final LatLng getCurrentCenter() {
        return getGoogleMap().getCameraPosition().target;
    }

    public final void moveCamera(LatLng center, float zoom) {
        getGoogleMap().moveCamera(CameraUpdateFactory.newLatLngZoom(center, zoom));
    }

    public final void animateCamera(LatLng center, float zoom) {
        getGoogleMap().animateCamera(CameraUpdateFactory.newLatLngZoom(center, zoom));
    }

    @Override
    public void onTaskResult(int requestCode, int resultCode, Serializable result) {
        // do nothing
    }



    public final Marker addMarker(MarkerOptions markerOptions) {
        Marker marker = getGoogleMap().addMarker(markerOptions);
        markers.add(marker);
        return marker;
    }

    protected final void removeAllMarkers() {
        getGoogleMap().clear();
        markers.clear();
    }

    public final Collection<Marker> getMarkers() {
        return new ArrayList<Marker>(markers);
    }

}
