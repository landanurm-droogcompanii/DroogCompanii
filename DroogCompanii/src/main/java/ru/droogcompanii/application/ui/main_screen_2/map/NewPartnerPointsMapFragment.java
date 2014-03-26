package ru.droogcompanii.application.ui.main_screen_2.map;

import android.app.Activity;
import android.location.Location;
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
import java.util.Set;

import ru.droogcompanii.application.DroogCompaniiSettings;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.ui.fragment.partner_points_map.PartnerPointsGroupedByPosition;
import ru.droogcompanii.application.ui.main_screen_2.filters_dialog.filters.DistanceFilterHelper;
import ru.droogcompanii.application.ui.main_screen_2.filters_dialog.filters.Filters;
import ru.droogcompanii.application.ui.main_screen_2.map.circle_of_nearest.CircleOfNearestDrawer;
import ru.droogcompanii.application.ui.main_screen_2.map.clicked_position_helper.ClickedPositionHelper;
import ru.droogcompanii.application.ui.util.location.ActualBaseLocationProvider;
import ru.droogcompanii.application.ui.util.StateManager;
import ru.droogcompanii.application.ui.util.able_to_start_task.TaskNotBeInterruptedDuringConfigurationChange;
import ru.droogcompanii.application.util.ListUtils;
import ru.droogcompanii.application.util.SerializableLatLng;

/**
 * Created by ls on 14.03.14.
 */
public class NewPartnerPointsMapFragment extends CustomMapFragmentWithBaseLocation
        implements ClusterManager.OnClusterClickListener<ClusterItem>,
                   ClusterManager.OnClusterItemClickListener<ClusterItem>,
                   GoogleMap.OnMapClickListener,
                   GoogleMap.OnCameraChangeListener {


    public static interface Callbacks {
        void onDisplayDetails(List<PartnerPoint> partnerPoints);
        void onHideDetails();
        void onDisplayingIsStarted();
        void onDisplayingIsCompleted(int numberOfDisplayedPartnerPoints);
    }

    private static class Key {
        public static final String CONDITION = "KEY_CONDITION";
        public static final String IS_FIRST_DISPLAYING = "KEY_IS_FIRST_DISPLAYING";
    }

    private static class RequestCode {
        private static final int TASK_DISPLAYING = 34316;
    }

    private static final Callbacks DUMMY_CALLBACKS = new Callbacks() {
        @Override
        public void onDisplayDetails(List<PartnerPoint> partnerPoints) {
            // do nothing
        }

        @Override
        public void onHideDetails() {
            // do nothing
        }

        @Override
        public void onDisplayingIsStarted() {
            // do nothing
        }

        @Override
        public void onDisplayingIsCompleted(int numberOfDisplayedPartnerPoints) {
            // do nothing
        }
    };


    private boolean isFirstDisplaying;
    private Callbacks callbacks;
    private ClickedPositionHelper clickedPositionHelper;
    private ClusterManager<ClusterItem> clusterManager;
    private Optional<String> conditionToReceivePartners;
    private PartnerPointsGroupedByPosition partnerPointsGroupedByPosition;
    private CircleOfNearestDrawer nearestDrawer;


    private final StateManager STATE_MANAGER = new StateManager() {
        @Override
        public void initStateByDefault() {
            isFirstDisplaying = true;
            conditionToReceivePartners = Optional.absent();
            clickedPositionHelper = new ClickedPositionHelper(NewPartnerPointsMapFragment.this);
            moveCameraToActualBasePosition();
        }

        @Override
        public void restoreState(Bundle savedInstanceState) {
            isFirstDisplaying = savedInstanceState.getBoolean(Key.IS_FIRST_DISPLAYING);
            conditionToReceivePartners = (Optional<String>) savedInstanceState.getSerializable(Key.CONDITION);
            clickedPositionHelper = new ClickedPositionHelper(NewPartnerPointsMapFragment.this);
            clickedPositionHelper.restoreFrom(savedInstanceState);
        }

        @Override
        public void saveState(Bundle outState) {
            outState.putBoolean(Key.IS_FIRST_DISPLAYING, isFirstDisplaying);
            outState.putSerializable(Key.CONDITION, conditionToReceivePartners);
            clickedPositionHelper.saveInto(outState);
        }
    };

    private void moveCameraToActualBasePosition() {
        getGoogleMap().moveCamera(CameraUpdateFactory.newLatLngZoom(
                ActualBaseLocationProvider.getPositionOfActualBaseLocation(),
                DroogCompaniiSettings.getDefaultZoom()
        ));
    }

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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        STATE_MANAGER.initState(savedInstanceState);
        initMap();
        nearestDrawer = new CircleOfNearestDrawer(this);
        nearestDrawer.update();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        STATE_MANAGER.saveState(outState);
    }

    private void initMap() {
        clusterManager = new ClusterManager<ClusterItem>(getActivity(), getGoogleMap());
        getGoogleMap().setOnMarkerClickListener(clusterManager);
        getGoogleMap().setOnInfoWindowClickListener(clusterManager);
        getGoogleMap().setOnCameraChangeListener(this);
        getGoogleMap().setOnMapClickListener(this);
        clusterManager.setOnClusterItemClickListener(this);
        clusterManager.setOnClusterClickListener(this);
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        clusterManager.onCameraChange(cameraPosition);
        clickedPositionHelper.updateOnCameraChange();
    }

    @Override
    public void onMapClick(LatLng latLng) {
        onNeedToRemoveClickedPosition();
    }

    private void onNeedToRemoveClickedPosition() {
        clickedPositionHelper.remove();
        callbacks.onHideDetails();
    }

    @Override
    public boolean onClusterItemClick(ClusterItem clusterItem) {
        LatLng position = clusterItem.getPosition();
        clickedPositionHelper.set(position);
        Set<PartnerPoint> partnerPointsAtClickedPosition = partnerPointsGroupedByPosition.get(position);

        callbacks.onDisplayDetails(ListUtils.listFromSet(partnerPointsAtClickedPosition));
        return false;
    }

    @Override
    public boolean onClusterClick(Cluster<ClusterItem> cluster) {
        animateCamera(cluster.getPosition(), getIncreasedZoom());
        return true;
    }

    private float getIncreasedZoom() {
        return Math.min(getMaxZoom(), getCurrentZoom() + 1.0f);
    }

    public void updateCondition(Optional<String> conditionToReceivePartners) {
        if (isDisplayingTaskAlreadyStarted()) {
            cancelDisplayingTask();
        }
        this.conditionToReceivePartners = conditionToReceivePartners;
        updateMap();
    }

    private void updateMap() {
        clusterManager.clearItems();
        if (conditionToReceivePartners.isPresent()) {
            startDisplayingTask();
        } else {
            clusterManager.cluster();
        }
    }

    public boolean isDisplayingTaskAlreadyStarted() {
        return isTaskStarted(RequestCode.TASK_DISPLAYING);
    }

    private void cancelDisplayingTask() {
        cancelTask(RequestCode.TASK_DISPLAYING);
    }

    private void startDisplayingTask() {
        nearestDrawer.update();
        callbacks.onDisplayingIsStarted();

        final Filters currentFilters = Filters.getCurrent(getActivity());
        final LatLngBounds bounds = getBounds();
        startTask(RequestCode.TASK_DISPLAYING, new TaskNotBeInterruptedDuringConfigurationChange() {
            @Override
            protected Serializable doInBackground(Void... voids) {
                Worker worker = new Worker(clusterManager,
                        clickedPositionHelper, conditionToReceivePartners);
                partnerPointsGroupedByPosition = new PartnerPointsGroupedByPosition();
                return worker.display(bounds, currentFilters, partnerPointsGroupedByPosition);
            }
        });
    }

    @Override
    public void onTaskResult(int requestCode, int resultCode, Serializable result) {
        if (resultCode != Activity.RESULT_OK) {
            onTaskCancelled();
            return;
        }
        if (requestCode == RequestCode.TASK_DISPLAYING) {
            onDisplayingTaskFinished(result);
        }
    }

    private void onTaskCancelled() {
        Activity activity = getActivity();
        if (activity != null) {
            activity.finish();
        }
    }

    private void onDisplayingTaskFinished(Serializable result) {
        clusterManager.cluster();
        Worker.DisplayingTaskResult taskResult = (Worker.DisplayingTaskResult) result;
        if (taskResult.isNeedToRemoveClickedPosition) {
            onNeedToRemoveClickedPosition();
        }
        updateMapCamera(taskResult);
        callbacks.onDisplayingIsCompleted(taskResult.numberOfDisplayedPartnerPoints);
    }

    private void updateMapCamera(Worker.DisplayingTaskResult taskResult) {
        if (isFirstDisplaying) {
            isFirstDisplaying = false;
            tryMoveToPosition(taskResult.nearestPosition);
        } else if (taskResult.isBoundsNotContainMarkers) {
            if (clickedPositionHelper.isClickedPositionPresent()) {
                moveToPosition(clickedPositionHelper.getClickedPosition());
            } else {
                tryMoveToPosition(taskResult.nearestPosition);
            }
        }
    }

    private void tryMoveToPosition(Optional<SerializableLatLng> optionalSerializablePosition) {
        if (optionalSerializablePosition.isPresent()) {
            LatLng position = optionalSerializablePosition.get().toParcelable();
            moveToPosition(position);
        }
    }

    private void moveToPosition(LatLng position) {
        moveCamera(position, getCurrentZoom());
    }

    @Override
    public void onLocationChanged(Location location) {
        super.onLocationChanged(location);
        if (isDistanceFilterSet()) {
            updateMap();
        }
    }

    private boolean isDistanceFilterSet() {
        DistanceFilterHelper distanceFilterHelper = DistanceFilterHelper.getCurrent(getActivity());
        return distanceFilterHelper.isActive();
    }
}