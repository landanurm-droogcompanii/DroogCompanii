package ru.droogcompanii.application.view.activity_3.filter_activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.SearchableSortable;
import ru.droogcompanii.application.data.data_structure.PartnerPoint;
import ru.droogcompanii.application.util.DroogCompaniiStringConstants;
import ru.droogcompanii.application.view.activity_3.filter_activity.filter.Filter;
import ru.droogcompanii.application.view.activity_3.filter_activity.filter.WorkerWithFilters;

/**
 * Created by ls on 15.01.14.
 */
public class WorkerWithStandardPartnerPointFilters implements WorkerWithFilters<PartnerPoint>, Serializable {

    @Override
    public View prepareViewOfFilters(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewOfFilters = inflater.inflate(R.layout.view_standard_filters, null, false);
        init(context, viewOfFilters);
        return viewOfFilters;
    }

    private void init(Context context, View viewOfFilters) {
        // TODO: may be need to read data from DB and init widgets on view
    }

    @Override
    public List<Filter<PartnerPoint>> readFilters(View viewOfFilters) {
        StandardFiltersReader filtersReader = new StandardFiltersReader(viewOfFilters);
        return filtersReader.read();
    }
}

class StandardFiltersReader {

    private final View viewOfFilters;

    private final boolean sortByDistance;
    private final boolean sortBySaleTypeValue;

    private final boolean cashlessPayments;

    private final boolean worksNow;

    private final boolean saleTypeBonus;
    private final boolean saleTypeDiscount;
    private final boolean saleTypeCashBack;


    StandardFiltersReader(View viewOfFilters) {
        this.viewOfFilters = viewOfFilters;

        sortByDistance = checkBoxIsMarked(R.id.sortByDistanceCheckBox);
        sortBySaleTypeValue = checkBoxIsMarked(R.id.sortBySaleTypeValueCheckBox);

        cashlessPayments = checkBoxIsMarked(R.id.cashlessPaymentsCheckBox);

        worksNow = checkBoxIsMarked(R.id.worksNowCheckBox);

        saleTypeBonus = checkBoxIsMarked(R.id.bonusCheckBox);
        saleTypeDiscount = checkBoxIsMarked(R.id.discountCheckBox);
        saleTypeCashBack = checkBoxIsMarked(R.id.cashBackCheckBox);
    }

    private boolean checkBoxIsMarked(int idOfCheckBox) {
        CheckBox checkBox = (CheckBox) viewOfFilters.findViewById(idOfCheckBox);
        return checkBox.isChecked();
    }

    public List<Filter<PartnerPoint>> read() {
        List<Filter<PartnerPoint>> filters = new ArrayList<Filter<PartnerPoint>>();
        if (sortByDistance) {
            filters.add(new SortByDistanceFilter());
        }
        if (sortBySaleTypeValue) {
            filters.add(new SortBySaleTypeValueFilter());
        }
        if (cashlessPayments) {
            filters.add(new CashlessPaymentsFilter());
        }
        if (worksNow) {
            filters.add(new WorksNowFilter());
        }
        SaleTypeFilter saleTypeFilter = new SaleTypeFilter(saleTypeBonus, saleTypeDiscount, saleTypeCashBack);
        filters.add(saleTypeFilter);
        return filters;
    }


    private static class SortByDistanceFilter implements Serializable,
            Filter<PartnerPoint>, Comparator<PartnerPoint> {
        @Override
        public void includeIn(SearchableSortable<PartnerPoint> searchableSortable) {
            searchableSortable.addComparator(this);
        }

        @Override
        public int compare(PartnerPoint partnerPoint1, PartnerPoint partnerPoint2) {
            // TODO:
            throw new RuntimeException("Not Implemented Yet");
        }
    }


    private static class SortBySaleTypeValueFilter implements Serializable,
            Filter<PartnerPoint>, Comparator<PartnerPoint> {
        @Override
        public void includeIn(SearchableSortable<PartnerPoint> searchableSortable) {
            searchableSortable.addComparator(this);
        }

        @Override
        public int compare(PartnerPoint partnerPoint, PartnerPoint partnerPoint2) {
            // TODO:
            throw new RuntimeException("Not Implemented Yet");
        }
    }


    private static class CashlessPaymentsFilter implements Serializable,
            Filter<PartnerPoint>, SearchableSortable.SearchFilter<PartnerPoint> {
        @Override
        public void includeIn(SearchableSortable<PartnerPoint> searchableSortable) {
            searchableSortable.addSearchFilter(this);
        }

        @Override
        public boolean meetCriteria(PartnerPoint partnerPoint) {
            return partnerPoint.paymentMethods.contains(DroogCompaniiStringConstants.cashlessPayments);
        }
    }


    private static class WorksForSomeTimeFilter implements Serializable,
            Filter<PartnerPoint>, SearchableSortable.SearchFilter<PartnerPoint> {

        private Calendar time;

        public WorksForSomeTimeFilter(Calendar time) {
            this.time = time;
        }

        @Override
        public void includeIn(SearchableSortable<PartnerPoint> searchableSortable) {
            searchableSortable.addSearchFilter(this);
        }

        @Override
        public boolean meetCriteria(PartnerPoint partnerPoint) {
            return partnerPoint.workingHours.includes(time);
        }
    }

    private static class WorksNowFilter extends WorksForSomeTimeFilter {
        public WorksNowFilter() {
            super(Calendar.getInstance());
        }
    }


    private static class SaleTypeFilter implements Serializable,
            Filter<PartnerPoint>, SearchableSortable.SearchFilter<PartnerPoint> {
        private List<String> saleTypes;

        SaleTypeFilter(boolean saleTypeBonus, boolean saleTypeDiscount, boolean saleTypeCashBack) {
            saleTypes = new ArrayList<String>();
            if (saleTypeBonus) {
                saleTypes.add(DroogCompaniiStringConstants.saleType_Bonus);
            }
            if (saleTypeDiscount) {
                saleTypes.add(DroogCompaniiStringConstants.saleType_Discount);
            }
            if (saleTypeCashBack) {
                saleTypes.add(DroogCompaniiStringConstants.saleType_CashBack);
            }
        }

        @Override
        public void includeIn(SearchableSortable<PartnerPoint> searchableSortable) {
            searchableSortable.addSearchFilter(this);
        }

        @Override
        public boolean meetCriteria(PartnerPoint partnerPoint) {
            MoreComparableString partnerPointSaleTypes = new MoreComparableString(saleTypesOf(partnerPoint));
            for (String saleType : saleTypes) {
                if (partnerPointSaleTypes.containsIgnoreCase(saleType)) {
                    return true;
                }
            }
            return false;
        }

        private String saleTypesOf(PartnerPoint partnerPoint) {
            // TODO:
            return "";
        }
    }
}

class MoreComparableString implements Serializable {

    private final String str;

    public MoreComparableString(String str) {
        this.str = str;
    }

    public boolean containsIgnoreCase(String other) {
        return str.toLowerCase().contains(other.toLowerCase());
    }
}