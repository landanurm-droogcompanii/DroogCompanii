package ru.droogcompanii.application.ui.fragment.partner_fragment;

import android.view.View;
import android.widget.TextView;

import java.util.Calendar;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;

/**
 * Created by ls on 15.01.14.
 */
class PartnerPointInfoFiller {
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
