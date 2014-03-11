package ru.droogcompanii.application.ui.activity.partner_list;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.ui.fragment.filter.standard.search_criteria_and_comparators.partner.PartnerComparatorByDiscountSize;
import ru.droogcompanii.application.ui.fragment.filter.standard.search_criteria_and_comparators.partner.PartnerComparatorByDistance;
import ru.droogcompanii.application.ui.fragment.filter.standard.search_criteria_and_comparators.partner.PartnerComparatorByTitle;
import ru.droogcompanii.application.ui.util.ActualBaseLocationProvider;

/**
 * Created by ls on 17.02.14.
 */
public class SpinnerAdapterPartnerImpl extends ArrayAdapter<String> {

    private static final List<SpinnerItem<Partner>> ITEMS = Arrays.asList(
            new SpinnerItem<Partner>(R.string.labelOfSortByTitleCheckBox,
                                     new PartnerComparatorByTitle()),
            new SpinnerItem<Partner>(R.string.labelOfSortByDiscountSizeCheckBox,
                                     new PartnerComparatorByDiscountSize()),
            new SpinnerItem<Partner>(R.string.labelOfSortByDistanceCheckBox,
                                     new PartnerComparatorByDistance(new ActualBaseLocationProvider()))
    );

    public static Comparator<Partner> getComparatorByPosition(int position) {
        return ITEMS.get(position).getComparator();
    }

    public SpinnerAdapterPartnerImpl(Context context) {
        super(context, android.R.layout.simple_list_item_1, getItemTitles(context));
    }

    private static List<String> getItemTitles(Context context) {
        List<String> itemTitles = new ArrayList<String>(ITEMS.size());
        for (SpinnerItem<Partner> each : ITEMS) {
            itemTitles.add(context.getString(each.getTitleId()));
        }
        return itemTitles;
    }
}
