package ru.droogcompanii.application.ui.activity.signin;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import java.io.Serializable;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.ui.activity.able_to_start_task.ActivityAbleToStartTask;
import ru.droogcompanii.application.ui.fragment.signin.SignInFragment;
import ru.droogcompanii.application.ui.helpers.task.TaskNotBeInterrupted;

public class SignInActivity extends ActivityAbleToStartTask {

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
        TaskNotBeInterrupted signInTask = new SignInTask(this, login, password);
        startTask(TASK_REQUEST_CODE_SIGNIN, signInTask, R.string.login_progress_signing_in);
    }

    @Override
    protected void onReceiveResult(int requestCode, int resultCode, Serializable result) {
        if (requestCode == TASK_REQUEST_CODE_SIGNIN) {
            onSignInCompleted(resultCode, result);
        }
    }

    private void onSignInCompleted(int resultCode, Serializable result) {
        getSignInFragment().onTaskResult(resultCode, result);
    }

    private SignInFragment getSignInFragment() {
        return (SignInFragment) getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT);
    }
}
