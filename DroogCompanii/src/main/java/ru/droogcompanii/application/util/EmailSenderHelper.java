package ru.droogcompanii.application.util;

import android.content.Context;
import android.content.Intent;

/**
 * Created by ls on 13.02.14.
 */
public class EmailSenderHelper {
    private final Context context;

    public EmailSenderHelper(Context context) {
        this.context = context;
    }

    public void sendEmailTo(String email) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] { email });
        context.startActivity(Intent.createChooser(intent, ""));
    }
}
