package ru.droogcompanii.application.ui.util.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by ls on 31.01.14.
 */
public class YesNoDialogMaker {

    private static final boolean DEFAULT_CANCELABLE = false;

    private final Context context;
    private final int titleOfYesButton;
    private final int titleOfNoButton;
    private boolean cancelable;

    public YesNoDialogMaker(Context context) {
        this(context, android.R.string.yes, android.R.string.no);
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
