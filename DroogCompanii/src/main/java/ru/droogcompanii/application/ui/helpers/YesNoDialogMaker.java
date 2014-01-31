package ru.droogcompanii.application.ui.helpers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import ru.droogcompanii.application.R;

/**
 * Created by ls on 31.01.14.
 */
public class YesNoDialogMaker {

    private final Context context;

    public YesNoDialogMaker(Context context) {
        this.context = context;
    }

    public AlertDialog make(String message,
                            DialogInterface.OnClickListener onYesListener,
                            DialogInterface.OnClickListener onNoListener) {
        return new AlertDialog.Builder(context)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(getString(R.string.yes), onYesListener)
                .setNegativeButton(getString(R.string.no), onNoListener)
                .show();
    }

    private String getString(int resId) {
        return context.getString(resId);
    }
}
