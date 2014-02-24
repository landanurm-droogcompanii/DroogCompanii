package ru.droogcompanii.application.ui.fragment.personal_details;

import android.content.Context;

import com.google.common.base.Optional;

import ru.droogcompanii.application.ui.activity.signin.AuthenticationToken;
import ru.droogcompanii.application.ui.activity.signin.AuthenticationTokenSaverLoader;

/**
 * Created by ls on 24.02.14.
 */
public class SignOutHelper {
    private final AuthenticationTokenSaverLoader tokenSaverLoader;
    private final Optional<AuthenticationToken> optionalToken;

    public SignOutHelper(Context context, Optional<AuthenticationToken> optionalToken) {
        this.tokenSaverLoader = new AuthenticationTokenSaverLoader(context);
        this.optionalToken = optionalToken;
    }

    public void signOut() {
        if (optionalToken.isPresent()) {
            tokenSaverLoader.invalidateToken(optionalToken.get());
        }
    }
}
