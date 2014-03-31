package ru.droogcompanii.application.data.hierarchy_of_partners;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import ru.droogcompanii.application.data.working_hours.WeekWorkingHours;

/**
 * Created by ls on 13.02.14.
 */
public interface PartnerPoint {
    int getId();
    String getTitle();
    double getLatitude();
    double getLongitude();
    String getAddress();
    List<String> getPhones();
    String getPaymentMethods();
    WeekWorkingHours getWorkingHours();
    int getPartnerId();
    LatLng getPosition();
}
