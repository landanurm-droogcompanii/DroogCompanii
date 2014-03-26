package ru.droogcompanii.application.ui.util;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;

/**
 * Created by ls on 26.03.14.
 */
public class DroogCompaniiBaseActivity extends ActionBarActivity {

    public void refresh() {
        Intent intent = getIntent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        startActivity(intent);
    }

}
