package ru.droogcompanii.application.ui.screens.partner_list;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.ui.screens.partner_list.comparators.ComparatorByDiscountSize;
import ru.droogcompanii.application.ui.screens.partner_list.comparators.ComparatorByDistance;
import ru.droogcompanii.application.ui.screens.partner_list.comparators.ComparatorByTitle;

/**
 * Created by ls on 17.02.14.
 */
class SpinnerAdapterImpl extends ArrayAdapter<String> {

    public static interface ComparatorProvider<T> {
        Comparator<T> get();
    }


    private static final List<SpinnerItem<Partner>> ITEMS = Arrays.asList(
            new SpinnerItem<Partner>(R.string.labelOfSortByTitleCheckBox,
                                     new ComparatorProvider<Partner>() {
                                         @Override
                                         public Comparator<Partner> get() {
                                             return new ComparatorByTitle();
                                         }
                                     }),
            new SpinnerItem<Partner>(R.string.labelOfSortByDiscountSizeCheckBox,
                                     new ComparatorProvider<Partner>() {
                                         @Override
                                         public Comparator<Partner> get() {
                                             return new ComparatorByDiscountSize();
                                         }
                                     }),
            new SpinnerItem<Partner>(R.string.labelOfSortByDistanceCheckBox,
                                     new ComparatorProvider<Partner>() {
                                         @Override
                                         public Comparator<Partner> get() {
                                             return new ComparatorByDistance();
                                         }
                                     })
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
}
