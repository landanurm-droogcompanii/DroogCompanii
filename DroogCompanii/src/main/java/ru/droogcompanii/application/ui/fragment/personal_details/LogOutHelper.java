package ru.droogcompanii.application.ui.fragment.personal_details;

import android.content.Context;

import com.google.common.base.Optional;

import ru.droogcompanii.application.ui.activity.login.AuthenticationToken;
import ru.droogcompanii.application.ui.activity.login.AuthenticationTokenSaverLoader;

/**
 * Created by ls on 24.02.14.
 */
public class LogOutHelper {
    private final AuthenticationTokenSaverLoader tokenSaverLoader;
    private final Optional<AuthenticationToken> optionalToken;

    public LogOutHelper(Context context, Optional<AuthenticationToken> optionalToken) {
        this.tokenSaverLoader = new AuthenticationTokenSaverLoader(context);
        this.optionalToken = optionalToken;
    }

    public void logout() {
        if (optionalToken.isPresent()) {
            tokenSaverLoader.invalidateToken(optionalToken.get());
        }
    }
}
