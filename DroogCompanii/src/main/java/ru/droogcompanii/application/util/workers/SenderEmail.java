package ru.droogcompanii.application.util.workers;

import android.content.Context;
import android.content.Intent;

/**
 * Created by ls on 13.02.14.
 */
public class SenderEmail {
    private final Context context;

    public SenderEmail(Context context) {
        this.context = context;
    }

    public void sendTo(String recipient) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] { recipient });
        context.startActivity(Intent.createChooser(intent, ""));
    }
}
