package ru.droogcompanii.application.data.hierarchy_of_partners;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.List;

import ru.droogcompanii.application.data.working_hours.WeekWorkingHours;
import ru.droogcompanii.application.util.HashCodeCalculator;

/**
 * Created by Leonid on 02.12.13.
 */
public class PartnerPoint implements Serializable {
    public final double latitude;
    public final double longitude;
    public final int partnerId;
    public final List<String> phones;
    public final String address;
    public final String paymentMethods;
    public final String title;
    public final WeekWorkingHours workingHours;

    public PartnerPoint(String title,
                        String address,
                        List<String> phones,
                        WeekWorkingHours workingHours,
                        String paymentMethods,
                        double longitude,
                        double latitude,
                        int partnerId) {
        this.title = title;
        this.address = address;
        this.phones = phones;
        this.workingHours = workingHours;
        this.paymentMethods = paymentMethods;
        this.longitude = longitude;
        this.latitude = latitude;
        this.partnerId = partnerId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof PartnerPoint)) {
            return false;
        }

        PartnerPoint other = (PartnerPoint) obj;
        return (title.equals(other.title)) &&
               (address.equals(other.address)) &&
               (phones.equals(other.phones)) &&
               (workingHours.equals(other.workingHours)) &&
               (paymentMethods.equals(other.paymentMethods)) &&
               (longitude == other.longitude) &&
               (latitude == other.latitude) &&
               (partnerId == other.partnerId);
    }

    @Override
    public int hashCode() {
        return HashCodeCalculator.hashCodeFromFields(
                title, address, phones, workingHours, paymentMethods, getPosition(), partnerId
        );
    }

    public LatLng getPosition() {
        return new LatLng(latitude, longitude);
    }

    public float distanceTo(PartnerPoint other) {
        if (other == null) {
            throw new IllegalArgumentException("argument <other> should be not null");
        }
        Location locationThis = toLocation();
        Location locationOther = other.toLocation();
        return locationThis.distanceTo(locationOther);
    }

    public Location toLocation() {
        Location location = new Location(title);
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        return location;
    }

    public float distanceTo(Location location) {
        if (location == null) {
            throw new IllegalArgumentException("argument <location> should be not null");
        }
        return toLocation().distanceTo(location);
    }

    @Override
    public String toString() {
        return title;
    }
}
