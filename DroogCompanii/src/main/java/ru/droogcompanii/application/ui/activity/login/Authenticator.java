package ru.droogcompanii.application.ui.activity.login;

import android.content.Context;

/**
 * Created by ls on 20.02.14.
 */
public class Authenticator {
    private final Context context;
    private final String login;
    private final String password;

    public Authenticator(Context context, String login, String password) {
        this.context = context;
        this.login = login;
        this.password = password;
    }

    public AuthenticationResult authenticate() {
        // TODO
        return AuthenticationResult.successful(new AuthenticationToken());
    }
}
