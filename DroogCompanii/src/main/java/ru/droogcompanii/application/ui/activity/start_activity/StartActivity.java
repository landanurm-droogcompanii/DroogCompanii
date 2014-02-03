package ru.droogcompanii.application.ui.activity.start_activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.ui.activity.main_screen.MainScreen;
import ru.droogcompanii.application.ui.activity.synchronization_activity.SynchronizationActivity;
import ru.droogcompanii.application.ui.fragment.filter_fragment.FilterUtils;
import ru.droogcompanii.application.ui.helpers.YesNoDialogMaker;
import ru.droogcompanii.application.util.VerifierDataForRelevance;

/**
 * Created by ls on 31.01.14.
 */
public class StartActivity extends FragmentActivity {

    private static final int REQUEST_CODE_SYNCHRONIZATION = 11111;
    private static final int REQUEST_CODE_MAIN_SCREEN = 22222;

    private static final BooleanSharedPreference IS_MAIN_SCREEN_STARTED =
            BooleanSharedPreference.from("IS_MAIN_SCREEN_STARTED");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            onAppStarted();
        }

        if (IS_MAIN_SCREEN_STARTED.get()) {
            finish();
            return;
        }

        if (VerifierDataForRelevance.isDataUpdated()) {
            openMainScreen();
        } else {
            showUpdateDataDialog();
        }
    }

    private void onAppStarted() {
        IS_MAIN_SCREEN_STARTED.set(false);
        FilterUtils.resetFilters(this);
    }

    private void openMainScreen() {
        IS_MAIN_SCREEN_STARTED.set(true);
        startActivityForResult(REQUEST_CODE_MAIN_SCREEN, MainScreen.class);
    }

    private void startActivityForResult(int requestCode, Class<?> activityClass) {
        Intent intent = new Intent(this, activityClass);
        startActivityForResult(intent, requestCode);
    }

    private void showUpdateDataDialog() {
        DialogInterface.OnClickListener onYesListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startSynchronization();
            }
        };
        DialogInterface.OnClickListener onNoListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        };
        String message = getString(R.string.messageIfUserWantsToGetData).trim();
        YesNoDialogMaker dialogMaker = new YesNoDialogMaker(this);
        dialogMaker.make(message, onYesListener, onNoListener);
    }

    private void startSynchronization() {
        startActivityForResult(REQUEST_CODE_SYNCHRONIZATION, SynchronizationActivity.class);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_MAIN_SCREEN:
                finish();
                return;

            case REQUEST_CODE_SYNCHRONIZATION:
                if (resultCode == RESULT_OK) {
                    openMainScreen();
                } else {
                    finish();
                }
                return;
        }
    }
}
