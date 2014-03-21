package ru.droogcompanii.application.ui.activity.main_screen_2.map;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.common.base.Optional;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;

import java.io.Serializable;

import ru.droogcompanii.application.data.db_util.hierarchy_of_partners.PartnerHierarchyContracts;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.ui.activity.main_screen_2.map.clicked_position_helper.ClickedPositionHelper;
import ru.droogcompanii.application.ui.fragment.partner_points_map.PartnerPointsGroupedByPosition;
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
        public Optional<SerializableLatLng> nearestPosition;
    }

    private static class Contracts {
        public static final PartnerHierarchyContracts.PartnerPointsContract
                PARTNER_POINTS = new PartnerHierarchyContracts.PartnerPointsContract();
        public static final PartnerHierarchyContracts.PartnersContract
                PARTNERS = new PartnerHierarchyContracts.PartnersContract();
    }

    private final ClusterManager<ClusterItem> clusterManager;
    private final ClickedPositionHelper clickedPositionHelper;
    private final Optional<String> conditionToReceivePartners;

    Worker(ClusterManager<ClusterItem> clusterManager,
           ClickedPositionHelper clickedPositionHelper,
           Optional<String> conditionToReceivePartners) {
        this.clusterManager = clusterManager;
        this.clickedPositionHelper = clickedPositionHelper;
        this.conditionToReceivePartners = conditionToReceivePartners;
    }

    DisplayingTaskResult displayAndGetNearestPosition(final LatLngBounds bounds,
                             final PartnerPointsGroupedByPosition partnerPointsGroupedByPosition) {

        final NearestPositionCalculator nearestCalculator = NearestPositionCalculator.fromActualBaseLocation();
        final PositionMatcher clickedPositionMatcher = new PositionMatcher(clickedPositionHelper.getOptionalClickedPosition());

        final DisplayingTaskResult taskResult = new DisplayingTaskResult();
        taskResult.isBoundsNotContainMarkers = true;

        IteratorOverPartnerPointsInDb iterator = new IteratorOverPartnerPointsInDb(prepareWhere());

        iterator.forEach(new OnEachHandler<PartnerPoint>() {
            @Override
            public void onEach(PartnerPoint partnerPoint) {

                LatLng position = partnerPoint.getPosition();

                boolean isPositionAlreadyDisplayed = partnerPointsGroupedByPosition.isContainPosition(position);

                if (isPositionAlreadyDisplayed) {
                    partnerPointsGroupedByPosition.put(partnerPoint);
                } else {
                    partnerPointsGroupedByPosition.put(partnerPoint);
                    clusterManager.addItem(new ClusterItemImpl(position));
                    nearestCalculator.add(position);
                    clickedPositionMatcher.makeOut(position);
                    if (taskResult.isBoundsNotContainMarkers && bounds.contains(position)) {
                        taskResult.isBoundsNotContainMarkers = false;
                    }
                }
            }
        });

        taskResult.nearestPosition = nearestCalculator.getSerializableNearestPosition();
        taskResult.isNeedToRemoveClickedPosition =
                clickedPositionHelper.isClickedPositionPresent() && !clickedPositionMatcher.isMet();
        return taskResult;
    }

    private String prepareWhere() {
        if (!conditionToReceivePartners.isPresent()) {
            return "";
        }
        String condition = conditionToReceivePartners.get().trim();
        if (condition.isEmpty()) {
            return "";
        }
        return "WHERE " + Contracts.PARTNER_POINTS.COLUMN_NAME_PARTNER_ID + " IN (" +
                " SELECT " + Contracts.PARTNERS.COLUMN_NAME_ID +
                " FROM " + Contracts.PARTNERS.TABLE_NAME +
                " WHERE " + condition +
                ")";
    }
}
