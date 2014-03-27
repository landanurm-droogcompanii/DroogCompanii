package ru.droogcompanii.application.ui.main_screen_2.map;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.common.base.Optional;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;

import java.io.Serializable;

import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.ui.fragment.partner_points_map.PartnerPointsGroupedByPosition;
import ru.droogcompanii.application.ui.main_screen_2.filters_dialog.filters.Filters;
import ru.droogcompanii.application.ui.main_screen_2.map.clicked_position_helper.ClickedPositionHelper;
import ru.droogcompanii.application.util.NearestPositionCalculator;
import ru.droogcompanii.application.util.OnEachHandler;
import ru.droogcompanii.application.util.SerializableLatLng;

/**
 * Created by ls on 21.03.14.
 */
class Worker {

    public static class DisplayingTaskResult implements Serializable {
        public boolean isBoundsNotContainMarkers;
        public boolean isNeedToRemoveClickedPosition;
        public int numberOfDisplayedPartnerPoints;
        public Optional<SerializableLatLng> nearestPosition;
    }

    private final ClusterManager<ClusterItem> clusterManager;
    private final ClickedPositionHelper clickedPositionHelper;
    private final Optional<String> conditionToReceivePartnerPoints;

    Worker(ClusterManager<ClusterItem> clusterManager,
           ClickedPositionHelper clickedPositionHelper,
           Optional<String> conditionToReceivePartnerPoints) {
        this.clusterManager = clusterManager;
        this.clickedPositionHelper = clickedPositionHelper;
        this.conditionToReceivePartnerPoints = conditionToReceivePartnerPoints;
    }

    DisplayingTaskResult display(final boolean withFilters,
                                 final LatLngBounds bounds,
                                 final Filters currentFilters,
                                 final PartnerPointsGroupedByPosition partnerPointsGroupedByPosition) {

        final NearestPositionCalculator nearestCalculator = NearestPositionCalculator.fromActualBaseLocation();
        final PositionMatcher clickedPositionMatcher = new PositionMatcher(clickedPositionHelper.getOptionalClickedPosition());

        final DisplayingTaskResult taskResult = new DisplayingTaskResult();

        taskResult.isBoundsNotContainMarkers = true;

        taskResult.numberOfDisplayedPartnerPoints = 0;

        IteratorOverPartnerPointsInDb iterator = new IteratorOverPartnerPointsInDb(currentFilters, prepareWhere());

        OnEachHandler<PartnerPoint> onEachHandler = new OnEachHandler<PartnerPoint>() {
            @Override
            public void onEach(PartnerPoint partnerPoint) {

                ++taskResult.numberOfDisplayedPartnerPoints;

                LatLng position = partnerPoint.getPosition();

                boolean isPositionYetNotDisplayed = !partnerPointsGroupedByPosition.isContainPosition(position);

                if (isPositionYetNotDisplayed) {
                    clusterManager.addItem(new ClusterItemImpl(position));
                    nearestCalculator.add(position);
                    clickedPositionMatcher.makeOut(position);
                    if (taskResult.isBoundsNotContainMarkers && bounds.contains(position)) {
                        taskResult.isBoundsNotContainMarkers = false;
                    }
                }

                partnerPointsGroupedByPosition.put(partnerPoint);
            }
        };

        if (withFilters) {
            iterator.forEachUsingFilters(onEachHandler);
        } else {
            iterator.forEach(onEachHandler);
        }

        taskResult.nearestPosition = nearestCalculator.getSerializableNearestPosition();
        taskResult.isNeedToRemoveClickedPosition =
                clickedPositionHelper.isClickedPositionPresent() && !clickedPositionMatcher.isMet();
        return taskResult;
    }

    private String prepareWhere() {
        if (!conditionToReceivePartnerPoints.isPresent()) {
            return "";
        }
        String condition = conditionToReceivePartnerPoints.get().trim();
        if (condition.isEmpty()) {
            return "";
        }
        return "WHERE " + condition;
    }
}
