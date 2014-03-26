package ru.droogcompanii.application.ui.fragment.partner_details;

import android.content.Context;
import android.view.View;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.ui.util.view.IconAndLabelItemMaker;
import ru.droogcompanii.application.ui.util.workers.SenderEmail;
import ru.droogcompanii.application.ui.util.workers.NavigatorToWebsite;

/**
 * Created by ls on 13.02.14.
 */
public class ContactsViewMaker {

    private final SenderEmail senderEmail;
    private final NavigatorToWebsite navigatorToWebsite;
    private final IconAndLabelItemMaker inflater;

    public ContactsViewMaker(Context context) {
        navigatorToWebsite = new NavigatorToWebsite(context);
        senderEmail = new SenderEmail(context);
        inflater = new IconAndLabelItemMaker(context);
    }

    public View makeViewByWebSite(final String webSite) {
        return inflater.make(R.drawable.ic_web_site, webSite, new Runnable() {
            @Override
            public void run() {
                navigatorToWebsite.navigateToSite(webSite);
            }
        });
    }

    public View makeViewByEmail(final String email) {
        return inflater.make(android.R.drawable.ic_dialog_email, email, new Runnable() {
            @Override
            public void run() {
                senderEmail.sendTo(email);
            }
        });
    }
}
