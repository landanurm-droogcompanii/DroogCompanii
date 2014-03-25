package ru.droogcompanii.application.ui.main_screen_2.filters.filters_impl;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.util.CashlessPaymentsChecker;

/**
 * Created by ls on 25.03.14.
 */
public class CashlessPaymentsFilter implements Filter, Serializable {

    private static class Key {
        public static final String ACTIVATED = "ACTIVATED" + CashlessPaymentsFilter.class.getName();
    }

    private static class DefaultValue {
        public static final boolean ACTIVATED = false;
    }

    private boolean activated;

    public CashlessPaymentsFilter() {
        activated = DefaultValue.ACTIVATED;
    }

    public boolean isActivated() {
        return activated;
    }

    public Set<String> getColumnsOfPartnerPoint() {
        Set<String> columns = new HashSet<String>();
        columns.add(PARTNER_POINTS.COLUMN_NAME_PAYMENT_METHODS);
        return columns;
    }

    @Override
    public Checker getChecker() {
        if (!isActivated()) {
            return new DummyChecker(true);
        }
        return new Checker() {
            @Override
            public boolean isMeet(PartnerPoint partnerPoint, Cursor cursor) {
                return CashlessPaymentsChecker.isSupportCashlessPayments(readPaymentMethods(cursor));
            }

            private String readPaymentMethods(Cursor cursor) {
                int indexPaymentMethodsColumn = cursor.getColumnIndexOrThrow(PARTNER_POINTS.COLUMN_NAME_PAYMENT_METHODS);
                return cursor.getString(indexPaymentMethodsColumn);
            }
        };
    }

    @Override
    public void readFrom(SharedPreferences sharedPreferences) {
        activated = sharedPreferences.getBoolean(Key.ACTIVATED, DefaultValue.ACTIVATED);
    }

    @Override
    public void writeTo(SharedPreferences.Editor editor) {
        editor.putBoolean(Key.ACTIVATED, isActivated());
    }

    @Override
    public View inflateContentView(Context context) {
        return LayoutInflater.from(context).inflate(R.layout.view_filter_cashless_payments, null);
    }

    @Override
    public void displayOn(View view) {
        CheckBox checkBox = (CheckBox) view.findViewById(R.id.cashlessPaymentsCheckBox);
        checkBox.setChecked(activated);
    }

    @Override
    public void readFrom(View view) {
        CheckBox checkBox = (CheckBox) view.findViewById(R.id.cashlessPaymentsCheckBox);
        activated = checkBox.isChecked();
    }

}
