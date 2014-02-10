package ru.droogcompanii.application.ui.fragment.partner_points_info_panel;

import android.view.View;

import ru.droogcompanii.application.R;

/**
 * Created by ls on 04.02.14.
 */
public class WorkingHoursIndicatorUpdater {
    public static void update(View indicator, boolean opened) {
        if (opened) {
            indicator.setBackgroundResource(R.drawable.gradient_open_indicator);
        } else {
            indicator.setBackgroundResource(R.drawable.gradient_close_indicator);
        }
    }
}
