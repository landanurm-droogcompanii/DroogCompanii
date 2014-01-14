package ru.droogcompanii.application.view.activity.partner_info_activity;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.view.helpers.ActionBarActivityWithBackButton;
import ru.droogcompanii.application.view.activity.partner_info_activity.latlng_bounds_calculator.LatLngBoundsCalculator;
import ru.droogcompanii.application.view.activity.partner_point_info_activity.PartnerPointInfoActivity;
import ru.droogcompanii.application.data.data_structure.Partner;
import ru.droogcompanii.application.data.data_structure.PartnerPoint;
import ru.droogcompanii.application.data.db_util.readers_from_database.PartnerPointsReader;
import ru.droogcompanii.application.util.Keys;
import ru.droogcompanii.application.util.StringsCombiner;
import ru.droogcompanii.application.view.helpers.ObserverOfViewWillBePlacedOnGlobalLayout;


public class PartnerInfoActivity extends ActionBarActivityWithBackButton
                              implements SearchPartnerPointsTask.OnTaskIsDoneListener,
                                         GoogleMap.OnInfoWindowClickListener {

    private GoogleMap googleMap;
    private List<PartnerPoint> partnerPoints;
    private List<Marker> markers;
    private Partner partner;
    private SearchPartnerPointsTask searchTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner_info);

        markers = new ArrayList<Marker>();
        searchTask = createSearchPartnerPointsTask();
        googleMap = getMap();
        googleMap.setOnInfoWindowClickListener(this);

        preparePartnerData(savedInstanceState);
        showPartnerData();

        handleIntent(getIntent());
    }

    private SearchPartnerPointsTask createSearchPartnerPointsTask() {
        return new SearchPartnerPointsTask(markers, partnerPoints, this);
    }

    private GoogleMap getMap() {
        return getSupportMapFragment().getMap();
    }

    private SupportMapFragment getSupportMapFragment() {
        return (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
    }

    private void preparePartnerData(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            preparePartnerData();
        } else {
            restorePartnerData(savedInstanceState);
        }
    }

    private void preparePartnerData() {
        Intent intent = getIntent();
        partner = (Partner) intent.getSerializableExtra(Keys.partner);
        PartnerPointsReader partnerPointsReader = new PartnerPointsReader(this);
        partnerPoints = partnerPointsReader.getPartnerPointsOf(partner);
    }

    @SuppressWarnings("unchecked")
    private void restorePartnerData(Bundle savedInstanceState) {
        partner = (Partner) savedInstanceState.getSerializable(Keys.partner);
        partnerPoints = (List<PartnerPoint>) savedInstanceState.getSerializable(Keys.partnerPoints);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(Keys.partner, partner);
        outState.putSerializable(Keys.partnerPoints, (Serializable) partnerPoints);
    }

    private void showPartnerData() {
        setTitle(partner.fullTitle);
        initMarkers();
    }

    private void initMarkers() {
        markers.clear();
        for (PartnerPoint partnerPoint : partnerPoints) {
            Marker marker = googleMap.addMarker(prepareMarkerOptions(partnerPoint));
            markers.add(marker);
        }
        fitVisibleMarkersOnScreenAfterMapViewWillBePlacedOnLayout();
    }

    private MarkerOptions prepareMarkerOptions(PartnerPoint partnerPoint) {
        String title = partnerPoint.title;
        LatLng position = new LatLng(partnerPoint.latitude, partnerPoint.longitude);
        MarkerOptions markerOptions = new MarkerOptions().title(title).position(position);
        includeSnippet(markerOptions, partnerPoint);
        return markerOptions;
    }

    private void includeSnippet(MarkerOptions markerOptions, PartnerPoint partnerPoint) {
        if (partnerPointHasAddress(partnerPoint)) {
            markerOptions.snippet(partnerPoint.address);
        } else if (partnerPointHasPhone(partnerPoint)) {
            String phones = StringsCombiner.combine(partnerPoint.phones, ", ");
            markerOptions.snippet(phones);
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
        googleMap.moveCamera(cameraUpdate);
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
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            processSearchQuery(query);
        }
    }

    private void processSearchQuery(String query) {
        cancelSearchTaskIfItIsRunning();
        searchTask.execute(query);
    }

    private void cancelSearchTaskIfItIsRunning() {
        if ((searchTask != null) && (searchTask.getStatus() == AsyncTask.Status.RUNNING)) {
            searchTask.cancel(true);
        }
        searchTask = createSearchPartnerPointsTask();
    }

    @Override
    public void onTaskIsDone() {
        cancelSearchTaskIfItIsRunning();
        fitVisibleMarkersOnScreen();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);

        MenuItem searchMenuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchableInfo searchableInfo = searchManager.getSearchableInfo(getComponentName());
        searchView.setSearchableInfo(searchableInfo);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.search) {
            onSearchRequested();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        PartnerPoint partnerPoint = getPartnerPointFromMarker(marker);
        showPartnerPointInfo(partner, partnerPoint);
    }

    public PartnerPoint getPartnerPointFromMarker(Marker marker) {
        int index = markers.indexOf(marker);
        return partnerPoints.get(index);
    }

    private void showPartnerPointInfo(Partner partner, PartnerPoint partnerPoint) {
        Intent intent = new Intent(this, PartnerPointInfoActivity.class);
        intent.putExtra(Keys.partnerPoint, partnerPoint);
        intent.putExtra(Keys.partner, partner);
        startActivity(intent);
    }
}
