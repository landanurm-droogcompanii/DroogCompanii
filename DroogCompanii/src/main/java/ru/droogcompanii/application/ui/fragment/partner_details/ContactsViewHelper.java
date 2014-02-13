package ru.droogcompanii.application.ui.fragment.partner_details;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.util.EmailSenderHelper;
import ru.droogcompanii.application.util.NavigationToWebsiteHelper;

/**
 * Created by ls on 13.02.14.
 */
public class ContactsViewHelper {

    private final EmailSenderHelper emailSenderHelper;
    private final NavigationToWebsiteHelper navigationToWebsiteHelper;
    private final LayoutInflater layoutInflater;

    public ContactsViewHelper(Context context) {
        navigationToWebsiteHelper = new NavigationToWebsiteHelper(context);
        emailSenderHelper = new EmailSenderHelper(context);
        layoutInflater = LayoutInflater.from(context);
    }

    public View makeViewByWebSite(final String webSite) {
        return makeView(R.drawable.ic_web_site, webSite, new Runnable() {
            @Override
            public void run() {
                navigationToWebsiteHelper.navigateToSite(webSite);
            }
        });
    }

    private View makeView(int iconId, String text, final Runnable runnableOnClick) {
        View view = layoutInflater.inflate(R.layout.view_contacts_row, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        imageView.setImageResource(iconId);
        TextView textView = (TextView) view.findViewById(R.id.textView);
        textView.setText(text);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runnableOnClick.run();
            }
        });
        return view;
    }

    public View makeViewByEmail(final String email) {
        return makeView(android.R.drawable.ic_dialog_email, email, new Runnable() {
            @Override
            public void run() {
                emailSenderHelper.sendEmailTo(email);
            }
        });
    }
}
