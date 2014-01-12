package ru.droogcompanii.application.activity.data_downloader_activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.activity_2.partner_category_list_activity.PartnerCategoryListActivity2;
import ru.droogcompanii.application.activity.helpers.task.TaskActivityMainFragment;

/**
 * Created by ls on 26.12.13.
 */
public class DataDownloaderActivity extends ActionBarActivity implements TaskActivityMainFragment.Callbacks {

    private static final Class<?> ACTIVITY_TO_START = PartnerCategoryListActivity2.class;
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
    public void onTaskFinished() {
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
