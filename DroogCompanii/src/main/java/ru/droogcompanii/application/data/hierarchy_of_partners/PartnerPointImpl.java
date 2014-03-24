package ru.droogcompanii.application.data.hierarchy_of_partners;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ru.droogcompanii.application.data.working_hours.WeekWorkingHours;
import ru.droogcompanii.application.ui.util.LocationConverter;
import ru.droogcompanii.application.util.ConverterToString;
import ru.droogcompanii.application.util.Objects;

/**
 * Created by Leonid on 02.12.13.
 */
public class PartnerPointImpl implements PartnerPoint, Serializable, Parcelable {
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
        return LocationConverter.fromLatitudeLongitude(title, latitude, longitude);
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
                "id", "title", "address", "phones", "workingHours", "paymentMethods", "longitude", "latitude", "partnerId"
            )
            .withFieldValues(
                id, title, address, phones, workingHours, paymentMethods, longitude, latitude, partnerId
            )
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



    public static final Parcelable.Creator<PartnerPointImpl> CREATOR = new Parcelable.Creator<PartnerPointImpl>() {

        public PartnerPointImpl createFromParcel(Parcel in) {
            return new PartnerPointImpl(in);
        }

        public PartnerPointImpl[] newArray(int size) {
            return new PartnerPointImpl[size];
        }

    };


    public PartnerPointImpl(Parcel in) {
        latitude = in.readDouble();
        longitude = in.readDouble();
        id = in.readInt();
        partnerId = in.readInt();
        phones = (List<String>) in.readSerializable();
        address = in.readString();
        paymentMethods = in.readString();
        title = in.readString();
        workingHours = (WeekWorkingHours) in.readSerializable();
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
        parcel.writeInt(id);
        parcel.writeInt(partnerId);
        parcel.writeSerializable((Serializable) phones);
        parcel.writeString(address);
        parcel.writeString(paymentMethods);
        parcel.writeString(title);
        parcel.writeSerializable(workingHours);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
