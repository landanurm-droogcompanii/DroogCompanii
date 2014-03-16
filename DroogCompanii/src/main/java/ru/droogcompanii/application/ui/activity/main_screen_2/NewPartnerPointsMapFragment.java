package ru.droogcompanii.application.ui.activity.main_screen_2;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.common.base.Optional;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;

import java.io.Serializable;

import ru.droogcompanii.application.DroogCompaniiSettings;
import ru.droogcompanii.application.data.db_util.CursorHandler;
import ru.droogcompanii.application.data.db_util.hierarchy_of_partners.PartnersContracts;
import ru.droogcompanii.application.data.db_util.hierarchy_of_partners.PartnersHierarchyReaderFromDatabase;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPointImpl;
import ru.droogcompanii.application.ui.fragment.partner_points_map.CustomMapFragment;
import ru.droogcompanii.application.ui.util.ActualBaseLocationProvider;
import ru.droogcompanii.application.ui.util.able_to_start_task.TaskNotBeInterruptedDuringConfigurationChange;
import ru.droogcompanii.application.util.NearestPositionCalculator;
import ru.droogcompanii.application.util.SerializableLatLng;

/**
 * Created by ls on 14.03.14.
 */
public class NewPartnerPointsMapFragment extends CustomMapFragment
        implements ClusterManager.OnClusterClickListener<NewPartnerPointsMapFragment.PartnerPointClusterItem>,
                   ClusterManager.OnClusterItemClickListener<NewPartnerPointsMapFragment.PartnerPointClusterItem> {

    private static final PartnersContracts.PartnerPointsContract PARTNER_POINTS = new PartnersContracts.PartnerPointsContract();
    private static final PartnersContracts.PartnersContract PARTNERS = new PartnersContracts.PartnersContract();

    private static final String KEY_CENTER = "KEY_CENTER";
    private static final String KEY_ZOOM = "KEY_ZOOM";
    private static final String KEY_CONDITION = "KEY_CONDITION";

    private static final int TASK_REQUEST_CODE_DISPLAYING_PARTNER_POINTS = 34316;

    static class PartnerPointClusterItem implements ClusterItem {

        private final PartnerPoint partnerPoint;

        public PartnerPointClusterItem(PartnerPoint partnerPoint) {
            this.partnerPoint = partnerPoint;
        }

        public LatLng getPosition() {
            return partnerPoint.getPosition();
        }

        public PartnerPoint getPartnerPoint() {
            return partnerPoint;
        }
    }

    private Optional<String> conditionToReceivePartners;
    private ClusterManager<PartnerPointClusterItem> clusterManager;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        if (savedInstanceState == null) {
            initStateByDefault();
        } else {
            restoreState(savedInstanceState);
        }
    }

    private void initStateByDefault() {
        conditionToReceivePartners = Optional.absent();
    }

    private void restoreState(Bundle savedInstanceState) {
        conditionToReceivePartners = (Optional<String>) savedInstanceState.getSerializable(KEY_CONDITION);
        restoreCameraState(savedInstanceState);
    }

    private void restoreCameraState(Bundle savedInstanceState) {
        LatLng center = savedInstanceState.getParcelable(KEY_CENTER);
        float zoom = savedInstanceState.getFloat(KEY_ZOOM);
        moveCamera(center, zoom);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveStateInto(outState);
    }

    private void saveStateInto(Bundle outState) {
        outState.putSerializable(KEY_CONDITION, conditionToReceivePartners);
        saveCameraStateInto(outState);
    }

    private void saveCameraStateInto(Bundle outState) {
        outState.putParcelable(KEY_CENTER, getCurrentCenter());
        outState.putFloat(KEY_ZOOM, getCurrentZoom());
    }

    private void init() {
        getGoogleMap().moveCamera(CameraUpdateFactory.newLatLngZoom(
                DroogCompaniiSettings.getDefaultBasePosition(),
                DroogCompaniiSettings.getDefaultZoom()
        ));
        clusterManager = new ClusterManager<PartnerPointClusterItem>(getActivity(), getGoogleMap());
        getGoogleMap().setOnCameraChangeListener(clusterManager);
        getGoogleMap().setOnMarkerClickListener(clusterManager);
        getGoogleMap().setOnInfoWindowClickListener(clusterManager);

        clusterManager.setOnClusterItemClickListener(this);
        clusterManager.setOnClusterClickListener(this);
    }

    @Override
    public boolean onClusterItemClick(PartnerPointClusterItem clusterItem) {
        return false;
    }

    @Override
    public boolean onClusterClick(Cluster<PartnerPointClusterItem> cluster) {
        increaseZoom(cluster.getPosition());
        return true;
    }

    private void increaseZoom(LatLng position) {
        final float MAX_ZOOM = 15.0f;
        float increasedZoom = Math.min(getCurrentZoom() + 1.0f, MAX_ZOOM);
        moveCamera(position, increasedZoom);
    }

    public void updateCondition(String conditionToReceivePartners) {
        this.conditionToReceivePartners = Optional.of(conditionToReceivePartners);
        clearMap();
        startTaskDisplayingPartnerPoints();
    }

    private void clearMap() {
        clusterManager.clearItems();
        clusterManager.cluster();
    }

    private void startTaskDisplayingPartnerPoints() {
        startTask(TASK_REQUEST_CODE_DISPLAYING_PARTNER_POINTS, new TaskNotBeInterruptedDuringConfigurationChange() {
            @Override
            protected Serializable doInBackground(Void... voids) {
                return displayPartnerPointsAndGetNearestPosition();
            }
        });
    }

    private Optional<SerializableLatLng> displayPartnerPointsAndGetNearestPosition() {
        PartnersHierarchyReaderFromDatabase reader = new PartnersHierarchyReaderFromDatabase(getActivity());

        LatLng actualBaseLocation = ActualBaseLocationProvider.getPositionOfActualBaseLocation();
        final NearestPositionCalculator nearestCalculator = new NearestPositionCalculator(actualBaseLocation);

        reader.handleCursorByQuery(prepareSql(), new CursorHandler() {
            @Override
            public void handle(Cursor cursor) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    PartnerPoint partnerPoint = readPartnerPoint(cursor);
                    clusterManager.addItem(new PartnerPointClusterItem(partnerPoint));
                    cursor.moveToNext();
                    nearestCalculator.add(partnerPoint.getPosition());
                }
            }
        });
        return nearestCalculator.getSerializableNearestPosition();
    }

    private String prepareSql() {
        String where;
        String condition = conditionToReceivePartners.get();
        if (condition.isEmpty()) {
            where = "";
        } else {
            where = "WHERE " + PARTNER_POINTS.COLUMN_NAME_PARTNER_ID + " IN ( " +
                        "SELECT " + PARTNERS.COLUMN_NAME_ID + " FROM " + PARTNERS.TABLE_NAME +
                            " WHERE " + condition +
                    " )";
        }
        return "SELECT " +
                PARTNER_POINTS.COLUMN_NAME_LATITUDE + ", " +
                PARTNER_POINTS.COLUMN_NAME_LONGITUDE + ", " +
                PARTNER_POINTS.COLUMN_NAME_TITLE +
                " FROM " + PARTNER_POINTS.TABLE_NAME + " " + where + ";";
    }

    private static PartnerPoint readPartnerPoint(Cursor cursor) {
        int latitudeColumnIndex = cursor.getColumnIndexOrThrow(PARTNER_POINTS.COLUMN_NAME_LATITUDE);
        int longitudeColumnIndex = cursor.getColumnIndexOrThrow(PARTNER_POINTS.COLUMN_NAME_LONGITUDE);
        int titleColumnIndex = cursor.getColumnIndexOrThrow(PARTNER_POINTS.COLUMN_NAME_TITLE);
        PartnerPointImpl partnerPoint = new PartnerPointImpl();
        partnerPoint.latitude = cursor.getDouble(latitudeColumnIndex);
        partnerPoint.longitude = cursor.getDouble(longitudeColumnIndex);
        partnerPoint.title = cursor.getString(titleColumnIndex);
        return partnerPoint;
    }

    @Override
    public void onTaskResult(int requestCode, int resultCode, Serializable result) {
        if (requestCode == TASK_REQUEST_CODE_DISPLAYING_PARTNER_POINTS && resultCode == Activity.RESULT_OK) {
            onPartnerPointsDisplayed(result);
        }
    }

    private void onPartnerPointsDisplayed(Serializable result) {
        clusterManager.cluster();
        Optional<SerializableLatLng> nearestPosition = (Optional<SerializableLatLng>) result;
        if (nearestPosition.isPresent()) {
            moveCamera(nearestPosition.get().toParcelable(), getCurrentZoom());
        }
    }
}
