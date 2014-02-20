package ru.droogcompanii.application.ui.activity.login;

import java.io.Serializable;

import ru.droogcompanii.application.ui.helpers.task.Task;
import ru.droogcompanii.application.util.Holder;
import ru.droogcompanii.application.util.Snorlax;
import ru.droogcompanii.application.util.WeakReferenceWrapper;

/**
 * Created by ls on 20.02.14.
 */
public class SignInTask extends Task {

    private final String login;
    private final String password;
    private final WeakReferenceWrapper<LoginActivity> loginActivityWrapper;

    public SignInTask(LoginActivity loginActivity, String login, String password) {
        this.loginActivityWrapper = new WeakReferenceWrapper<LoginActivity>(loginActivity);
        this.login = login;
        this.password = password;
    }

    @Override
    protected Serializable doInBackground(Void... voids) {
        final Holder<Serializable> resultHolder = new Holder<Serializable>(AuthenticationResult.createDummyResult());
        loginActivityWrapper.handleIfExist(new WeakReferenceWrapper.Handler<LoginActivity>() {
            @Override
            public void handle(LoginActivity loginActivity) {
                Authenticator authenticator = new Authenticator(loginActivity, login, password);
                resultHolder.value = authenticator.authenticate();
                Snorlax.sleep();
            }
        });
        return resultHolder.value;
    }

}
