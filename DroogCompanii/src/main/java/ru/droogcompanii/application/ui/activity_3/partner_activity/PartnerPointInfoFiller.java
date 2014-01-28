package ru.droogcompanii.application.ui.activity_3.partner_activity;

import android.view.View;
import android.widget.TextView;

import java.util.Calendar;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;

/**
 * Created by ls on 15.01.14.
 */
public class PartnerPointInfoFiller {
    public static void fill(View container, PartnerPoint partnerPoint) {
        TextView titleTextView = (TextView) container.findViewById(R.id.partnerPointTitleTextView);
        titleTextView.setText(partnerPoint.title);

        Calendar now = Calendar.getInstance();
        boolean openedNow = partnerPoint.workingHours.includes(now);
        if (openedNow) {
            container.findViewById(R.id.closedIndicator).setVisibility(View.INVISIBLE);
        } else {
            container.findViewById(R.id.openedIndicator).setVisibility(View.INVISIBLE);
        }
    }
}
