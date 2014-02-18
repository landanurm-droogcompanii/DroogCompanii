package ru.droogcompanii.application.ui.fragment.filter.standard.filter_impl;

import android.content.SharedPreferences;
import android.view.View;
import android.widget.CheckBox;

import java.io.Serializable;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.ui.fragment.filter.Filter;
import ru.droogcompanii.application.ui.fragment.filter.FilterSet;
import ru.droogcompanii.application.ui.fragment.filter.standard.search_criteria_and_comparators.partner.PartnerSearchCriterionByDiscountType;
import ru.droogcompanii.application.ui.fragment.filter.standard.search_criteria_and_comparators.partner_point.PartnerPointSearchCriterionByDiscountType;
import ru.droogcompanii.application.util.Objects;

/**
 * Created by ls on 21.01.14.
 */
class SearchByDiscountTypeFilter implements Filter, Serializable {

    private static class KeyOfDiscountType {
        public static final String BONUS = "Bonus" + getClassName();
        public static final String DISCOUNT = "Discount" + getClassName();
        public static final String CASH_BACK = "Cash Back" + getClassName();
    }

    private static String getClassName() {
        return SearchByDiscountTypeFilter.class.getName();
    }

    private static final boolean DEFAULT_BONUS = true;
    private static final boolean DEFAULT_DISCOUNT = true;
    private static final boolean DEFAULT_CASH_BACK = true;

    private boolean bonus;
    private boolean discount;
    private boolean cashBack;

    public SearchByDiscountTypeFilter() {
        bonus = DEFAULT_BONUS;
        discount = DEFAULT_DISCOUNT;
        cashBack = DEFAULT_CASH_BACK;
    }

    @Override
    public void readFrom(View view) {
        bonus = findCheckBox(view, R.id.bonusCheckBox).isChecked();
        discount = findCheckBox(view, R.id.discountCheckBox).isChecked();
        cashBack = findCheckBox(view, R.id.cashBackCheckBox).isChecked();
    }

    @Override
    public void displayOn(View view) {
        findCheckBox(view, R.id.bonusCheckBox).setChecked(bonus);
        findCheckBox(view, R.id.discountCheckBox).setChecked(discount);
        findCheckBox(view, R.id.cashBackCheckBox).setChecked(cashBack);
    }

    private CheckBox findCheckBox(View view, int idOfCheckBox) {
        return (CheckBox) view.findViewById(idOfCheckBox);
    }

    @Override
    public void restoreFrom(SharedPreferences sharedPreferences) {
        bonus = sharedPreferences.getBoolean(KeyOfDiscountType.BONUS, DEFAULT_BONUS);
        discount = sharedPreferences.getBoolean(KeyOfDiscountType.DISCOUNT, DEFAULT_DISCOUNT);
        cashBack = sharedPreferences.getBoolean(KeyOfDiscountType.CASH_BACK, DEFAULT_CASH_BACK);
    }

    @Override
    public void saveInto(SharedPreferences.Editor editor) {
        editor.putBoolean(KeyOfDiscountType.BONUS, bonus);
        editor.putBoolean(KeyOfDiscountType.DISCOUNT, discount);
        editor.putBoolean(KeyOfDiscountType.CASH_BACK, cashBack);
    }

    @Override
    public void includeIn(FilterSet filterSet) {
        filterSet.addPartnerPointSearchCriterion(
                new PartnerPointSearchCriterionByDiscountType(bonus, discount, cashBack));
        filterSet.addPartnerSearchCriterion(
                new PartnerSearchCriterionByDiscountType(bonus, discount, cashBack));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        SearchByDiscountTypeFilter other = (SearchByDiscountTypeFilter) obj;
        return ((bonus == other.bonus) &&
                (discount == other.discount) &&
                (cashBack == other.cashBack));
    }

    @Override
    public int hashCode() {
        return Objects.hash(bonus, discount, cashBack);
    }

    @Override
    public String toString() {
        return getClass().getName() + ": bonus(" + bonus + "), " +
                "discount(" + discount + "), cashBack(" + cashBack + ")";
    }
}
