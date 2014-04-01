package ru.droogcompanii.application.ui.screens.main.map;

import android.database.Cursor;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.common.base.Optional;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import ru.droogcompanii.application.DroogCompaniiApplication;
import ru.droogcompanii.application.data.db_util.CursorHandler;
import ru.droogcompanii.application.data.db_util.hierarchy_of_partners.PartnerHierarchyContracts;
import ru.droogcompanii.application.data.db_util.hierarchy_of_partners.PartnersHierarchyReaderFromDatabase;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPointImpl;
import ru.droogcompanii.application.ui.screens.main.filters_dialog.filters.Filter;
import ru.droogcompanii.application.ui.screens.main.filters_dialog.filters.Filters;
import ru.droogcompanii.application.ui.screens.main.map.clicked_position_helper.ClickedPositionHelper;
import ru.droogcompanii.application.util.NearestPositionCalculator;
import ru.droogcompanii.application.util.OnEachHandler;
import ru.droogcompanii.application.util.SerializableLatLng;
import ru.droogcompanii.application.util.StringsCombiner;

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

    public static class Factory {
        private final IteratorOverPartnerPoints.Creator iteratorCreator;

        public Factory(final boolean withFilters) {
            iteratorCreator = new IteratorOverPartnerPoints.Creator() {
                @Override
                public IteratorOverPartnerPoints create(Filters currentFilters, String where) {
                    return (withFilters)
                            ? new IteratorOverPartnerPointsUsingFilters(currentFilters, where)
                            : new IteratorOverAllPartnerPoints(currentFilters, where);
                }
            };
        }

        public Worker create(ClusterManager<ClusterItem> clusterManager,
                             ClickedPositionHelper clickedPositionHelper,
                             Optional<String> conditionToReceivePartnerPoints) {

            return new Worker(iteratorCreator,
                              clusterManager,
                              clickedPositionHelper,
                              conditionToReceivePartnerPoints);
        }
    }


    private final ClusterManager<ClusterItem> clusterManager;
    private final ClickedPositionHelper clickedPositionHelper;
    private final IteratorOverPartnerPoints.Creator iteratorCreator;
    private final Optional<String> conditionToReceivePartnerPoints;


    private Worker(IteratorOverPartnerPoints.Creator iteratorCreator,
                   ClusterManager<ClusterItem> clusterManager,
                   ClickedPositionHelper clickedPositionHelper,
                   Optional<String> conditionToReceivePartnerPoints) {
        this.clusterManager = clusterManager;
        this.clickedPositionHelper = clickedPositionHelper;
        this.iteratorCreator = iteratorCreator;
        this.conditionToReceivePartnerPoints = conditionToReceivePartnerPoints;
    }

    DisplayingTaskResult display(final LatLngBounds bounds,
                                 final Filters currentFilters,
                                 final PartnerPointsGroupedByPosition partnerPointsGroupedByPosition) {

        final NearestPositionCalculator nearestCalculator =
                NearestPositionCalculator.fromActualBaseLocation();
        final PositionMatcher clickedPositionMatcher =
                new PositionMatcher(clickedPositionHelper.getOptionalClickedPosition());

        final DisplayingTaskResult taskResult = new DisplayingTaskResult();

        taskResult.isBoundsNotContainMarkers = true;

        taskResult.numberOfDisplayedPartnerPoints = 0;

        IteratorOverPartnerPoints iterator = iteratorCreator.create(currentFilters, prepareWhere());

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

        iterator.forEach(onEachHandler);

        taskResult.nearestPosition = nearestCalculator.getNearestPosition();
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


    private static interface IteratorOverPartnerPoints {
        public static interface Creator {
            IteratorOverPartnerPoints create(Filters currentFilters, String where);
        }

        void forEach(final OnEachHandler<PartnerPoint> onEachHandler);
    }


    static class IteratorOverAllPartnerPoints implements IteratorOverPartnerPoints {

        private static final PartnerHierarchyContracts.PartnerPointsContract
                PARTNER_POINTS = new PartnerHierarchyContracts.PartnerPointsContract();

        private final PartnersHierarchyReaderFromDatabase READER =
                new PartnersHierarchyReaderFromDatabase(DroogCompaniiApplication.getContext());

        private final String where;
        private final Filters filters;

        public IteratorOverAllPartnerPoints(Filters currentFilters, String where) {
            this.where = where;
            this.filters = currentFilters;
        }

        protected static interface OnEachPartnerPointAndCursorHandler {
            void onEach(PartnerPoint partnerPoint, Cursor cursor);
        }

        public void forEach(final OnEachHandler<PartnerPoint> onEachHandler) {
            forEach(new OnEachPartnerPointAndCursorHandler() {
                @Override
                public void onEach(PartnerPoint partnerPoint, Cursor cursor) {
                    onEachHandler.onEach(partnerPoint);
                }
            });
        }

        protected void forEach(final OnEachPartnerPointAndCursorHandler onEachHandler) {
            READER.handleCursorByQuery(prepareSql(), new CursorHandler() {
                @Override
                public void handle(Cursor cursor) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        PartnerPoint partnerPoint = readPartnerPoint(cursor);
                        onEachHandler.onEach(partnerPoint, cursor);
                        cursor.moveToNext();
                    }
                }
            });
        }

        private String prepareSql() {
            return "SELECT " +
                    prepareColumnList() +
                    " FROM " + PARTNER_POINTS.TABLE_NAME + " " +
                    where + ";";
        }

        private String prepareColumnList() {
            Set<String> columns = new HashSet<String>();
            columns.add(PARTNER_POINTS.COLUMN_ID);
            columns.add(PARTNER_POINTS.COLUMN_LATITUDE);
            columns.add(PARTNER_POINTS.COLUMN_LONGITUDE);
            columns.addAll(filters.getColumnsOfPartnerPoint());
            return StringsCombiner.combine(columns, ", ");
        }

        private PartnerPoint readPartnerPoint(Cursor cursor) {
            int idColumnIndex = cursor.getColumnIndexOrThrow(PARTNER_POINTS.COLUMN_ID);
            int latitudeColumnIndex = cursor.getColumnIndexOrThrow(PARTNER_POINTS.COLUMN_LATITUDE);
            int longitudeColumnIndex = cursor.getColumnIndexOrThrow(PARTNER_POINTS.COLUMN_LONGITUDE);
            PartnerPointImpl partnerPoint = new PartnerPointImpl();
            partnerPoint.id = cursor.getInt(idColumnIndex);
            partnerPoint.latitude = cursor.getDouble(latitudeColumnIndex);
            partnerPoint.longitude = cursor.getDouble(longitudeColumnIndex);
            return partnerPoint;
        }
    }

    static class IteratorOverPartnerPointsUsingFilters extends IteratorOverAllPartnerPoints {

        private final Filter filter;

        public IteratorOverPartnerPointsUsingFilters(Filters currentFilters, String where) {
            super(currentFilters, where);
            this.filter = currentFilters.getActiveFilter();
        }

        @Override
        public void forEach(final OnEachHandler<PartnerPoint> onEachHandler) {
            super.forEach(new OnEachPartnerPointAndCursorHandler() {
                @Override
                public void onEach(PartnerPoint partnerPoint, Cursor cursor) {
                    if (filter.isPassedThroughFilter(partnerPoint, cursor)) {
                        onEachHandler.onEach(partnerPoint);
                    }
                }
            });
        }
    }
}
