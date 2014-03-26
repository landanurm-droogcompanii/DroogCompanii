package ru.droogcompanii.application.ui.main_screen_2.filters_dialog.filters;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.data.working_hours.WeekWorkingHours;
import ru.droogcompanii.application.util.CalendarUtils;
import ru.droogcompanii.application.util.SerializationUtils;

/**
 * Created by ls on 25.03.14.
 */
class WorksNowFilterHelper implements FilterHelper, Serializable {

    private static class Key {
        public static final String ACTIVE = "ACTIVE" + WorksNowFilterHelper.class.getName();
    }

    private static class DefaultValue {
        public static final boolean ACTIVE = false;
    }

    private boolean active;

    public WorksNowFilterHelper() {
        active = DefaultValue.ACTIVE;
    }

    public boolean isActive() {
        return active;
    }

    @Override
    public Filter getFilter() {
        return new FilterImpl();
    }

    private class FilterImpl implements Filter {
        private final Calendar now = CalendarUtils.now();

        @Override
        public boolean isPassedThroughFilter(PartnerPoint partnerPoint, Cursor cursor) {
            return readWorkingHours(cursor).includes(now);
        }

        private WeekWorkingHours readWorkingHours(Cursor cursor) {
            int indexWorkingHoursColumn = cursor.getColumnIndexOrThrow(PARTNER_POINTS.COLUMN_NAME_WORKING_HOURS);
            byte[] workingHoursBlob = cursor.getBlob(indexWorkingHoursColumn);
            return (WeekWorkingHours) SerializationUtils.deserialize(workingHoursBlob);
        }
    }

    public Collection<String> getColumnsOfPartnerPoint() {
        return Arrays.asList(PARTNER_POINTS.COLUMN_NAME_WORKING_HOURS);
    }

    @Override
    public void readFrom(SharedPreferences sharedPreferences) {
        active = sharedPreferences.getBoolean(Key.ACTIVE, DefaultValue.ACTIVE);
    }

    @Override
    public void writeTo(SharedPreferences.Editor editor) {
        editor.putBoolean(Key.ACTIVE, active);
    }

    @Override
    public View inflateContentView(Context context) {
        return LayoutInflater.from(context).inflate(R.layout.view_filter_works_now, null);
    }

    @Override
    public void displayOn(View view) {
        CheckBox checkBox = (CheckBox) view.findViewById(R.id.worksNowCheckBox);
        checkBox.setChecked(active);
    }

    @Override
    public void readFrom(View view) {
        CheckBox checkBox = (CheckBox) view.findViewById(R.id.worksNowCheckBox);
        active = checkBox.isChecked();
    }
}
