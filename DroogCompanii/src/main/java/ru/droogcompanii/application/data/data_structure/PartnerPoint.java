package ru.droogcompanii.application.data.data_structure;

import java.io.Serializable;
import java.util.List;

import ru.droogcompanii.application.data.data_structure.working_hours.WeekWorkingHours;

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
        return title.hashCode() +
               address.hashCode() +
               phones.hashCode() +
               workingHours.hashCode() +
               paymentMethods.hashCode() +
               ((Double) longitude).hashCode() +
               ((Double) latitude).hashCode() +
               partnerId;
    }
}
