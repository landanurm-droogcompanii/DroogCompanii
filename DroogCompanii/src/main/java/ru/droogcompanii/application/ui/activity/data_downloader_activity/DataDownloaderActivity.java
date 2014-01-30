package ru.droogcompanii.application.ui.activity.data_downloader_activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.ui.activity_3.main_screen.MainScreen;
import ru.droogcompanii.application.ui.helpers.task.TaskFragmentHolder;

/**
 * Created by ls on 26.12.13.
 */
public class DataDownloaderActivity extends ActionBarActivity implements TaskFragmentHolder.Callbacks {

    private static final Class<?> ACTIVITY_TO_START = MainScreen.class;
    private static final int REQUEST_CODE = 23;

    private boolean screenRotated;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        screenRotated = (savedInstanceState != null);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_downloader);
    }

    public boolean screenRotated() {
        return screenRotated;
    }

    @Override
    public void onTaskFinished(int resultCode, Intent resultIntent) {
        if (resultCode == RESULT_CANCELED) {
            finish();
            return;
        }
        Intent intent = new Intent(this, ACTIVITY_TO_START);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            finish();
        }
    }
}
