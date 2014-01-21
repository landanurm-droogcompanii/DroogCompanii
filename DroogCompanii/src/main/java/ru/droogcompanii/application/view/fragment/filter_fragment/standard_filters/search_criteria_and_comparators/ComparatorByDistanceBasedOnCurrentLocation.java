package ru.droogcompanii.application.view.fragment.filter_fragment.standard_filters.search_criteria_and_comparators;

import android.location.Location;

import java.io.Serializable;

import ru.droogcompanii.application.util.CurrentLocationProvider;

/**
 * Created by ls on 17.01.14.
 */
public class ComparatorByDistanceBasedOnCurrentLocation extends ComparatorByDistance {

    private static class BaseLocationProviderImpl implements BaseLocationProvider, Serializable {
        @Override
        public Location getBaseLocation() {
            return CurrentLocationProvider.get();
        }
    }

    public ComparatorByDistanceBasedOnCurrentLocation() {
        super(new BaseLocationProviderImpl());
    }
}
