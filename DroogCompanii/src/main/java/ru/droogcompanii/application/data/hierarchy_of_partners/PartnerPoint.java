package ru.droogcompanii.application.data.hierarchy_of_partners;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import ru.droogcompanii.application.data.working_hours.WeekWorkingHours;

/**
 * Created by ls on 13.02.14.
 */
public interface PartnerPoint {
    String getTitle();
    double getLatitude();
    double getLongitude();
    String getAddress();
    List<String> getPhones();
    String getPaymentMethods();
    WeekWorkingHours getWorkingHours();
    int getPartnerId();

    LatLng getPosition();
    float distanceTo(PartnerPoint other);
    float distanceTo(Location location);
    Location toLocation();
}
