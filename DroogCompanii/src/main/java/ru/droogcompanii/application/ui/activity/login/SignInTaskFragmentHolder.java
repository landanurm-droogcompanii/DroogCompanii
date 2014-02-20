package ru.droogcompanii.application.ui.activity.login;

import android.app.Activity;
import android.os.Bundle;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.ui.helpers.task.Task;
import ru.droogcompanii.application.ui.helpers.task.TaskFragmentHolder;

/**
 * Created by ls on 20.02.14.
 */
public class SignInTaskFragmentHolder extends TaskFragmentHolder {
    private String login;
    private String password;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Bundle args = getArguments();
        login = args.getString(LoginActivity.KEY_LOGIN);
        password = args.getString(LoginActivity.KEY_PASSWORD);
    }

    @Override
    protected Integer getTaskDialogTitleId() {
        return R.string.login_progress_signing_in;
    }

    @Override
    protected Task prepareTask() {
        LoginActivity activity = (LoginActivity) getActivity();
        return new SignInTask(activity, login, password);
    }
}
