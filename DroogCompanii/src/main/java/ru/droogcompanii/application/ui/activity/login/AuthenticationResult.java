package ru.droogcompanii.application.ui.activity.login;

import java.io.Serializable;

/**
 * Created by ls on 20.02.14.
 */
public class AuthenticationResult implements Serializable {

    private final AuthenticationToken token;
    private final boolean isSuccessful;

    public static AuthenticationResult unsuccessful() {
        return new AuthenticationResult(false, null);
    }

    public static AuthenticationResult successful(AuthenticationToken token) {
        return new AuthenticationResult(true, token);
    }

    private AuthenticationResult(boolean isSuccessful, AuthenticationToken token) {
        this.isSuccessful = isSuccessful;
        this.token = token;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public AuthenticationToken getToken() {
        return token;
    }
}
