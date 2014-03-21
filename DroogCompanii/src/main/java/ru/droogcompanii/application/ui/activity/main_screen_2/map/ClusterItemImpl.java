package ru.droogcompanii.application.ui.activity.main_screen_2.map;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import ru.droogcompanii.application.util.ConverterToString;
import ru.droogcompanii.application.util.Objects;

/**
 * Created by ls on 21.03.14.
 */
public class ClusterItemImpl implements ClusterItem {
    private final LatLng position;

    public ClusterItemImpl(LatLng position) {
        this.position = position;
    }

    public LatLng getPosition() {
        return position;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ClusterItemImpl other = (ClusterItemImpl) obj;
        return Objects.equals(position, other.position);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(position);
    }

    @Override
    public String toString() {
        return ConverterToString.buildFor(this)
                .withFieldNames("position")
                .withFieldValues(position)
                .toString();
    }
}
