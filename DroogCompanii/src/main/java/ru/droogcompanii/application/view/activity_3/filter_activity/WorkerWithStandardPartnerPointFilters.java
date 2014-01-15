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
        fill(context, viewOfFilters);
        return viewOfFilters;
    }

    private void fill(Context context, View viewOfFilters) {
        // TODO: may be need to read data from DB and init widgets on view
    }

    @Override
    public List<Filter<PartnerPoint>> readFilters(View viewOfFilters) {
        StandardFiltersReader filtersReader = new StandardFiltersReader(viewOfFilters);
        return filtersReader.read();
    }
}

class StandardFiltersReader implements Serializable {

    private final CheckBox sortByDistanceCheckBox;
    private final CheckBox sortBySaleTypeValueCheckBox;

    private final CheckBox cashlessPaymentsCheckBox;

    private final CheckBox worksNowCheckBox;

    private final CheckBox bonusCheckBox;
    private final CheckBox discountCheckBox;
    private final CheckBox cashBackCheckBox;


    StandardFiltersReader(View viewOfFilters) {
        sortByDistanceCheckBox = (CheckBox) viewOfFilters.findViewById(R.id.sortByDistanceCheckBox);
        sortBySaleTypeValueCheckBox = (CheckBox) viewOfFilters.findViewById(R.id.sortBySaleTypeValueCheckBox);

        cashlessPaymentsCheckBox = (CheckBox) viewOfFilters.findViewById(R.id.cashlessPaymentsCheckBox);

        worksNowCheckBox = (CheckBox) viewOfFilters.findViewById(R.id.worksNowCheckBox);

        bonusCheckBox = (CheckBox) viewOfFilters.findViewById(R.id.bonusCheckBox);
        discountCheckBox = (CheckBox) viewOfFilters.findViewById(R.id.discountCheckBox);
        cashBackCheckBox = (CheckBox) viewOfFilters.findViewById(R.id.cashBackCheckBox);
    }

    public List<Filter<PartnerPoint>> read() {
        List<Filter<PartnerPoint>> filters = new ArrayList<Filter<PartnerPoint>>();
        if (sortByDistanceCheckBox.isChecked()) {
            filters.add(new SortByDistanceFilter());
        }
        if (sortBySaleTypeValueCheckBox.isChecked()) {
            filters.add(new SortBySaleTypeValueFilter());
        }
        if (cashlessPaymentsCheckBox.isChecked()) {
            filters.add(new CashlessPaymentsFilter());
        }
        if (worksNowCheckBox.isChecked()) {
            filters.add(new WorksNowFilter());
        }
        filters.add(new SaleTypeFilter());
        return filters;
    }


    private class SortByDistanceFilter implements Serializable,
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


    private class SortBySaleTypeValueFilter implements Serializable,
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


    private class CashlessPaymentsFilter implements Serializable,
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


    private class WorksNowFilter implements Serializable,
            Filter<PartnerPoint>, SearchableSortable.SearchFilter<PartnerPoint> {
        @Override
        public void includeIn(SearchableSortable<PartnerPoint> searchableSortable) {
            searchableSortable.addSearchFilter(this);
        }

        @Override
        public boolean meetCriteria(PartnerPoint partnerPoint) {
            Calendar now = Calendar.getInstance();
            return partnerPoint.workingHours.includes(now);
        }
    }


    private class SaleTypeFilter implements Serializable,
            Filter<PartnerPoint>, SearchableSortable.SearchFilter<PartnerPoint> {
        private List<String> saleTypes;

        SaleTypeFilter() {
            saleTypes = new ArrayList<String>();
            if (bonusCheckBox.isChecked()) {
                saleTypes.add(DroogCompaniiStringConstants.saleType_Bonus);
            }
            if (discountCheckBox.isChecked()) {
                saleTypes.add(DroogCompaniiStringConstants.saleType_Discount);
            }
            if (cashBackCheckBox.isChecked()) {
                saleTypes.add(DroogCompaniiStringConstants.saleType_CashBack);
            }
        }

        @Override
        public void includeIn(SearchableSortable<PartnerPoint> searchableSortable) {
            searchableSortable.addSearchFilter(this);
        }

        @Override
        public boolean meetCriteria(PartnerPoint partnerPoint) {
            for (String saleType : saleTypes) {
                StringComparator discountType = new StringComparator(getSaleTypes(partnerPoint));
                if (discountType.containsIngnoreCase(saleType)) {
                    return true;
                }
            }
            return false;
        }

        private String getSaleTypes(PartnerPoint partnerPoint) {
            // TODO:
            return "";
        }
    }
}

class StringComparator {

    private final String str;

    public StringComparator(String str) {
        this.str = str;
    }

    public boolean containsIngnoreCase(String other) {
        return str.toLowerCase().contains(other.toLowerCase());
    }
}