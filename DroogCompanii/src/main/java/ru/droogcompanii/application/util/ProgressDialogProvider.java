package ru.droogcompanii.application.util;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by Leonid on 17.12.13.
 */
public class ProgressDialogProvider {
    public static ProgressDialog prepareProgressDialog(Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(true);
        return progressDialog;
    }
}
