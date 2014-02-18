package ru.droogcompanii.application.ui.helpers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import ru.droogcompanii.application.R;

/**
 * Created by ls on 31.01.14.
 */
public class YesNoDialogMaker {

    private static final boolean DEFAULT_CANCELABLE = false;

    private final Context context;
    private final int titleOfYesButton;
    private final int titleOfNoButton;
    private boolean cancelable;

    public static YesNoDialogMaker createCancelable(Context context, int titleOfYesButton, int titleOfNoButton) {
        YesNoDialogMaker maker = new YesNoDialogMaker(context, titleOfYesButton, titleOfNoButton);
        maker.cancelable = true;
        return maker;
    }

    public YesNoDialogMaker(Context context) {
        this(context, R.string.yes, R.string.no);
    }

    public YesNoDialogMaker(Context context, int titleOfYesButton, int titleOfNoButton) {
        this.context = context;
        this.titleOfYesButton = titleOfYesButton;
        this.titleOfNoButton = titleOfNoButton;
        this.cancelable = DEFAULT_CANCELABLE;
    }

    public AlertDialog make(String message,
                            DialogInterface.OnClickListener onYesListener,
                            DialogInterface.OnClickListener onNoListener) {
        return new AlertDialog.Builder(context)
                .setMessage(message)
                .setCancelable(cancelable)
                .setPositiveButton(titleOfYesButton, onYesListener)
                .setNegativeButton(titleOfNoButton, onNoListener)
                .show();
    }

    public AlertDialog make(int messageId,
                            DialogInterface.OnClickListener onYesListener,
                            DialogInterface.OnClickListener onNoListener) {
        return new AlertDialog.Builder(context)
                .setMessage(messageId)
                .setCancelable(cancelable)
                .setPositiveButton(titleOfYesButton, onYesListener)
                .setNegativeButton(titleOfNoButton, onNoListener)
                .show();
    }
}
