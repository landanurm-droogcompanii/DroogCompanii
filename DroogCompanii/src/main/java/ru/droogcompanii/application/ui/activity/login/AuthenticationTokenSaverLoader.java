package ru.droogcompanii.application.ui.activity.login;

import android.content.Context;

import com.google.common.base.Optional;

/**
 * Created by ls on 21.02.14.
 */
public class AuthenticationTokenSaverLoader {

    private static boolean tokenSaved = false;

    private final Context context;

    public AuthenticationTokenSaverLoader(Context context) {
        this.context = context;
    }

    public Optional<AuthenticationToken> load() {
        // TODO
        if (tokenSaved) {
            return Optional.of(new AuthenticationToken());
        } else {
            return Optional.absent();
        }
    }

    public void save(AuthenticationToken token) {
        // TODO
        tokenSaved = true;
    }
}
