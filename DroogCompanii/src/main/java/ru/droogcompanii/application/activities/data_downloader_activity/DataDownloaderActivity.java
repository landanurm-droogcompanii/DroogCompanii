package ru.droogcompanii.application.activities.data_downloader_activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.activities.helpers.task.TaskActivityMainFragment;
import ru.droogcompanii.application.activities.partner_category_list_activity.PartnerCategoryListActivity;

/**
 * Created by ls on 26.12.13.
 */
public class DataDownloaderActivity extends ActionBarActivity implements TaskActivityMainFragment.Callbacks {

    private static final int REQUEST_CODE = 2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_downloader_activity);
    }

    @Override
    public void onTaskFinished() {
        Intent intent = new Intent(this, PartnerCategoryListActivity.class);
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
