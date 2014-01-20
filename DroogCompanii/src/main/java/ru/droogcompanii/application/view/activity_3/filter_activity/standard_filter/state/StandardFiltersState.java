package ru.droogcompanii.application.view.activity_3.filter_activity.standard_filter.state;

import android.content.SharedPreferences;

import java.io.Serializable;

/**
 * Created by ls on 20.01.14.
 */
public class StandardFiltersState implements Serializable {

    public static class Keys {
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
        defState.sortByDistance = false;
        defState.sortByDiscount = false;
        defState.cashlessPayments = false;
        defState.worksNow = false;
        defState.discountTypeBonus = true;
        defState.discountTypeDiscount = true;
        defState.discountTypeCashBack = true;
        return defState;
    }

    public boolean sortByDistance;
    public boolean sortByDiscount;
    public boolean cashlessPayments;
    public boolean worksNow;
    public boolean discountTypeBonus;
    public boolean discountTypeDiscount;
    public boolean discountTypeCashBack;

    public void saveInto(SharedPreferences.Editor editor) {
        editor.putBoolean(Keys.sortByDistance, sortByDistance);
        editor.putBoolean(Keys.sortByDiscount, sortByDiscount);
        editor.putBoolean(Keys.cashlessPayments, cashlessPayments);
        editor.putBoolean(Keys.worksNow, worksNow);
        editor.putBoolean(Keys.discountTypeBonus, discountTypeBonus);
        editor.putBoolean(Keys.discountTypeDiscount, discountTypeDiscount);
        editor.putBoolean(Keys.discountTypeCashBack, discountTypeCashBack);
    }

    public void restoreFrom(SharedPreferences sharedPreferences) {
        sortByDistance = sharedPreferences.getBoolean(Keys.sortByDistance, DEFAULT.sortByDistance);
        sortByDiscount = sharedPreferences.getBoolean(Keys.sortByDiscount, DEFAULT.sortByDiscount);
        cashlessPayments = sharedPreferences.getBoolean(Keys.cashlessPayments, DEFAULT.cashlessPayments);
        worksNow = sharedPreferences.getBoolean(Keys.worksNow, DEFAULT.worksNow);
        discountTypeBonus = sharedPreferences.getBoolean(Keys.discountTypeBonus, DEFAULT.discountTypeBonus);
        discountTypeDiscount = sharedPreferences.getBoolean(Keys.discountTypeDiscount, DEFAULT.discountTypeDiscount);
        discountTypeCashBack = sharedPreferences.getBoolean(Keys.discountTypeCashBack, DEFAULT.discountTypeCashBack);
    }
}
