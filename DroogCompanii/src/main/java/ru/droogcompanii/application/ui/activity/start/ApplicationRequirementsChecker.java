package ru.droogcompanii.application.ui.activity.start;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

/**
 * Created by ls on 11.02.14.
 */
public class ApplicationRequirementsChecker {

    public static boolean areRequirementsSatisfied(final Activity activity) {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);
        if (status == ConnectionResult.SUCCESS) {
            return true;
        } else {
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, activity, 10);
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    activity.finish();
                }
            });
            dialog.show();
            return false;
        }
    }
}
