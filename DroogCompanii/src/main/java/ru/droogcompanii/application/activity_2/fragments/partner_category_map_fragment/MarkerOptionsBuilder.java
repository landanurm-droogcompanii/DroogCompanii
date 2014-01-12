package ru.droogcompanii.application.activity_2.fragments.partner_category_map_fragment;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import ru.droogcompanii.application.data.data_structure.PartnerPoint;
import ru.droogcompanii.application.util.StringsCombiner;

/**
 * Created by Leonid on 11.01.14.
 */
class MarkerOptionsBuilder {

    public MarkerOptions buildFrom(PartnerPoint partnerPoint) {
        LatLng position = new LatLng(partnerPoint.latitude, partnerPoint.longitude);
        return new MarkerOptions()
                .title(partnerPoint.title)
                .position(position)
                .snippet(prepareSnippet(partnerPoint));
    }

    private String prepareSnippet(PartnerPoint partnerPoint) {
        if (partnerPointHasAddress(partnerPoint)) {
            return partnerPoint.address;
        } else if (partnerPointHasPhone(partnerPoint)) {
            return StringsCombiner.combine(partnerPoint.phones, ", ");
        } else {
            return "";
        }
    }

    private boolean partnerPointHasAddress(PartnerPoint partnerPoint) {
        String address = partnerPoint.address.trim();
        return !address.isEmpty();
    }

    private boolean partnerPointHasPhone(PartnerPoint partnerPoint) {
        int numberOfPhones = partnerPoint.phones.size();
        return numberOfPhones > 0;
    }
}
