package ru.droogcompanii.application.ui.helpers;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.PhoneNumberUtils;

/**
 * Created by ls on 31.01.14.
 */
public class Caller {

    private final Context context;

    public Caller(Context context) {
        this.context = context;
    }

    public void call(String phoneNumber) {
        String formattedPhone = PhoneNumberUtils.formatNumber(phoneNumber);
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + formattedPhone));
        context.startActivity(intent);
    }
}
