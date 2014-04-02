package ru.droogcompanii.application.ui.screens.partner_details;

import android.content.Context;
import android.view.View;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.util.ui.view.IconAndLabelItemMaker;
import ru.droogcompanii.application.util.workers.EmailSender;
import ru.droogcompanii.application.util.workers.NavigatorToWebsite;

/**
 * Created by ls on 13.02.14.
 */
public class ContactsViewMaker {

    private final EmailSender emailSender;
    private final NavigatorToWebsite navigatorToWebsite;
    private final IconAndLabelItemMaker itemMaker;

    public ContactsViewMaker(Context context) {
        navigatorToWebsite = new NavigatorToWebsite(context);
        emailSender = new EmailSender(context);
        itemMaker = new IconAndLabelItemMaker(context);
    }

    public View makeViewByWebSite(final String webSite) {
        return itemMaker.make(R.drawable.ic_web_site, webSite, new Runnable() {
            @Override
            public void run() {
                navigatorToWebsite.navigateToSite(webSite);
            }
        });
    }

    public View makeViewByEmail(final String email) {
        return itemMaker.make(android.R.drawable.ic_dialog_email, email, new Runnable() {
            @Override
            public void run() {
                emailSender.sendTo(email);
            }
        });
    }
}
