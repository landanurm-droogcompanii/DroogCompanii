package ru.droogcompanii.application.ui.fragment.partner_points_info_panel;

import android.content.Context;
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
public class RouteHelper {

    private final Context context;

    public RouteHelper(Context context) {
        this.context = context;
    }

    public void showRouteTo(PartnerPoint destination) {
        context.startActivity(RouteHelper.makeIntentRouteTo(destination));
    }

    private static Intent makeIntentRouteTo(PartnerPoint destination) {
        String uri = makeUri(destination);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        return intent;
    }

    private static String makeUri(PartnerPoint destination) {
        LatLng to = destination.getPosition();
        LatLng from = getPositionOfBaseLocation();
        return String.format(Locale.ENGLISH,
                "http://maps.google.com/maps?saddr=%f,%f&daddr=%f,%f(%s)",
                from.latitude, from.longitude,
                to.latitude, to.longitude,
                destination.title);
    }

    private static LatLng getPositionOfBaseLocation() {
        Location baseLocation = DroogCompaniiSettings.getBaseLocation();
        return new LatLng(baseLocation.getLatitude(), baseLocation.getLongitude());
    }
}
