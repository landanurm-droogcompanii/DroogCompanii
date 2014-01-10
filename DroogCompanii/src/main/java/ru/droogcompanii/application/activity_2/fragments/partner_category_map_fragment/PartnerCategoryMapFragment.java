package ru.droogcompanii.application.activity_2.fragments.partner_category_map_fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.activity.helpers.ObserverOfViewWillBePlacedOnGlobalLayout;
import ru.droogcompanii.application.activity.partner_info_activity.latlng_bounds_calculator.LatLngBoundsCalculator;
import ru.droogcompanii.application.data.data_structure.Partner;
import ru.droogcompanii.application.data.data_structure.PartnerCategory;
import ru.droogcompanii.application.data.data_structure.PartnerPoint;
import ru.droogcompanii.application.data.db_util.readers_from_database.PartnerPointsReader;
import ru.droogcompanii.application.data.db_util.readers_from_database.PartnersReader;
import ru.droogcompanii.application.util.Keys;
import ru.droogcompanii.application.util.StringsCombiner;

/**
 * Created by ls on 10.01.14.
 */
public class PartnerCategoryMapFragment extends BaseCustomMapFragment implements GoogleMap.OnInfoWindowClickListener {

    public static interface OnPartnerPointInfoWindowClickListener {
        void onPartnerPointInfoWindowClick(PartnerPoint partnerPoint);
    }

    private boolean markersAreDisplayed;
    private List<PartnerPoint> partnerPoints;
    private List<Marker> markers;
    private OnPartnerPointInfoWindowClickListener onPartnerPointInfoWindowClickListener;


    public static PartnerCategoryMapFragment newInstance(PartnerCategory partnerCategory) {
        Bundle args = new Bundle();
        args.putSerializable(Keys.partnerCategory, partnerCategory);
        PartnerCategoryMapFragment fragment = new PartnerCategoryMapFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        onPartnerPointInfoWindowClickListener = getOnPartnerPointInfoWindowClickListener();

        markers = new ArrayList<Marker>();
        getGoogleMap().setOnInfoWindowClickListener(this);

        if (partnerPoints == null) {
            preparePartnerPoints(savedInstanceState);
        }
        showPartnerPoints();

        markersAreDisplayed = true;
    }

    private OnPartnerPointInfoWindowClickListener getOnPartnerPointInfoWindowClickListener() {
        Activity activity = getActivity();
        try {
            return (OnPartnerPointInfoWindowClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(
                activity.toString() + " must implement " + OnPartnerPointInfoWindowClickListener.class.getName()
            );
        }
    }

    private void preparePartnerPoints(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            preparePartnerPoints();
        } else {
            restoreInstanceState(savedInstanceState);
        }
    }

    private void preparePartnerPoints() {
        Bundle args = getArguments();
        PartnerCategory partnerCategory = (PartnerCategory) args.getSerializable(Keys.partnerCategory);
        if (partnerCategory != null) {
            partnerPoints = partnerPointsFrom(partnerCategory);
        } else {
            partnerPoints = withoutPartnerPoints();
        }
    }

    private List<PartnerPoint> withoutPartnerPoints() {
        return new ArrayList<PartnerPoint>();
    }

    private List<PartnerPoint> partnerPointsFrom(PartnerCategory partnerCategory) {
        PartnersReader partnersReader = new PartnersReader(getActivity());
        List<Partner> partners = partnersReader.getPartners(partnerCategory);
        PartnerPointsReader partnerPointsReader = new PartnerPointsReader(getActivity());
        List<PartnerPoint> result = withoutPartnerPoints();
        for (Partner partner : partners) {
            result.addAll(partnerPointsReader.getPartnerPointsOf(partner));
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    private void restoreInstanceState(Bundle savedInstanceState) {
        partnerPoints = (List<PartnerPoint>) savedInstanceState.getSerializable(Keys.partnerPoints);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(Keys.partnerPoints, (Serializable) partnerPoints);
    }

    public void setPartnerCategory(PartnerCategory partnerCategory) {
        partnerPoints = partnerPointsFrom(partnerCategory);
        if (markersAreDisplayed) {
            showPartnerPoints();
        }
    }

    private void showPartnerPoints() {
        markers.clear();
        for (PartnerPoint partnerPoint : partnerPoints) {
            MarkerOptions markerOptions = prepareMarkerOptions(partnerPoint);
            Marker marker = getGoogleMap().addMarker(markerOptions);
            markers.add(marker);
        }
        fitVisibleMarkersOnScreenAfterMapViewWillBePlacedOnLayout();
    }

    private MarkerOptions prepareMarkerOptions(PartnerPoint partnerPoint) {
        LatLng position = new LatLng(partnerPoint.latitude, partnerPoint.longitude);
        return new MarkerOptions()
                .title(partnerPoint.title)
                .position(position)
                .snippet(prepareSnippet(partnerPoint));
    }

    private String prepareSnippet(PartnerPoint partnerPoint) {
        if (partnerPointHasAddress(partnerPoint)) {
            return partnerPoint.address;
        } else if (partnerPointHasPhone(partnerPoint)) {
            return StringsCombiner.combine(partnerPoint.phones, ", ");
        } else {
            return "";
        }
    }

    private boolean partnerPointHasAddress(PartnerPoint partnerPoint) {
        String address = partnerPoint.address.trim();
        return !address.isEmpty();
    }

    private boolean partnerPointHasPhone(PartnerPoint partnerPoint) {
        int numberOfPhones = partnerPoint.phones.size();
        return numberOfPhones > 0;
    }

    private void fitVisibleMarkersOnScreenAfterMapViewWillBePlacedOnLayout() {
        ObserverOfViewWillBePlacedOnGlobalLayout.runAfterViewWillBePlacedOnLayout(getMapView(), new Runnable() {
            @Override
            public void run() {
                fitVisibleMarkersOnScreen();
            }
        });
    }

    private View getMapView() {
        return getSupportMapFragment().getView();
    }

    private void fitVisibleMarkersOnScreen() {
        if (noVisibleMarkers()) {
            return;
        }
        LatLngBounds bounds = LatLngBoundsCalculator.calculateBoundsOfVisibleMarkers(markers);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, getMapPadding());
        getGoogleMap().moveCamera(cameraUpdate);
    }

    private boolean noVisibleMarkers() {
        for (Marker each : markers) {
            if (each.isVisible()) {
                return false;
            }
        }
        return true;
    }

    private int getMapPadding() {
        return getResources().getInteger(R.integer.map_markers_padding);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        PartnerPoint partnerPoint = partnerPointOf(marker);
        onPartnerPointInfoWindowClickListener.onPartnerPointInfoWindowClick(partnerPoint);
    }

    private PartnerPoint partnerPointOf(Marker marker) {
        int index = markers.indexOf(marker);
        return partnerPoints.get(index);
    }
}
