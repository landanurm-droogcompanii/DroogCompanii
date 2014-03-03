package ru.droogcompanii.application.ui.fragment.partner_details;

import android.content.Context;
import android.view.View;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.ui.util.SenderOfEmail;
import ru.droogcompanii.application.ui.util.IconAndLabelItemInflater;
import ru.droogcompanii.application.ui.util.NavigatorToWebsite;

/**
 * Created by ls on 13.02.14.
 */
public class ContactsViewMaker {

    private final SenderOfEmail senderOfEmail;
    private final NavigatorToWebsite navigatorToWebsite;
    private final IconAndLabelItemInflater inflater;

    public ContactsViewMaker(Context context) {
        navigatorToWebsite = new NavigatorToWebsite(context);
        senderOfEmail = new SenderOfEmail(context);
        inflater = new IconAndLabelItemInflater(context);
    }

    public View makeViewByWebSite(final String webSite) {
        return inflater.inflate(R.drawable.ic_web_site, webSite, new Runnable() {
            @Override
            public void run() {
                navigatorToWebsite.navigateToSite(webSite);
            }
        });
    }

    public View makeViewByEmail(final String email) {
        return inflater.inflate(android.R.drawable.ic_dialog_email, email, new Runnable() {
            @Override
            public void run() {
                senderOfEmail.sendTo(email);
            }
        });
    }
}
