package ru.droogcompanii.application.ui.screens.personal_account.signin;

import android.content.Context;

import java.io.Serializable;

import ru.droogcompanii.application.util.ui.able_to_start_task.TaskNotBeInterruptedDuringConfigurationChange;

/**
 * Created by ls on 20.02.14.
 */
public class SignInTask extends TaskNotBeInterruptedDuringConfigurationChange {

    private final Context context;
    private final String login;
    private final String password;

    public SignInTask(Context context, String login, String password) {
        this.context = context;
        this.login = login;
        this.password = password;
    }

    @Override
    protected Serializable doInBackground(Void... voids) {
        Authenticator authenticator = new Authenticator(context, login, password);
        return authenticator.authenticate();
    }

}
