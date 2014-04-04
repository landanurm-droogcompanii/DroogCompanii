package ru.droogcompanii.application.ui.screens.synchronization;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.util.ui.activity.DroogCompaniiBaseActivity;

/**
 * Created by ls on 26.12.13.
 */
public class SynchronizationActivity extends DroogCompaniiBaseActivity
        implements SynchronizationFragment.Callbacks {

    public static final int REQUEST_CODE = 157;

    private static class Tag {
        public static final String SYNCHRONIZATION_FRAGMENT = "SYNCHRONIZATION_FRAGMENT";
    }


    public static void startForResult(Activity activity) {
        Intent intent = new Intent(activity, SynchronizationActivity.class);
        activity.startActivityForResult(intent, SynchronizationActivity.REQUEST_CODE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_synchronization);

        if (savedInstanceState == null) {
            placeFragmentOnLayout();
        }
    }

    private void placeFragmentOnLayout() {
        SynchronizationFragment fragment = new SynchronizationFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragmentContainer, fragment, Tag.SYNCHRONIZATION_FRAGMENT);
        transaction.commit();
    }

    @Override
    public void setResultAndFinish(int resultCode) {
        setResult(resultCode);
        finish();
    }

}
