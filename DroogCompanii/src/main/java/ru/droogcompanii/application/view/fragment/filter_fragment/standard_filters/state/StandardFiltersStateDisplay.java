package ru.droogcompanii.application.view.fragment.filter_fragment.standard_filters.state;

import android.view.View;
import android.widget.CheckBox;

import ru.droogcompanii.application.R;

/**
 * Created by ls on 20.01.14.
 */
public class StandardFiltersStateDisplay {
    private final View viewOfFilters;

    public StandardFiltersStateDisplay(View viewOfFilters) {
        this.viewOfFilters = viewOfFilters;
    }

    public void display(StandardFiltersState state) {
        setCheckBox(R.id.sortByTitleCheckBox, state.sortByTitle);
        setCheckBox(R.id.sortByDistanceCheckBox, state.sortByDistance);
        setCheckBox(R.id.sortByDiscountCheckBox, state.sortByDiscount);
        setCheckBox(R.id.cashlessPaymentsCheckBox, state.cashlessPayments);
        setCheckBox(R.id.worksNowCheckBox, state.worksNow);
        setCheckBox(R.id.bonusCheckBox, state.discountTypeBonus);
        setCheckBox(R.id.discountCheckBox, state.discountTypeDiscount);
        setCheckBox(R.id.cashBackCheckBox, state.discountTypeCashBack);
    }

    private void setCheckBox(int idOfCheckBox, boolean needToMark) {
        CheckBox checkBox = (CheckBox) viewOfFilters.findViewById(idOfCheckBox);
        checkBox.setChecked(needToMark);
    }
}
