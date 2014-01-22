package ru.droogcompanii.application.view.fragment.partner_points_map_fragment;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Collection;
import java.util.Set;

import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.util.StringsCombiner;

/**
 * Created by Leonid on 11.01.14.
 */
public class MultiPointsMarkerOptionsBuilder {

    public MarkerOptions buildFrom(LatLng position, Set<PartnerPoint> partnerPoints) {
        if (partnerPoints.isEmpty()) {
            throw new IllegalArgumentException("Cannot build marker options without partner points");
        } else if (partnerPoints.size() == 1) {
            return buildSimpleMarkerOptions(getFirstElement(partnerPoints));
        } else {
            return buildMultiPointsMarkerOptions(partnerPoints);
        }
    }

    private MarkerOptions buildSimpleMarkerOptions(PartnerPoint partnerPoint) {
        return null;
    }

    private static <T> T getFirstElement(Collection<T> collection) {
        for (T each : collection) {
            return each;
        }
        throw new IllegalArgumentException("Input collection is empty");
    }

    private MarkerOptions buildMultiPointsMarkerOptions(Set<PartnerPoint> partnerPoints) {
        return null;
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
