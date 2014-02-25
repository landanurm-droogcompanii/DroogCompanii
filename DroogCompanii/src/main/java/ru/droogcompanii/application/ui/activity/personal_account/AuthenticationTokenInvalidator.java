package ru.droogcompanii.application.ui.activity.personal_account;

import android.content.Context;

import com.google.common.base.Optional;

import ru.droogcompanii.application.ui.activity.signin.AuthenticationToken;
import ru.droogcompanii.application.ui.activity.signin.AuthenticationTokenSaverLoader;

/**
 * Created by ls on 24.02.14.
 */
public class AuthenticationTokenInvalidator {
    private final AuthenticationTokenSaverLoader tokenSaverLoader;

    public AuthenticationTokenInvalidator(Context context) {
        this.tokenSaverLoader = new AuthenticationTokenSaverLoader(context);
    }

    public void invalidate(Optional<AuthenticationToken> optionalToken) {
        if (optionalToken.isPresent()) {
            tokenSaverLoader.invalidateToken(optionalToken.get());
        }
    }
}
