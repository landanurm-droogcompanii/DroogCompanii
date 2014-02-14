package ru.droogcompanii.application.ui.fragment.partner_details;

import android.content.Context;
import android.view.View;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.ui.util.IconAndLabelItemInflater;
import ru.droogcompanii.application.util.EmailSenderHelper;
import ru.droogcompanii.application.util.NavigationToWebsiteHelper;

/**
 * Created by ls on 13.02.14.
 */
public class ContactsViewHelper {

    private final EmailSenderHelper emailSenderHelper;
    private final NavigationToWebsiteHelper navigationToWebsiteHelper;
    private final IconAndLabelItemInflater inflater;

    public ContactsViewHelper(Context context) {
        navigationToWebsiteHelper = new NavigationToWebsiteHelper(context);
        emailSenderHelper = new EmailSenderHelper(context);
        inflater = new IconAndLabelItemInflater(context);
    }

    public View makeViewByWebSite(final String webSite) {
        return inflater.inflate(R.drawable.ic_web_site, webSite, new Runnable() {
            @Override
            public void run() {
                navigationToWebsiteHelper.navigateToSite(webSite);
            }
        });
    }

    public View makeViewByEmail(final String email) {
        return inflater.inflate(android.R.drawable.ic_dialog_email, email, new Runnable() {
            @Override
            public void run() {
                emailSenderHelper.sendEmailTo(email);
            }
        });
    }
}
