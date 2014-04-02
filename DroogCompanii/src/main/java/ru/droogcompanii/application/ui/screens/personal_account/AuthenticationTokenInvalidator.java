package ru.droogcompanii.application.ui.screens.personal_account;

import android.content.Context;

import com.google.common.base.Optional;

import ru.droogcompanii.application.ui.screens.personal_account.signin.AuthenticationToken;
import ru.droogcompanii.application.ui.screens.personal_account.signin.AuthenticationTokenSaverLoader;

/**
 * Created by ls on 24.02.14.
 */
public class AuthenticationTokenInvalidator {
    private final AuthenticationTokenSaverLoader tokenSaverLoader;

    public AuthenticationTokenInvalidator(Context context) {
        this.tokenSaverLoader = new AuthenticationTokenSaverLoader(context);
    }

    public void invalidate(Optional<AuthenticationToken> token) {
        if (token.isPresent()) {
            tokenSaverLoader.invalidateToken(token.get());
        }
    }
}
