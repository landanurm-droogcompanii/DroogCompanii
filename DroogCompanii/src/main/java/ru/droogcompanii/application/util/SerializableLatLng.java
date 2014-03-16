package ru.droogcompanii.application.util;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

/**
 * Created by Leonid on 16.03.14.
 */
public class SerializableLatLng implements Serializable {
    public final double latitude;
    public final double longitude;

    public static SerializableLatLng fromParcelable(LatLng latLng) {
        return new SerializableLatLng(latLng.latitude, latLng.longitude);
    }

    public SerializableLatLng(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if ((obj == null) || (((Object) this).getClass() != obj.getClass())) {
            return false;
        }
        SerializableLatLng other = (SerializableLatLng) obj;
        return (latitude == other.latitude) && (longitude == other.longitude);
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude);
    }

    @Override
    public String toString() {
        return ConverterToString.buildFor(this)
                .withFieldNames("latitude", "longitude")
                .withFieldValues(latitude, longitude)
                .toString();
    }

    public LatLng toParcelable() {
        return new LatLng(latitude, longitude);
    }
}
