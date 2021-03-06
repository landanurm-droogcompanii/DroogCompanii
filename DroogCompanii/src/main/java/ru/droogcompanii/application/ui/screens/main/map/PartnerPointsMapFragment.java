package ru.droogcompanii.application.ui.screens.main.map;

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
import ru.droogcompanii.application.ui.screens.main.filters_dialog.filters.DistanceFilterHelper;
import ru.droogcompanii.application.ui.screens.main.filters_dialog.filters.Filters;
import ru.droogcompanii.application.ui.screens.main.map.circle_of_nearest.ActualCircleOfNearestDrawer;
import ru.droogcompanii.application.ui.screens.main.map.circle_of_nearest.CircleOfNearestDrawer;
import ru.droogcompanii.application.ui.screens.main.map.circle_of_nearest.DummyCircleOfNearestDrawer;
import ru.droogcompanii.application.ui.screens.main.map.clicked_position_helper.ClickedPositionHelper;
import ru.droogcompanii.application.util.ListUtils;
import ru.droogcompanii.application.util.SerializableLatLng;
import ru.droogcompanii.application.util.StateManager;
import ru.droogcompanii.application.util.ui.able_to_start_task.TaskNotBeInterruptedDuringConfigurationChange;
import ru.droogcompanii.application.util.location.ActualBaseLocationProvider;

/**
 * Created by ls on 14.03.14.
 */
public class PartnerPointsMapFragment extends CustomMapFragmentWithBaseLocation
        implements ClusterManager.OnClusterClickListener<ClusterItem>,
                   ClusterManager.OnClusterItemClickListener<ClusterItem>,
                   GoogleMap.OnMapClickListener,
                   GoogleMap.OnCameraChangeListener {


    public static PartnerPointsMapFragment newInstance(boolean withFilters) {
        PartnerPointsMapFragment fragment = new PartnerPointsMapFragment();
        Bundle args = new Bundle();
        args.putBoolean(Key.WITH_FILTERS, withFilters);
        fragment.setArguments(args);
        return fragment;
    }

    public static interface Callbacks {
        void onMarkerClicked(List<PartnerPoint> partnerPoints);
        void onNoClickedMarker();
        void onDisplayingIsStarting();
        void onDisplayingIsCompleted(int numberOfDisplayedPartnerPoints);
        void onMapInitialized();
    }

    private static class Key {
        public static final String CONDITION = "KEY_CONDITION";
        public static final String IS_FIRST_DISPLAYING = "KEY_IS_FIRST_DISPLAYING";
        public static final String WITH_FILTERS = "WITH_FILTERS";
    }

    private static class DefaultValue {
        public static final boolean WITH_FILTERS = true;
    }

    private static class RequestCode {
        private static final int TASK_DISPLAYING = 34316;
    }

    private static final Callbacks DUMMY_CALLBACKS = new Callbacks() {
        @Override
        public void onMarkerClicked(List<PartnerPoint> partnerPoints) {
            // do nothing
        }

        @Override
        public void onNoClickedMarker() {
            // do nothing
        }

        @Override
        public void onDisplayingIsStarting() {
            // do nothing
        }

        @Override
        public void onDisplayingIsCompleted(int numberOfDisplayedPartnerPoints) {
            // do nothing
        }

        @Override
        public void onMapInitialized() {
            // do nothing
        }
    };


    private boolean withFilters;
    private boolean isFirstDisplaying;
    private Callbacks callbacks;
    private CircleOfNearestDrawer circleOfNearestDrawer;
    private ClickedPositionHelper clickedPositionHelper;
    private ClusterManager<ClusterItem> clusterManager;
    private Optional<String> conditionToReceivePartnerPoints;
    private PartnerPointsGroupedByPosition partnerPointsGroupedByPosition;
    private Worker.Factory workerFactory;


    private final StateManager STATE_MANAGER = new StateManager() {
        @Override
        public void initStateByDefault() {
            withFilters = getArguments().getBoolean(Key.WITH_FILTERS, DefaultValue.WITH_FILTERS);
            initWithFilters();
            isFirstDisplaying = true;
            conditionToReceivePartnerPoints = Optional.absent();
            clickedPositionHelper = new ClickedPositionHelper(PartnerPointsMapFragment.this);
        }

        @Override
        public void restoreState(Bundle savedInstanceState) {
            withFilters = savedInstanceState.getBoolean(Key.WITH_FILTERS, DefaultValue.WITH_FILTERS);
            initWithFilters();
            isFirstDisplaying = savedInstanceState.getBoolean(Key.IS_FIRST_DISPLAYING);
            conditionToReceivePartnerPoints = (Optional<String>) savedInstanceState.getSerializable(Key.CONDITION);
            clickedPositionHelper = new ClickedPositionHelper(PartnerPointsMapFragment.this);
            clickedPositionHelper.restoreFrom(savedInstanceState);
        }

        @Override
        public void saveState(Bundle outState) {
            outState.putBoolean(Key.WITH_FILTERS, withFilters);
            outState.putBoolean(Key.IS_FIRST_DISPLAYING, isFirstDisplaying);
            outState.putSerializable(Key.CONDITION, conditionToReceivePartnerPoints);
            clickedPositionHelper.saveInto(outState);
        }
    };

    private void initWithFilters() {
        if (withFilters) {
            circleOfNearestDrawer = new ActualCircleOfNearestDrawer(this);
        } else {
            circleOfNearestDrawer = new DummyCircleOfNearestDrawer();
        }
        workerFactory = new Worker.Factory(withFilters);
    }

    private void moveCameraToActualBasePosition() {
        getGoogleMap().moveCamera(CameraUpdateFactory.newLatLngZoom(
                ActualBaseLocationProvider.getActualBasePosition(),
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
        onPostMapInitialized(savedInstanceState);
    }

    private void initMap() {
        clusterManager = new ClusterManager<ClusterItem>(getActivity(), getGoogleMap());
        getGoogleMap().setOnMarkerClickListener(clusterManager);
        getGoogleMap().setOnInfoWindowClickListener(clusterManager);
        getGoogleMap().setOnCameraChangeListener(this);
        getGoogleMap().setOnMapClickListener(this);
        clusterManager.setOnClusterItemClickListener(this);
        clusterManager.setOnClusterClickListener(this);
        circleOfNearestDrawer.update();
        callbacks.onMapInitialized();
    }

    private void onPostMapInitialized(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            moveCameraToActualBasePosition();
        } else {
            updateMap();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        STATE_MANAGER.saveState(outState);
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        clusterManager.onCameraChange(cameraPosition);
        clickedPositionHelper.updateOnCameraChange();
    }

    @Override
    public void onMapClick(LatLng latLng) {
        removeClickedPosition();
    }

    private void removeClickedPosition() {
        clickedPositionHelper.remove();
        callbacks.onNoClickedMarker();
    }

    @Override
    public boolean onClusterItemClick(ClusterItem clusterItem) {
        notifyNeedToShowPartnerPoints(clusterItem.getPosition());
        return false;
    }

    private void notifyNeedToShowPartnerPoints(LatLng position) {
        clickedPositionHelper.set(position);
        List<PartnerPoint> partnerPoints = getPartnerPointsAtPosition(position);
        if (partnerPoints.size() > 0) {
            callbacks.onMarkerClicked(partnerPoints);
        }
    }

    private List<PartnerPoint> getPartnerPointsAtPosition(LatLng position) {
        Set<PartnerPoint> partnerPointsAtClickedPosition = partnerPointsGroupedByPosition.get(position);
        return ListUtils.convertToList(partnerPointsAtClickedPosition);
    }

    public void selectPartnerPoint(PartnerPoint partnerPoint) {
        LatLng position = partnerPoint.getPosition();
        clickedPositionHelper.set(position);
        List<PartnerPoint> partnerPoints = getPartnerPointsAtPosition(position);
        movePartnerPointWithThisIdAtFirstPositionAtList(partnerPoint.getId(), partnerPoints);
        callbacks.onMarkerClicked(partnerPoints);
        moveCamera(position);
    }

    private static void movePartnerPointWithThisIdAtFirstPositionAtList(int partnerPointId, List<PartnerPoint> partnerPoints) {
        int index = indexOfPartnerPointWithThisId(partnerPointId, partnerPoints);
        ListUtils.swap(partnerPoints, 0, index);
    }

    private static int indexOfPartnerPointWithThisId(int partnerPointId, List<PartnerPoint> partnerPoints) {
        for (int i = 0; i < partnerPoints.size(); ++i) {
            if (partnerPointId == partnerPoints.get(i).getId()) {
                return i;
            }
        }
        throw new IllegalStateException("There is should be partner point with this <id> at list");
    }

    @Override
    public boolean onClusterClick(Cluster<ClusterItem> cluster) {
        animateCamera(cluster.getPosition(), getIncreasedZoom());
        return true;
    }

    private float getIncreasedZoom() {
        return Math.min(getMaxZoom(), getCurrentZoom() + 1.0f);
    }

    public void updateCondition(Optional<String> conditionToReceivePartnerPoints) {
        this.conditionToReceivePartnerPoints = conditionToReceivePartnerPoints;
        updateMap();
    }

    private void updateMap() {
        if (isDisplayingTaskAlreadyStarted()) {
            cancelDisplayingTask();
        }
        clusterManager.clearItems();
        if (conditionToReceivePartnerPoints.isPresent()) {
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
        circleOfNearestDrawer.update();
        callbacks.onDisplayingIsStarting();

        final Filters currentFilters = Filters.getCurrent(getActivity());
        final LatLngBounds bounds = getBounds();
        startTask(RequestCode.TASK_DISPLAYING, new TaskNotBeInterruptedDuringConfigurationChange() {
            @Override
            protected Serializable doInBackground(Void... voids) {
                Worker worker = workerFactory.create(clusterManager,
                        clickedPositionHelper, conditionToReceivePartnerPoints);
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
        updateClickedPosition(taskResult);
        updateMapCamera(taskResult);
        callbacks.onDisplayingIsCompleted(taskResult.numberOfDisplayedPartnerPoints);
    }

    private void updateClickedPosition(Worker.DisplayingTaskResult taskResult) {
        if (taskResult.isNeedToRemoveClickedPosition) {
            removeClickedPosition();
        } else if (clickedPositionHelper.isClickedPositionPresent()) {
            notifyNeedToShowPartnerPoints(clickedPositionHelper.getClickedPosition());
        }
    }

    private void updateMapCamera(Worker.DisplayingTaskResult taskResult) {
        if (isFirstDisplaying) {
            isFirstDisplaying = false;
            moveToPositionIfPresent(taskResult.nearestPosition);
        } else if (taskResult.isBoundsNotContainMarkers) {
            if (clickedPositionHelper.isClickedPositionPresent()) {
                moveToPosition(clickedPositionHelper.getClickedPosition());
            } else {
                moveToPositionIfPresent(taskResult.nearestPosition);
            }
        }
    }

    private void moveToPositionIfPresent(Optional<SerializableLatLng> optionalSerializablePosition) {
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

    @Override
    protected boolean isAbleToChangeBaseLocationByLongClickOnMap() {
        return withFilters;
    }
}