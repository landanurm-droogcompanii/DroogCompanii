package ru.droogcompanii.application.view.activity_3.partner_activity;

import android.view.View;
import android.widget.TextView;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.data_structure.Partner;

/**
 * Created by ls on 15.01.14.
 */
public class PartnerInfoFiller {

    public static void fill(View container, Partner partner) {
        TextView partnerTitleTextView = (TextView) container.findViewById(R.id.partnerTitleTextView);
        partnerTitleTextView.setText(partner.title);
        TextView partnerDetailsTextView = (TextView) container.findViewById(R.id.partnerDetailsTextView);
        partnerDetailsTextView.setText(preparePartnerDetailsText(partner));
    }

    private static String preparePartnerDetailsText(Partner partner) {
        return partner.fullTitle + "\n" + partner.saleType;
    }
}