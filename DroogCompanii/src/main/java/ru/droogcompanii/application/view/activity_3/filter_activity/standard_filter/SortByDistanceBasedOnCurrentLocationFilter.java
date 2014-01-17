package ru.droogcompanii.application.view.activity_3.filter_activity.standard_filter;

import android.location.Location;

import java.io.Serializable;

import ru.droogcompanii.application.util.CurrentLocationProvider;

/**
 * Created by ls on 17.01.14.
 */
public class SortByDistanceBasedOnCurrentLocationFilter extends SortByDistanceFilter {

    private static class BaseLocationProviderImpl implements BaseLocationProvider, Serializable {
        @Override
        public Location getBaseLocation() {
            return CurrentLocationProvider.get();
        }
    }

    public SortByDistanceBasedOnCurrentLocationFilter() {
        super(new BaseLocationProviderImpl());
    }
}
