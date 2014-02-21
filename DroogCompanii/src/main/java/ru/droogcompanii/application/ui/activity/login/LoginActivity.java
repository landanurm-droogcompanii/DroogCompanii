package ru.droogcompanii.application.ui.activity.login;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.ui.activity.able_to_start_task.ActivityAbleToStartTask;
import ru.droogcompanii.application.ui.fragment.login.LoginFragment;

public class LoginActivity extends ActivityAbleToStartTask {

    private static final String TAG_FRAGMENT = "LoginFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (savedInstanceState == null) {
            placeLoginFragmentOnLayout();
        }
    }

    private void placeLoginFragmentOnLayout() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment = new LoginFragment();
        transaction.add(R.id.containerOfFragment, fragment, TAG_FRAGMENT);
        transaction.commit();
    }

    @Override
    protected String getTagOfTaskResultReceiverFragment() {
        return TAG_FRAGMENT;
    }
}
