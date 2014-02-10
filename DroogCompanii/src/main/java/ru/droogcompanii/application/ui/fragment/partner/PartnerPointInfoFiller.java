package ru.droogcompanii.application.ui.fragment.partner;

import android.view.View;
import android.widget.TextView;

import java.util.Calendar;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.ui.fragment.partner_points_info_panel.WorkingHoursIndicatorUpdater;

/**
 * Created by ls on 15.01.14.
 */
class PartnerPointInfoFiller {
    public static void fill(View container, PartnerPoint partnerPoint) {
        TextView titleTextView = (TextView) container.findViewById(R.id.partnerPointTitleTextView);
        titleTextView.setText(partnerPoint.title);

        Calendar now = Calendar.getInstance();
        boolean openedNow = partnerPoint.workingHours.includes(now);
        View indicator = container.findViewById(R.id.indicator);
        WorkingHoursIndicatorUpdater.update(indicator, openedNow);
    }
}
