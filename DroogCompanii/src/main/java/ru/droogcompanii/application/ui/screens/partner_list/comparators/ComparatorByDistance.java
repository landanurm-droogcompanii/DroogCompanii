package ru.droogcompanii.application.ui.screens.partner_list.comparators;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.droogcompanii.application.DroogCompaniiApplication;
import ru.droogcompanii.application.data.db_util.hierarchy_of_partners.PartnerPointsReader;
import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.util.MinMax;
import ru.droogcompanii.application.util.SerializableLatLng;
import ru.droogcompanii.application.util.location.ActualBaseLocationProvider;

/**
 * Created by ls on 02.04.14.
 */
public class ComparatorByDistance extends BaseComparator<Partner> implements Serializable {
    private transient LatLng basePosition;
    private SerializableLatLng serializableBasePosition;
    private Map<Integer, Double> pairsPartnerIdAndMinDistance = new HashMap<Integer, Double>();


    @Override
    public int compare(Partner partner1, Partner partner2) {
        Double d1 = getMinDistanceToBasePosition(partner1);
        Double d2 = getMinDistanceToBasePosition(partner2);
        return d1.compareTo(d2);
    }

    private Double getMinDistanceToBasePosition(Partner partner) {
        Integer key = partner.getId();
        if (pairsPartnerIdAndMinDistance.containsKey(key)) {
            return pairsPartnerIdAndMinDistance.get(key);
        } else {
            Double minDistance = computeMinDistanceToBasePosition(partner);
            pairsPartnerIdAndMinDistance.put(key, minDistance);
            return minDistance;
        }
    }

    private Double computeMinDistanceToBasePosition(Partner partner) {
        MinMax<Double> minMax = new MinMax<Double>();
        minMax.add(Double.MAX_VALUE);
        for (PartnerPoint partnerPoint : getPointsOf(partner)) {
            minMax.add(computeDistanceToBasePosition(partnerPoint));
        }
        Double minDistance = minMax.min();
        return minDistance;
    }

    private Double computeDistanceToBasePosition(PartnerPoint partnerPoint) {
        return SphericalUtil.computeDistanceBetween(partnerPoint.getPosition(), getBasePosition());
    }

    private LatLng getBasePosition() {
        prepareBasePositionIfNeed();
        return basePosition;
    }

    private void prepareBasePositionIfNeed() {
        if (basePosition == null) {
            prepareBasePosition();
        } else if (serializableBasePosition == null) {
            serializableBasePosition = SerializableLatLng.fromParcelable(basePosition);
        }
    }

    private void prepareBasePosition() {
        if (serializableBasePosition != null) {
            basePosition = serializableBasePosition.toParcelable();
        } else {
            resetBasePosition();
        }
    }

    private void resetBasePosition() {
        basePosition = ActualBaseLocationProvider.getActualBasePosition();
        serializableBasePosition = SerializableLatLng.fromParcelable(basePosition);
        pairsPartnerIdAndMinDistance = new HashMap<Integer, Double>();
    }

    private static List<PartnerPoint> getPointsOf(Partner partner) {
        Context appContext = DroogCompaniiApplication.getContext();
        PartnerPointsReader partnerPointsReader = new PartnerPointsReader(appContext);
        return partnerPointsReader.getPartnerPointsOf(partner);
    }
}