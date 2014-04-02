package ru.droogcompanii.application.util.location;

import android.location.Location;

import com.google.android.gms.maps.LocationSource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import ru.droogcompanii.application.util.Holder;
import ru.droogcompanii.application.util.Objects;
import ru.droogcompanii.application.util.WeakReferenceWrapper;

/**
 * Created by ls on 26.03.14.
 */
public class LocationStateListeners {

    public static interface LocationStateListener
            extends LocationSource.OnLocationChangedListener,
                    OnCurrentAndCustomLocationsAreNotAvailableListener {
    }

    private static final Collection<WeakReferenceWrapper<LocationStateListener>>
            listenerWrappers = new ArrayList<WeakReferenceWrapper<LocationStateListener>>();


    public static void addListener(LocationStateListener listener) {
        removeListener(listener);
        listenerWrappers.add(WeakReferenceWrapper.from(listener));
    }

    public static void removeListener(final LocationStateListener listenerToRemove) {
        final Holder<Boolean> isFound = Holder.from(false);

        Iterator<WeakReferenceWrapper<LocationStateListener>> iterator = listenerWrappers.iterator();
        while (iterator.hasNext()) {
            WeakReferenceWrapper<LocationStateListener> each = iterator.next();
            each.handleIfExist(new WeakReferenceWrapper.Handler<LocationStateListener>() {
                @Override
                public void handle(LocationStateListener eachListener) {
                    if (Objects.equals(eachListener, listenerToRemove)) {
                        isFound.value = true;
                    }
                }
            });
            if (isFound.value) {
                iterator.remove();
                break;
            }
        }
    }

    static void notifyListenersAboutLocationChange() {
        final Location actualLocation = ActualBaseLocationProvider.getActualBaseLocation();
        for (WeakReferenceWrapper<LocationStateListener> listenerWrapper : listenerWrappers) {
            listenerWrapper.handleIfExist(new WeakReferenceWrapper.Handler<LocationStateListener>() {
                @Override
                public void handle(LocationStateListener listener) {
                    listener.onLocationChanged(actualLocation);
                }
            });
        }
    }

    public static void notifyListenersAboutCurrentAndCustomLocationsAreNotAvailable() {
        for (WeakReferenceWrapper<LocationStateListener> listenerWrapper : listenerWrappers) {
            listenerWrapper.handleIfExist(new WeakReferenceWrapper.Handler<LocationStateListener>() {
                @Override
                public void handle(LocationStateListener listener) {
                    listener.onCurrentAndCustomLocationsAreNotAvailable();
                }
            });
        }
    }
}
