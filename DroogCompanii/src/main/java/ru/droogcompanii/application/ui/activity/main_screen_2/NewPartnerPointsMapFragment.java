package ru.droogcompanii.application.ui.activity.main_screen_2;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.common.base.Optional;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;

import java.io.Serializable;
import java.util.List;

import ru.droogcompanii.application.DroogCompaniiSettings;
import ru.droogcompanii.application.data.db_util.CursorHandler;
import ru.droogcompanii.application.data.db_util.hierarchy_of_partners.PartnerHierarchyContracts;
import ru.droogcompanii.application.data.db_util.hierarchy_of_partners.PartnersHierarchyReaderFromDatabase;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPointImpl;
import ru.droogcompanii.application.ui.fragment.partner_points_map.CustomMapFragment;
import ru.droogcompanii.application.ui.fragment.partner_points_map.PartnerPointsGroupedByPosition;
import ru.droogcompanii.application.ui.util.ActualBaseLocationProvider;
import ru.droogcompanii.application.ui.util.able_to_start_task.TaskNotBeInterruptedDuringConfigurationChange;
import ru.droogcompanii.application.util.ConverterToString;
import ru.droogcompanii.application.util.ListUtils;
import ru.droogcompanii.application.util.LogUtils;
import ru.droogcompanii.application.util.NearestPositionCalculator;
import ru.droogcompanii.application.util.Objects;
import ru.droogcompanii.application.util.SerializableLatLng;

/**
 * Created by ls on 14.03.14.
 */
public class NewPartnerPointsMapFragment extends CustomMapFragment
        implements ClusterManager.OnClusterClickListener<NewPartnerPointsMapFragment.PartnerPointClusterItem>,
                   ClusterManager.OnClusterItemClickListener<NewPartnerPointsMapFragment.PartnerPointClusterItem>,
                   GoogleMap.OnMapClickListener {

    public static interface Callbacks {
        void onDisplayDetails(List<PartnerPoint> partnerPoints);
        void onHideDetails();
    }

    private static final Callbacks DUMMY_CALLBACKS = new Callbacks() {
        @Override
        public void onDisplayDetails(List<PartnerPoint> partnerPoints) {
            LogUtils.debug("DUMMY_CALLBACKS: onDisplayDetails()");
        }

        @Override
        public void onHideDetails() {
            LogUtils.debug("DUMMY_CALLBACKS: onHideDetails()");
        }
    };


    private static final PartnerHierarchyContracts.PartnerPointsContract PARTNER_POINTS = new PartnerHierarchyContracts.PartnerPointsContract();
    private static final PartnerHierarchyContracts.PartnersContract PARTNERS = new PartnerHierarchyContracts.PartnersContract();

    private static final String KEY_BOUNDS = "KEY_BOUNDS";
    private static final String KEY_CONDITION = "KEY_CONDITION";
    private static final String KEY_IS_FIRST_DISPLAYING = "KEY_IS_FIRST_DISPLAYING";

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

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            PartnerPointClusterItem other = (PartnerPointClusterItem) obj;
            return Objects.equals(partnerPoint, other.partnerPoint);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(partnerPoint);
        }

        @Override
        public String toString() {
            return ConverterToString.buildFor(this)
                    .withFieldNames("partnerPoint")
                    .withFieldValues(partnerPoint)
                    .toString();
        }
    }


    private boolean isFirstDisplaying;
    private Callbacks callbacks;
    private ClickedPositionHelper clickedPositionHelper;
    private ClusterManager<PartnerPointClusterItem> clusterManager;
    private Optional<String> conditionToReceivePartners;

    private PartnerPointsGroupedByPosition partnerPointsGroupedByPosition;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        callbacks = DUMMY_CALLBACKS;
        super.onDetach();
    }

    public NewPartnerPointsMapFragment() {
        clickedPositionHelper = new ClickedPositionHelper(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState == null) {
            initStateByDefault();
        } else {
            restoreState(savedInstanceState);
        }
        init();
    }

    private void initStateByDefault() {
        isFirstDisplaying = true;
        conditionToReceivePartners = Optional.absent();
        moveCameraToBasePosition();
    }

    private void moveCameraToBasePosition() {
        getGoogleMap().moveCamera(CameraUpdateFactory.newLatLngZoom(
                ActualBaseLocationProvider.getPositionOfActualBaseLocation(),
                DroogCompaniiSettings.getDefaultZoom()
        ));
    }

    private void restoreState(Bundle savedInstanceState) {
        clickedPositionHelper.restoreFrom(savedInstanceState);
        isFirstDisplaying = savedInstanceState.getBoolean(KEY_IS_FIRST_DISPLAYING);
        conditionToReceivePartners = (Optional<String>) savedInstanceState.getSerializable(KEY_CONDITION);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveStateInto(outState);
    }

    private void saveStateInto(Bundle outState) {
        clickedPositionHelper.saveInto(outState);
        outState.putBoolean(KEY_IS_FIRST_DISPLAYING, isFirstDisplaying);
        outState.putSerializable(KEY_CONDITION, conditionToReceivePartners);
        outState.putParcelable(KEY_BOUNDS, getBounds());
    }

    private void init() {
        clusterManager = new ClusterManager<PartnerPointClusterItem>(getActivity(), getGoogleMap());
        getGoogleMap().setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                clusterManager.onCameraChange(cameraPosition);
                NewPartnerPointsMapFragment.this.onCameraChange();
            }
        });
        getGoogleMap().setOnMarkerClickListener(clusterManager);
        getGoogleMap().setOnInfoWindowClickListener(clusterManager);
        getGoogleMap().setOnMapClickListener(this);

        clusterManager.setOnClusterItemClickListener(this);
        clusterManager.setOnClusterClickListener(this);
    }

    private void onCameraChange() {
        clickedPositionHelper.updateOnCameraChange();
    }

    @Override
    public void onMapClick(LatLng latLng) {
        removeClickedPosition();
    }

    @Override
    public boolean onClusterItemClick(PartnerPointClusterItem clusterItem) {
        clickedPositionHelper.set(clusterItem.getPosition());
        callbacks.onDisplayDetails(getPartnerPointsAtClickedPosition());
        return false;
    }

    private List<PartnerPoint> getPartnerPointsAtClickedPosition() {
        LatLng position = clickedPositionHelper.getClickedPosition();
        return ListUtils.listFromSet(partnerPointsGroupedByPosition.get(position));
    }

    @Override
    public boolean onClusterClick(Cluster<PartnerPointClusterItem> cluster) {
        increaseZoom(cluster.getPosition());
        return true;
    }

    private void increaseZoom(LatLng position) {
        float increasedZoom = Math.min(getCurrentZoom() + 1.0f, getMaxZoom());
        animateCamera(position, increasedZoom);
    }

    public void updateCondition(Optional<String> condition) {
        clearClusterManager();
        this.conditionToReceivePartners = condition;
        if (conditionToReceivePartners.isPresent()) {
            startTaskDisplayingPartnerPoints();
        }
    }

    public void clearMap() {
        clearClusterManager();
        clickedPositionHelper.remove();
    }

    private void clearClusterManager() {
        clusterManager.clearItems();
        clusterManager.cluster();
    }

    private static class TaskResult implements Serializable {
        boolean isBoundsNotContainMarkers;
        boolean isNeedToRemoveClickedPosition;
        Optional<SerializableLatLng> nearestPosition;
    }

    private void startTaskDisplayingPartnerPoints() {

        final LatLngBounds bounds = getBounds();

        startTask(TASK_REQUEST_CODE_DISPLAYING_PARTNER_POINTS, new TaskNotBeInterruptedDuringConfigurationChange() {
            @Override
            protected Serializable doInBackground(Void... voids) {
                return displayPartnerPointsAndGetNearestPosition(bounds);
            }
        });
    }

    private TaskResult displayPartnerPointsAndGetNearestPosition(final LatLngBounds bounds) {
        final TaskResult taskResult = new TaskResult();

        PartnersHierarchyReaderFromDatabase reader = new PartnersHierarchyReaderFromDatabase(getActivity());

        LatLng actualBaseLocation = ActualBaseLocationProvider.getPositionOfActualBaseLocation();
        final NearestPositionCalculator nearestCalculator = new NearestPositionCalculator(actualBaseLocation);

        taskResult.isBoundsNotContainMarkers = true;

        final PositionMatcher clickedPositionMatcher = new PositionMatcher(clickedPositionHelper.getOptionalClickedPosition());

        partnerPointsGroupedByPosition = new PartnerPointsGroupedByPosition();

        reader.handleCursorByQuery(prepareSql(), new CursorHandler() {
            @Override
            public void handle(Cursor cursor) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    PartnerPoint partnerPoint = readPartnerPoint(cursor);
                    LatLng position = partnerPoint.getPosition();

                    if (!partnerPointsGroupedByPosition.isContainPosition(position)) {
                        clusterManager.addItem(new PartnerPointClusterItem(partnerPoint));
                        nearestCalculator.add(position);
                        clickedPositionMatcher.makeOut(position);

                        if (taskResult.isBoundsNotContainMarkers && bounds.contains(position)) {
                            taskResult.isBoundsNotContainMarkers = false;
                        }
                    }

                    partnerPointsGroupedByPosition.put(partnerPoint);
                    cursor.moveToNext();

                }
            }
        });
        taskResult.isNeedToRemoveClickedPosition =
                clickedPositionHelper.isClickedPositionPresent() && !clickedPositionMatcher.isMet();
        taskResult.nearestPosition = nearestCalculator.getSerializableNearestPosition();
        return taskResult;
    }

    private String prepareSql() {
        return "SELECT " +
                PARTNER_POINTS.COLUMN_NAME_ID + ", " +
                PARTNER_POINTS.COLUMN_NAME_LATITUDE + ", " +
                PARTNER_POINTS.COLUMN_NAME_LONGITUDE + ", " +
                PARTNER_POINTS.COLUMN_NAME_TITLE +
                " FROM " + PARTNER_POINTS.TABLE_NAME + " " + getPartnerPointsWhereExpression() + ";";
    }

    private String getPartnerPointsWhereExpression() {
        String condition = conditionToReceivePartners.get().trim();
        if (condition.isEmpty()) {
            return "";
        }
        return "WHERE " + PARTNER_POINTS.COLUMN_NAME_PARTNER_ID + " IN (" +
                " SELECT " + PARTNERS.COLUMN_NAME_ID +
                " FROM " + PARTNERS.TABLE_NAME +
                " WHERE " + condition +
                ")";
    }

    private static PartnerPoint readPartnerPoint(Cursor cursor) {
        int idColumnIndex = cursor.getColumnIndexOrThrow(PARTNER_POINTS.COLUMN_NAME_ID);
        int latitudeColumnIndex = cursor.getColumnIndexOrThrow(PARTNER_POINTS.COLUMN_NAME_LATITUDE);
        int longitudeColumnIndex = cursor.getColumnIndexOrThrow(PARTNER_POINTS.COLUMN_NAME_LONGITUDE);
        int titleColumnIndex = cursor.getColumnIndexOrThrow(PARTNER_POINTS.COLUMN_NAME_TITLE);
        PartnerPointImpl partnerPoint = new PartnerPointImpl();
        partnerPoint.id = cursor.getInt(idColumnIndex);
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
        TaskResult taskResult = (TaskResult) result;
        if (taskResult.isNeedToRemoveClickedPosition) {
            removeClickedPosition();
        }
        updateMapCamera(taskResult);
    }

    private void removeClickedPosition() {
        clickedPositionHelper.remove();
        callbacks.onHideDetails();
    }

    private void updateMapCamera(TaskResult taskResult) {
        if (isFirstDisplaying) {
            isFirstDisplaying = false;
            moveToPosition(taskResult.nearestPosition);
        } else if (taskResult.isBoundsNotContainMarkers) {
            if (clickedPositionHelper.isClickedPositionPresent()) {
                moveToPosition(clickedPositionHelper.getClickedPosition());
            } else {
                moveToPosition(taskResult.nearestPosition);
            }
        }
    }

    private void moveToPosition(Optional<SerializableLatLng> optionalSerializablePosition) {
        if (optionalSerializablePosition.isPresent()) {
            LatLng position = optionalSerializablePosition.get().toParcelable();
            moveToPosition(position);
        }
    }

    private void moveToPosition(LatLng position) {
        moveCamera(position, getCurrentZoom());
    }
}
