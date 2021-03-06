package ru.droogcompanii.application.data.hierarchy_of_partners;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ru.droogcompanii.application.data.working_hours.WeekWorkingHours;
import ru.droogcompanii.application.util.Objects;

/**
 * Created by Leonid on 02.12.13.
 */
public class PartnerPointImpl implements PartnerPoint, Serializable {
    public double latitude;
    public double longitude;
    public int id;
    public int partnerId;
    public List<String> phones;
    public String address;
    public String paymentMethods;
    public String title;
    public WeekWorkingHours workingHours;


    public PartnerPointImpl() {
        id = 0;
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
        return (Objects.equals(id, other.getId()) &&
                Objects.equals(title, other.getTitle()) &&
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
            id, title, address, phones, workingHours, paymentMethods, longitude, latitude, partnerId
        );
    }

    @Override
    public LatLng getPosition() {
        return new LatLng(latitude, longitude);
    }

    @Override
    public String toString() {
        return com.google.common.base.Objects.toStringHelper(this)
                .add("id", id)
                .add("title", title)
                .add("address", address)
                .add("phones", phones)
                .add("workingHours", workingHours)
                .add("paymentMethods", paymentMethods)
                .add("latitude", latitude)
                .add("longitude", longitude)
                .add("partnerId", partnerId)
                .toString();
    }

    @Override
    public int getId() {
        return id;
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
