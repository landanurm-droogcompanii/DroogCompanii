package ru.droogcompanii.application.ui.fragment.partner_point_list;

import android.view.View;
import android.widget.TextView;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.ui.fragment.partner_points_info_panel.WorkingHoursIndicatorUpdater;
import ru.droogcompanii.application.util.CalendarUtils;

/**
 * Created by ls on 15.01.14.
 */
public class PartnerPointDetailsFiller {

    public static void fill(View container, PartnerPoint partnerPoint) {
        TextView titleTextView = (TextView) container.findViewById(R.id.partnerPointTitle);
        titleTextView.setText(partnerPoint.getTitle());

        View indicator = container.findViewById(R.id.indicator);
        WorkingHoursIndicatorUpdater.update(indicator, isOpenedNow(partnerPoint));
    }

    private static boolean isOpenedNow(PartnerPoint partnerPoint) {
        return partnerPoint.getWorkingHours().includes(CalendarUtils.now());
    }
}
