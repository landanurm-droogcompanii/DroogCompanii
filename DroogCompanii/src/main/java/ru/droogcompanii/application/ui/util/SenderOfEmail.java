package ru.droogcompanii.application.ui.util;

import android.content.Context;
import android.content.Intent;

/**
 * Created by ls on 13.02.14.
 */
public class SenderOfEmail {
    private final Context context;

    public SenderOfEmail(Context context) {
        this.context = context;
    }

    public void sendTo(String email) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] { email });
        context.startActivity(Intent.createChooser(intent, ""));
    }
}
