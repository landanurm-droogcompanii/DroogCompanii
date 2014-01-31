package ru.droogcompanii.application.ui.activity.synchronization_activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import java.io.Serializable;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.ui.activity.main_screen.MainScreen;
import ru.droogcompanii.application.ui.helpers.task.TaskFragmentHolder;
import ru.droogcompanii.application.util.VerifierDataForRelevance;

/**
 * Created by ls on 26.12.13.
 */
public class SynchronizationActivity extends ActionBarActivity implements TaskFragmentHolder.Callbacks {

    private static final Class<?> ACTIVITY_TO_START = MainScreen.class;
    private static final int REQUEST_CODE = 23;

    private boolean screenRotated;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        screenRotated = (savedInstanceState != null);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_synchronization);
    }

    public boolean screenRotated() {
        return screenRotated;
    }

    @Override
    public void onTaskFinished(int resultCode, Serializable result) {
        if (resultCode == RESULT_OK) {
            VerifierDataForRelevance.setDataIsUpdated();
        }
        setResult(resultCode);
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            finish();
        }
    }
}
