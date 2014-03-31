package ru.droogcompanii.application.ui.screens.partner_list;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import ru.droogcompanii.application.DroogCompaniiApplication;
import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.db_util.hierarchy_of_partners.PartnerPointsReader;
import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.util.MinMax;
import ru.droogcompanii.application.util.location.ActualBaseLocationProvider;

/**
 * Created by ls on 17.02.14.
 */
class SpinnerAdapterImpl extends ArrayAdapter<String> {

    private static final List<SpinnerItem<Partner>> ITEMS = Arrays.asList(
            new SpinnerItem<Partner>(R.string.labelOfSortByTitleCheckBox,
                                     new ComparatorByTitle()),
            new SpinnerItem<Partner>(R.string.labelOfSortByDiscountSizeCheckBox,
                                     new ComparatorByDiscountSize()),
            new SpinnerItem<Partner>(R.string.labelOfSortByDistanceCheckBox,
                                     new ComparatorByDistance())
    );

    public static Comparator<Partner> getComparatorByPosition(int position) {
        return ITEMS.get(position).getComparator();
    }

    public SpinnerAdapterImpl(Context context) {
        super(context, android.R.layout.simple_list_item_1, getItemTitles(context));
    }

    private static List<String> getItemTitles(Context context) {
        List<String> itemTitles = new ArrayList<String>(ITEMS.size());
        for (SpinnerItem<Partner> each : ITEMS) {
            itemTitles.add(context.getString(each.getTitleId()));
        }
        return itemTitles;
    }


    static class ComparatorByDiscountSize implements Comparator<Partner>, Serializable {
        @Override
        public int compare(Partner partner1, Partner partner2) {
            Integer discountSize1 = partner1.getDiscountSize();
            Integer discountSize2 = partner2.getDiscountSize();
            return discountSize1.compareTo(discountSize2);
        }
    }


    static class ComparatorByDistance implements Comparator<Partner>, Serializable {

        @Override
        public int compare(Partner partner1, Partner partner2) {
            LatLng basePosition = ActualBaseLocationProvider.getPositionOfActualBaseLocation();
            Double d1 = minDistanceFromPartnerToBasePosition(partner1, basePosition);
            Double d2 = minDistanceFromPartnerToBasePosition(partner2, basePosition);
            return d1.compareTo(d2);
        }

        private static Double minDistanceFromPartnerToBasePosition(Partner partner, LatLng basePosition) {
            MinMax<Double> minMax = new MinMax<Double>();
            minMax.add(Double.MAX_VALUE);
            for (PartnerPoint partnerPoint : getPointsOf(partner)) {
                double distance = SphericalUtil.computeDistanceBetween(partnerPoint.getPosition(), basePosition);
                minMax.add(distance);
            }
            return minMax.min();
        }

        private static List<PartnerPoint> getPointsOf(Partner partner) {
            Context context = DroogCompaniiApplication.getContext();
            PartnerPointsReader partnerPointsReader = new PartnerPointsReader(context);
            return partnerPointsReader.getPartnerPointsOf(partner);
        }
    }


    static class ComparatorByTitle implements Comparator<Partner>, Serializable {
        @Override
        public int compare(Partner partner1, Partner partner2) {
            String title1 = partner1.getTitle();
            String title2 = partner2.getTitle();
            return title1.compareToIgnoreCase(title2);
        }
    }
}
