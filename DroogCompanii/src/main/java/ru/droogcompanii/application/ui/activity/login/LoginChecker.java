package ru.droogcompanii.application.ui.activity.login;

import android.text.TextUtils;

import ru.droogcompanii.application.R;

/**
 * Created by ls on 19.02.14.
 */
public class LoginChecker {

    private static final int MIN_LENGTH = 4;
    private static final int MAX_LENGTH = 24;

    private static final String VALID_CHARACTERS = "1234567890";


    public static CheckResult check(String loginKey) {
        if (TextUtils.isEmpty(loginKey)) {
            return CheckResult.withError(R.string.error_field_required);
        } else if (loginHasIllegalLength(loginKey) || loginContainsIllegalSymbols(loginKey)) {
            return CheckResult.withError(R.string.error_invalid_login);
        }
        return CheckResult.successful();
    }

    private static boolean loginContainsIllegalSymbols(String loginKey) {
        return !loginKey.matches("^[" + VALID_CHARACTERS + "]+$");
    }

    private static boolean loginHasIllegalLength(String loginKey) {
        int loginLength = loginKey.length();
        return (MIN_LENGTH > loginLength || MAX_LENGTH < loginLength);
    }
}
