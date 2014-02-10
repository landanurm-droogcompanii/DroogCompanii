package ru.droogcompanii.application.ui.activity.synchronization;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import java.io.Serializable;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.global_flags.FlagNeedToUpdateMap;
import ru.droogcompanii.application.ui.helpers.task.TaskFragmentHolder;
import ru.droogcompanii.application.global_flags.VerifierDataForRelevance;

/**
 * Created by ls on 26.12.13.
 */
public class SynchronizationActivity extends ActionBarActivity implements TaskFragmentHolder.Callbacks {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_synchronization);
    }

    @Override
    public void onTaskFinished(int resultCode, Serializable result) {
        if (resultCode == RESULT_OK) {
            onTaskFinishedSuccessfully();
        }
        setResult(resultCode);
        finish();
    }

    private void onTaskFinishedSuccessfully() {
        VerifierDataForRelevance.setDataIsUpdated();
        FlagNeedToUpdateMap.set(true);
    }
}
