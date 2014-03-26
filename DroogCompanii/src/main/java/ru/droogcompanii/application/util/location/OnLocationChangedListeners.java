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
public class OnLocationChangedListeners {

    private static final Collection<WeakReferenceWrapper<LocationSource.OnLocationChangedListener>>
            listenerWrappers = new ArrayList<WeakReferenceWrapper<LocationSource.OnLocationChangedListener>>();


    public static void addOnLocationChangedListener(LocationSource.OnLocationChangedListener listener) {
        removeOnLocationChangedListener(listener);
        listenerWrappers.add(WeakReferenceWrapper.from(listener));
    }

    public static void removeAllOnLocationChangedListeners() {
        listenerWrappers.clear();
    }

    public static void removeOnLocationChangedListener(final LocationSource.OnLocationChangedListener listenerToRemove) {
        final Holder<Boolean> isFound = Holder.from(false);

        Iterator<WeakReferenceWrapper<LocationSource.OnLocationChangedListener>> iterator = listenerWrappers.iterator();
        while (iterator.hasNext()) {
            WeakReferenceWrapper<LocationSource.OnLocationChangedListener> each = iterator.next();
            each.handleIfExist(new WeakReferenceWrapper.Handler<LocationSource.OnLocationChangedListener>() {
                @Override
                public void handle(LocationSource.OnLocationChangedListener eachListener) {
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

    public static void notifyListeners() {
        final Location actualLocation = ActualBaseLocationProvider.getActualBaseLocation();
        for (WeakReferenceWrapper<LocationSource.OnLocationChangedListener> listenerWrapper : listenerWrappers) {
            listenerWrapper.handleIfExist(new WeakReferenceWrapper.Handler<LocationSource.OnLocationChangedListener>() {
                @Override
                public void handle(LocationSource.OnLocationChangedListener listener) {
                    listener.onLocationChanged(actualLocation);
                }
            });
        }
    }
}
