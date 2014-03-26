package ru.droogcompanii.application.ui.activity.signin;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import java.io.Serializable;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.ui.fragment.signin.SignInFragment;
import ru.droogcompanii.application.ui.util.activity.ActionBarActivityWithUpButton;
import ru.droogcompanii.application.ui.util.able_to_start_task.TaskNotBeInterruptedDuringConfigurationChange;

public class SignInActivity extends ActionBarActivityWithUpButton {

    private static final String TAG_FRAGMENT = "SignInFragment";

    private static final int TASK_REQUEST_CODE_SIGNIN = 259;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        if (savedInstanceState == null) {
            placeSignInFragmentOnLayout();
        }
    }

    private void placeSignInFragmentOnLayout() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment = new SignInFragment();
        transaction.add(R.id.containerOfFragment, fragment, TAG_FRAGMENT);
        transaction.commit();
    }

    public void startSignInTask(String login, String password) {
        TaskNotBeInterruptedDuringConfigurationChange signInTask = new SignInTask(this, login, password);
        startTask(TASK_REQUEST_CODE_SIGNIN, signInTask, R.string.login_progress_signing_in);
    }

    @Override
    public void onTaskResult(int requestCode, int resultCode, Serializable result) {
        if (requestCode == TASK_REQUEST_CODE_SIGNIN) {
            onSignInCompleted(requestCode, resultCode, result);
        }
    }

    private void onSignInCompleted(int requestCode, int resultCode, Serializable result) {
        getSignInFragment().onTaskResult(requestCode, resultCode, result);
    }

    private SignInFragment getSignInFragment() {
        return (SignInFragment) getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT);
    }
}
