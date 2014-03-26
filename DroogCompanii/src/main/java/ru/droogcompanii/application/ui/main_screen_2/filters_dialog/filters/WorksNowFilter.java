package ru.droogcompanii.application.ui.main_screen_2.filters_dialog.filters;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.data.working_hours.WeekWorkingHours;
import ru.droogcompanii.application.util.CalendarUtils;
import ru.droogcompanii.application.util.SerializationUtils;

/**
 * Created by ls on 25.03.14.
 */
public class WorksNowFilter implements Filter, Serializable {

    private static class Key {
        public static final String ACTIVATED = "ACTIVATED" + WorksNowFilter.class.getName();
    }

    private static class DefaultValue {
        public static final boolean ACTIVATED = false;
    }

    private boolean activated;

    public WorksNowFilter() {
        activated = DefaultValue.ACTIVATED;
    }

    public boolean isActive() {
        return activated;
    }

    @Override
    public Checker getChecker() {
        if (!isActive()) {
            return new DummyChecker(true);
        }
        final Calendar now = CalendarUtils.now();
        return new Checker() {
            @Override
            public boolean isMeet(PartnerPoint partnerPoint, Cursor cursor) {
                return readWorkingHours(cursor).includes(now);
            }

            private WeekWorkingHours readWorkingHours(Cursor cursor) {
                int indexWorkingHoursColumn = cursor.getColumnIndexOrThrow(PARTNER_POINTS.COLUMN_NAME_WORKING_HOURS);
                byte[] workingHoursBlob = cursor.getBlob(indexWorkingHoursColumn);
                return (WeekWorkingHours) SerializationUtils.deserialize(workingHoursBlob);
            }
        };
    }

    public Set<String> getColumnsOfPartnerPoint() {
        Set<String> columns = new HashSet<String>();
        columns.add(PARTNER_POINTS.COLUMN_NAME_WORKING_HOURS);
        return columns;
    }

    @Override
    public void readFrom(SharedPreferences sharedPreferences) {
        activated = sharedPreferences.getBoolean(Key.ACTIVATED, DefaultValue.ACTIVATED);
    }

    @Override
    public void writeTo(SharedPreferences.Editor editor) {
        editor.putBoolean(Key.ACTIVATED, activated);
    }

    @Override
    public View inflateContentView(Context context) {
        return LayoutInflater.from(context).inflate(R.layout.view_filter_works_now, null);
    }

    @Override
    public void displayOn(View view) {
        CheckBox checkBox = (CheckBox) view.findViewById(R.id.worksNowCheckBox);
        checkBox.setChecked(activated);
    }

    @Override
    public void readFrom(View view) {
        CheckBox checkBox = (CheckBox) view.findViewById(R.id.worksNowCheckBox);
        activated = checkBox.isChecked();
    }
}
