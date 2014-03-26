package ru.droogcompanii.application.ui.fragment.learn_maps_utility.marker_clustering;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;

import java.io.Serializable;

import ru.droogcompanii.application.DroogCompaniiSettings;
import ru.droogcompanii.application.data.db_util.CursorHandler;
import ru.droogcompanii.application.data.db_util.hierarchy_of_partners.PartnerHierarchyContracts;
import ru.droogcompanii.application.data.db_util.hierarchy_of_partners.PartnerPointsReader;
import ru.droogcompanii.application.data.db_util.hierarchy_of_partners.PartnersHierarchyReaderFromDatabase;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPointImpl;
import ru.droogcompanii.application.ui.fragment.partner_points_map.CustomMapFragment;
import ru.droogcompanii.application.util.able_to_start_task.TaskNotBeInterruptedDuringConfigurationChange;
import ru.droogcompanii.application.util.LogUtils;
import ru.droogcompanii.application.util.WeakReferenceWrapper;

/**
 * Created by ls on 14.03.14.
 */
public class LearnMarkerClusteringFragment extends CustomMapFragment {

    private static final String KEY_CENTER = "KEY_CENTER";
    private static final String KEY_ZOOM = "KEY_ZOOM";

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

    static class ClusterItemImpl implements ClusterItem {

        private final LatLng latLng;

        public ClusterItemImpl(LatLng latLng) {
            this.latLng = latLng;
        }

        public LatLng getPosition() {
            return latLng;
        }
    }

    private ClusterManager<PartnerPointClusterItem> clusterManager;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        if (savedInstanceState != null) {
            restoreState(savedInstanceState);
        }
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

        clusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<PartnerPointClusterItem>() {
            @Override
            public boolean onClusterItemClick(PartnerPointClusterItem partnerPointClusterItem) {
                LogUtils.debug("onClusterItemClick()");
                return false;
            }
        });
        clusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<PartnerPointClusterItem>() {
            @Override
            public boolean onClusterClick(Cluster<PartnerPointClusterItem> partnerPointClusterItemCluster) {
                LogUtils.debug("onClusterClick(): " + partnerPointClusterItemCluster.getItems().size() + " items");
                increaseZoom(partnerPointClusterItemCluster.getPosition());
                return true;
            }
        });
        startTaskDisplayingPartnerPoints();
    }

    private void increaseZoom(LatLng position) {
        final float MAX_ZOOM = 15.0f;
        float increasedZoom = Math.min(getCurrentZoom() + 1.0f, MAX_ZOOM);
        moveCamera(position, increasedZoom);
    }

    private void startTaskDisplayingPartnerPoints() {
        final WeakReferenceWrapper<LearnMarkerClusteringFragment> fragmentWrapper = WeakReferenceWrapper.from(this);
        startTask(TASK_REQUEST_CODE_DISPLAYING_PARTNER_POINTS, new TaskNotBeInterruptedDuringConfigurationChange() {
            @Override
            protected Serializable doInBackground(Void... voids) {
                fragmentWrapper.handleIfExist(new WeakReferenceWrapper.Handler<LearnMarkerClusteringFragment>() {
                    @Override
                    public void handle(LearnMarkerClusteringFragment fragment) {
                        doWork();
                    }
                });
                return null;
            }
        });
    }

    private void doWork() {
        Context context = getActivity();
        if (context == null) {
            return;
        }
        displayPartnerPoints(context, clusterManager);
    }

    private static void displayPartnerPoints(Context context, final ClusterManager<PartnerPointClusterItem> clusterManager) {
        final PartnerHierarchyContracts.PartnerPointsContract PARTNER_POINTS = new PartnerHierarchyContracts.PartnerPointsContract();
        PartnersHierarchyReaderFromDatabase reader = new PartnerPointsReader(context);
        final String sql = "SELECT " +
                PartnerHierarchyContracts.PartnerPointsContract.COLUMN_NAME_LATITUDE + ", " +
                PartnerHierarchyContracts.PartnerPointsContract.COLUMN_NAME_LONGITUDE + ", " +
                PartnerHierarchyContracts.PartnerPointsContract.COLUMN_NAME_TITLE +
                " FROM " + PARTNER_POINTS.TABLE_NAME + ";";
        reader.handleCursorByQuery(sql, new CursorHandler() {
            @Override
            public void handle(Cursor cursor) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    PartnerPoint partnerPoint = readPartnerPoint(cursor);
                    clusterManager.addItem(new PartnerPointClusterItem(partnerPoint));
                    cursor.moveToNext();
                }
            }
        });
    }

    private static PartnerPoint readPartnerPoint(Cursor cursor) {
        int latitudeColumnIndex = cursor.getColumnIndexOrThrow(PartnerHierarchyContracts.PartnerPointsContract.COLUMN_NAME_LATITUDE);
        int longitudeColumnIndex = cursor.getColumnIndexOrThrow(PartnerHierarchyContracts.PartnerPointsContract.COLUMN_NAME_LONGITUDE);
        int titleColumnIndex = cursor.getColumnIndexOrThrow(PartnerHierarchyContracts.PartnerPointsContract.COLUMN_NAME_TITLE);
        PartnerPointImpl partnerPoint = new PartnerPointImpl();
        partnerPoint.latitude = cursor.getDouble(latitudeColumnIndex);
        partnerPoint.longitude = cursor.getDouble(longitudeColumnIndex);
        partnerPoint.title = cursor.getString(titleColumnIndex);
        return partnerPoint;
    }

    private static LatLng readPosition(Cursor cursor) {
        int latitudeColumnIndex = cursor.getColumnIndexOrThrow(PartnerHierarchyContracts.PartnerPointsContract.COLUMN_NAME_LATITUDE);
        int longitudeColumnIndex = cursor.getColumnIndexOrThrow(PartnerHierarchyContracts.PartnerPointsContract.COLUMN_NAME_LONGITUDE);
        double latitude = cursor.getDouble(latitudeColumnIndex);
        double longitude = cursor.getDouble(longitudeColumnIndex);
        return new LatLng(latitude, longitude);
    }

    private void restoreState(Bundle savedInstanceState) {
        LatLng center = savedInstanceState.getParcelable(KEY_CENTER);
        float zoom = savedInstanceState.getFloat(KEY_ZOOM);
        moveCamera(center, zoom);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(KEY_CENTER, getCurrentCenter());
        outState.putFloat(KEY_ZOOM, getCurrentZoom());
    }
}
