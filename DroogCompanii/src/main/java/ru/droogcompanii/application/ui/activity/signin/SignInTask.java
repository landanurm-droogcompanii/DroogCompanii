package ru.droogcompanii.application.ui.activity.signin;

import android.content.Context;

import java.io.Serializable;

import ru.droogcompanii.application.ui.helpers.task.TaskNotBeInterrupted;
import ru.droogcompanii.application.util.Holder;
import ru.droogcompanii.application.util.Snorlax;
import ru.droogcompanii.application.util.WeakReferenceWrapper;

/**
 * Created by ls on 20.02.14.
 */
public class SignInTask extends TaskNotBeInterrupted {

    private final String login;
    private final String password;
    private final WeakReferenceWrapper<Context> contextWrapper;

    public SignInTask(Context context, String login, String password) {
        this.contextWrapper = new WeakReferenceWrapper<Context>(context);
        this.login = login;
        this.password = password;
    }

    @Override
    protected Serializable doInBackground(Void... voids) {
        final Holder<Serializable> resultHolder = new Holder<Serializable>(AuthenticationResult.unsuccessful());
        contextWrapper.handleIfExist(new WeakReferenceWrapper.Handler<Context>() {
            @Override
            public void handle(Context context) {
                Authenticator authenticator = new Authenticator(context, login, password);
                resultHolder.value = authenticator.authenticate();
            }
        });
        Snorlax.sleep();
        return resultHolder.value;
    }

}
