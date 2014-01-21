package ru.droogcompanii.application.view.fragment.filter_fragment.standard_filters.state;

import android.content.SharedPreferences;

import java.io.Serializable;

/**
 * Created by ls on 20.01.14.
 */
public class StandardFiltersState implements Serializable {

    public static class FilterKeys {
        public static final String sortByTitle = "sortByTitle";
        public static final String sortByDistance = "sortByDistance";
        public static final String sortByDiscount = "sortByDiscount";
        public static final String cashlessPayments = "cashlessPayments";
        public static final String worksNow = "worksNow";
        public static final String discountTypeBonus = "discountTypeBonus";
        public static final String discountTypeDiscount = "discountTypeDiscount";
        public static final String discountTypeCashBack = "discountTypeCashBack";
    }

    public static final StandardFiltersState DEFAULT = prepareDefaultState();

    private static StandardFiltersState prepareDefaultState() {
        StandardFiltersState defState = new StandardFiltersState();
        defState.sortByTitle = true;
        defState.sortByDistance = false;
        defState.sortByDiscount = false;
        defState.cashlessPayments = false;
        defState.worksNow = false;
        defState.discountTypeBonus = true;
        defState.discountTypeDiscount = true;
        defState.discountTypeCashBack = true;
        return defState;
    }

    public boolean sortByTitle;
    public boolean sortByDistance;
    public boolean sortByDiscount;
    public boolean cashlessPayments;
    public boolean worksNow;
    public boolean discountTypeBonus;
    public boolean discountTypeDiscount;
    public boolean discountTypeCashBack;

    public void saveInto(SharedPreferences.Editor editor) {
        editor.putBoolean(FilterKeys.sortByTitle, sortByTitle);
        editor.putBoolean(FilterKeys.sortByDistance, sortByDistance);
        editor.putBoolean(FilterKeys.sortByDiscount, sortByDiscount);
        editor.putBoolean(FilterKeys.cashlessPayments, cashlessPayments);
        editor.putBoolean(FilterKeys.worksNow, worksNow);
        editor.putBoolean(FilterKeys.discountTypeBonus, discountTypeBonus);
        editor.putBoolean(FilterKeys.discountTypeDiscount, discountTypeDiscount);
        editor.putBoolean(FilterKeys.discountTypeCashBack, discountTypeCashBack);
    }

    public void restoreFrom(SharedPreferences sharedPreferences) {
        sortByTitle = sharedPreferences.getBoolean(FilterKeys.sortByTitle, DEFAULT.sortByTitle);
        sortByDistance = sharedPreferences.getBoolean(FilterKeys.sortByDistance, DEFAULT.sortByDistance);
        sortByDiscount = sharedPreferences.getBoolean(FilterKeys.sortByDiscount, DEFAULT.sortByDiscount);
        cashlessPayments = sharedPreferences.getBoolean(FilterKeys.cashlessPayments, DEFAULT.cashlessPayments);
        worksNow = sharedPreferences.getBoolean(FilterKeys.worksNow, DEFAULT.worksNow);
        discountTypeBonus = sharedPreferences.getBoolean(FilterKeys.discountTypeBonus, DEFAULT.discountTypeBonus);
        discountTypeDiscount = sharedPreferences.getBoolean(FilterKeys.discountTypeDiscount, DEFAULT.discountTypeDiscount);
        discountTypeCashBack = sharedPreferences.getBoolean(FilterKeys.discountTypeCashBack, DEFAULT.discountTypeCashBack);
    }
}
