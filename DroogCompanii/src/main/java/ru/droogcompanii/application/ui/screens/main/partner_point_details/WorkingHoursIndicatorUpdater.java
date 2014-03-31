package ru.droogcompanii.application.ui.screens.main.partner_point_details;

import android.view.View;

import ru.droogcompanii.application.R;

/**
 * Created by ls on 04.02.14.
 */
class WorkingHoursIndicatorUpdater {
    public static void update(View indicator, boolean opened) {
        if (opened) {
            indicator.setBackgroundResource(R.drawable.ic_working_hours_is_open);
        } else {
            indicator.setBackgroundResource(R.drawable.ic_working_hours_is_close);
        }
    }
}
