package ru.droogcompanii.application.util.view;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by ls on 05.03.14.
 */
public class ToastUtils {

    public static final ToastUtils SHORT = new ToastUtils(Toast.LENGTH_SHORT);
    public static final ToastUtils LONG = new ToastUtils(Toast.LENGTH_LONG);

    private final int duration;

    private ToastUtils(int duration) {
        this.duration = duration;
    }

    public void show(Context context, String message) {
        Toast.makeText(context, message, duration).show();
    }

    public void show(Context context, int messageId) {
        Toast.makeText(context, messageId, duration).show();
    }
}