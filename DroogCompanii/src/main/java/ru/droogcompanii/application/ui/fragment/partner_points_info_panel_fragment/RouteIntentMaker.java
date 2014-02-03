package ru.droogcompanii.application.ui.fragment.partner_points_info_panel_fragment;

import android.content.Intent;
import android.location.Location;
import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;

import java.util.Locale;

import ru.droogcompanii.application.DroogCompaniiSettings;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;

/**
 * Created by ls on 29.01.14.
 */
class RouteIntentMaker {

    public static Intent makeIntentRouteTo(PartnerPoint toPartnerPoint) {
        String uri = makeUri(toPartnerPoint);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        return intent;
    }

    private static String makeUri(PartnerPoint destinationPartnerPoint) {
        LatLng destination = destinationPartnerPoint.getPosition();
        LatLng source = getPositionOfBaseLocation();
        return String.format(Locale.ENGLISH,
                "http://maps.google.com/maps?saddr=%f,%f&daddr=%f,%f(%s)",
                source.latitude, source.longitude,
                destination.latitude, destination.longitude,
                destinationPartnerPoint.title);
    }

    private static LatLng getPositionOfBaseLocation() {
        Location baseLocation = DroogCompaniiSettings.getBaseLocation();
        return new LatLng(baseLocation.getLatitude(), baseLocation.getLongitude());
    }
}
