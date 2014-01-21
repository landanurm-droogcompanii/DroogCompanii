package ru.droogcompanii.application.view.fragment.filter_fragment.standard_filters.filters;

import android.content.SharedPreferences;
import android.view.View;
import android.widget.CheckBox;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.view.fragment.filter_fragment.Filters;
import ru.droogcompanii.application.view.fragment.filter_fragment.standard_filters.search_criteria_and_comparators.DiscountTypeSearchCriterion;

/**
 * Created by ls on 21.01.14.
 */
public class FilterSearchDiscountType implements ru.droogcompanii.application.view.fragment.filter_fragment.Filter {

    private static class KeysOfDiscountTypes {
        public static final String BONUS = getClassName() + "Bonus";
        public static final String DISCOUNT = getClassName() + "Discount";
        public static final String CASH_BACK = getClassName() + "Cash Back";
    }

    private static String getClassName() {
        return FilterSearchDiscountType.class.getSimpleName();
    }

    private static final boolean DEFAULT_BONUS = true;
    private static final boolean DEFAULT_DISCOUNT = true;
    private static final boolean DEFAULT_CASH_BACK = true;

    private boolean bonus;
    private boolean discount;
    private boolean cashBack;

    public FilterSearchDiscountType() {
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
        bonus = sharedPreferences.getBoolean(KeysOfDiscountTypes.BONUS, DEFAULT_BONUS);
        discount = sharedPreferences.getBoolean(KeysOfDiscountTypes.DISCOUNT, DEFAULT_DISCOUNT);
        cashBack = sharedPreferences.getBoolean(KeysOfDiscountTypes.CASH_BACK, DEFAULT_CASH_BACK);
    }

    @Override
    public void saveInto(SharedPreferences.Editor editor) {
        editor.putBoolean(KeysOfDiscountTypes.BONUS, bonus);
        editor.putBoolean(KeysOfDiscountTypes.DISCOUNT, discount);
        editor.putBoolean(KeysOfDiscountTypes.CASH_BACK, cashBack);
    }

    @Override
    public void includeIn(Filters filters) {
        filters.add(new DiscountTypeSearchCriterion(bonus, discount, cashBack));
    }
}
