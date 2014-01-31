package ru.droogcompanii.application.ui.activity.synchronization_activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import java.io.Serializable;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.ui.helpers.task.TaskFragmentHolder;
import ru.droogcompanii.application.util.VerifierDataForRelevance;

/**
 * Created by ls on 26.12.13.
 */
public class SynchronizationActivity extends ActionBarActivity implements TaskFragmentHolder.Callbacks {

    private boolean isFirstLaunched;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        isFirstLaunched = (savedInstanceState == null);

        setContentView(R.layout.activity_synchronization);
    }

    public boolean isFirstLaunched() {
        return isFirstLaunched;
    }

    @Override
    public void onTaskFinished(int resultCode, Serializable result) {
        if (resultCode == RESULT_OK) {
            VerifierDataForRelevance.setDataIsUpdated();
        }
        setResult(resultCode);
        finish();
    }
}
