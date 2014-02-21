package ru.droogcompanii.application.data.hierarchy_of_partners;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ru.droogcompanii.application.data.working_hours.WeekWorkingHours;
import ru.droogcompanii.application.util.ConverterToString;
import ru.droogcompanii.application.util.Objects;

/**
 * Created by Leonid on 02.12.13.
 */
public class PartnerPointImpl implements PartnerPoint, Serializable {
    public double latitude;
    public double longitude;
    public int partnerId;
    public List<String> phones;
    public String address;
    public String paymentMethods;
    public String title;
    public WeekWorkingHours workingHours;


    public PartnerPointImpl() {
        title = "";
        address = "";
        latitude = 0.0;
        longitude = 0.0;
        phones = new ArrayList<String>();
        paymentMethods = "";
        workingHours = null;
        partnerId = 0;
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
        return (Objects.equals(title, other.getTitle()) &&
                Objects.equals(address, other.getAddress()) &&
                Objects.equals(phones, other.getPhones()) &&
                Objects.equals(workingHours, other.getWorkingHours()) &&
                Objects.equals(paymentMethods, other.getPaymentMethods()) &&
                Objects.equals(longitude, other.getLongitude()) &&
                Objects.equals(latitude, other.getLatitude()) &&
                Objects.equals(partnerId, other.getPartnerId()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            title, address, phones, workingHours, paymentMethods, longitude, latitude, partnerId
        );
    }

    @Override
    public LatLng getPosition() {
        return new LatLng(latitude, longitude);
    }

    @Override
    public float distanceTo(PartnerPoint other) {
        if (other == null) {
            throw new IllegalArgumentException("argument <other> should be not null");
        }
        Location locationThis = toLocation();
        Location locationOther = other.toLocation();
        return locationThis.distanceTo(locationOther);
    }

    @Override
    public Location toLocation() {
        Location location = new Location(title);
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        return location;
    }

    @Override
    public float distanceTo(Location location) {
        if (location == null) {
            throw new IllegalArgumentException("argument <location> should be not null");
        }
        return toLocation().distanceTo(location);
    }

    @Override
    public String toString() {
        return ConverterToString.buildFor(this)
            .withFieldNames(
                "title", "address", "phones", "workingHours", "paymentMethods", "longitude", "latitude", "partnerId"
            )
            .withFieldValues(
                title, address, phones, workingHours, paymentMethods, longitude, latitude, partnerId
            )
            .toString();
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public double getLatitude() {
        return latitude;
    }

    @Override
    public double getLongitude() {
        return longitude;
    }

    @Override
    public String getAddress() {
        return address;
    }

    @Override
    public List<String> getPhones() {
        return phones;
    }

    @Override
    public String getPaymentMethods() {
        return paymentMethods;
    }

    @Override
    public WeekWorkingHours getWorkingHours() {
        return workingHours;
    }

    @Override
    public int getPartnerId() {
        return partnerId;
    }
}
