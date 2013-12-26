package ru.droogcompanii.application.activities.partner_info_activity;

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
import java.util.HashMap;
import java.util.List;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.activities.helpers.ActionBarActivityWithBackButton;
import ru.droogcompanii.application.activities.partner_info_activity.latlng_bounds_calculator.LatLngBoundsCalculator;
import ru.droogcompanii.application.activities.partner_point_info_activity.PartnerPointInfoActivity;
import ru.droogcompanii.application.data.data_structure.Partner;
import ru.droogcompanii.application.data.data_structure.PartnerPoint;
import ru.droogcompanii.application.data.db_util.readers_from_database.PartnerPointsReader;
import ru.droogcompanii.application.util.Keys;
import ru.droogcompanii.application.util.ObserverOfViewWillBePlacedOnGlobalLayout;


public class PartnerInfoActivity extends ActionBarActivityWithBackButton
                              implements SearchPartnerPointsTask.OnTaskIsDoneListener,
                                         GoogleMap.OnInfoWindowClickListener {

    private GoogleMap googleMap;
    private List<PartnerPoint> partnerPoints;
    private java.util.Map<Marker, PartnerPoint> markersAndPartnerPoints;
    private Partner partner;
    private SearchPartnerPointsTask searchTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner_info);

        markersAndPartnerPoints = new HashMap<Marker, PartnerPoint>();
        searchTask = createSearchPartnerPointsTask();
        googleMap = getMap();
        googleMap.setOnInfoWindowClickListener(this);

        preparePartnerData(savedInstanceState);
        showPartnerData();

        handleIntent(getIntent());
    }

    private SearchPartnerPointsTask createSearchPartnerPointsTask() {
        return new SearchPartnerPointsTask(markersAndPartnerPoints, this);
    }

    private void preparePartnerData(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            preparePartnerData(getIntent());
        } else {
            restorePartnerData(savedInstanceState);
        }
    }

    private void preparePartnerData(Intent intent) {
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

    private GoogleMap getMap() {
        return getSupportMapFragment().getMap();
    }

    private SupportMapFragment getSupportMapFragment() {
        return (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
    }

    private void showPartnerData() {
        setTitle(partner.fullTitle);
        initMarkers();
    }

    private void initMarkers() {
        markersAndPartnerPoints.clear();
        for (PartnerPoint partnerPoint : partnerPoints) {
            Marker marker = googleMap.addMarker(prepareMarkerOptions(partnerPoint));
            markersAndPartnerPoints.put(marker, partnerPoint);
        }
        fitAllPartnerPointsOnScreenAfterMapViewIsPlacedOnLayout();
    }

    private MarkerOptions prepareMarkerOptions(PartnerPoint partnerPoint) {
        LatLng position = new LatLng(partnerPoint.latitude, partnerPoint.longitude);
        return new MarkerOptions()
                       .title(partnerPoint.title)
                       .snippet(prepareSnippet(partnerPoint))
                       .position(position);
    }

    private String prepareSnippet(PartnerPoint partnerPoint) {
        return partnerPoint.address;
    }

    private void fitAllPartnerPointsOnScreenAfterMapViewIsPlacedOnLayout() {
        ObserverOfViewWillBePlacedOnGlobalLayout.runAfterViewWillBePlacedOnLayout(getMapView(), new Runnable() {
            @Override
            public void run() {
                fitAllPartnerPointsOnScreen();
            }
        });
    }

    private View getMapView() {
        return getSupportMapFragment().getView();
    }

    private void fitAllPartnerPointsOnScreen() {
        if (noPartnerPoints()) {
            return;
        }
        LatLngBounds bounds = LatLngBoundsCalculator.calculate(partnerPoints);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, getMapPadding());
        googleMap.moveCamera(cameraUpdate);
    }

    private boolean noPartnerPoints() {
        return partnerPoints.isEmpty();
    }

    private int getMapPadding() {
        return getResources().getInteger(R.integer.map_markers_padding);
    }

    public PartnerPoint getPartnerPointFromMarker(Marker marker) {
        return markersAndPartnerPoints.get(marker);
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
        Intent intent = new Intent(this, PartnerPointInfoActivity.class);
        intent.putExtra(Keys.partnerPoint, partnerPoint);
        intent.putExtra(Keys.partner, partner);
        startActivity(intent);
    }
}
