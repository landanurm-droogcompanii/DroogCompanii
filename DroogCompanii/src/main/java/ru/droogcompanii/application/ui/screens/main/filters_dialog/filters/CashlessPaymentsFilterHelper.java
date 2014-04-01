package ru.droogcompanii.application.ui.screens.main.filters_dialog.filters;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.util.CashlessPaymentsUtils;

/**
 * Created by ls on 25.03.14.
 */
class CashlessPaymentsFilterHelper implements FilterHelper, Serializable {

    private static class Key {
        public static final String ACTIVE = "ACTIVE" + CashlessPaymentsFilterHelper.class.getName();
    }

    private static class DefaultValue {
        public static final boolean ACTIVE = false;
    }

    private boolean active;

    public CashlessPaymentsFilterHelper() {
        active = DefaultValue.ACTIVE;
    }

    public boolean isActive() {
        return active;
    }

    public Collection<String> getColumnsOfPartnerPoint() {
        return Arrays.asList(
                PARTNER_POINTS.COLUMN_PAYMENT_METHODS
        );
    }

    @Override
    public Filter getFilter() {
        return new FilterImpl();
    }

    private static class FilterImpl implements Filter {
        @Override
        public boolean isPassedThroughFilter(PartnerPoint partnerPoint, Cursor cursor) {
            return CashlessPaymentsUtils.isSupportCashlessPayments(readPaymentMethods(cursor));
        }

        private String readPaymentMethods(Cursor cursor) {
            int indexPaymentMethods = cursor.getColumnIndexOrThrow(PARTNER_POINTS.COLUMN_PAYMENT_METHODS);
            return cursor.getString(indexPaymentMethods);
        }
    }

    @Override
    public void readFrom(SharedPreferences sharedPreferences) {
        active = sharedPreferences.getBoolean(Key.ACTIVE, DefaultValue.ACTIVE);
    }

    @Override
    public void writeTo(SharedPreferences.Editor editor) {
        editor.putBoolean(Key.ACTIVE, isActive());
    }

    @Override
    public View inflateContentView(Context context) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        return layoutInflater.inflate(R.layout.view_filter_cashless_payments, null);
    }

    @Override
    public void displayOn(View view) {
        CheckBox checkBox = (CheckBox) view.findViewById(R.id.cashlessPaymentsCheckBox);
        checkBox.setChecked(active);
    }

    @Override
    public void readFrom(View view) {
        CheckBox checkBox = (CheckBox) view.findViewById(R.id.cashlessPaymentsCheckBox);
        active = checkBox.isChecked();
    }

}
