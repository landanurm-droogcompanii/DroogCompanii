package ru.droogcompanii.application.ui.activity.login;

import android.text.TextUtils;

import ru.droogcompanii.application.R;

/**
 * Created by ls on 19.02.14.
 */
public class PasswordChecker {

    private static final int MIN_LENGTH_OF_PASSWORD = 4;

    public static CheckResult check(String password) {
        if (TextUtils.isEmpty(password)) {
            return CheckResult.withError(R.string.error_field_required);
        } else if (password.length() < MIN_LENGTH_OF_PASSWORD) {
            return CheckResult.withError(R.string.error_invalid_password);
        }
        return CheckResult.successful();
    }
}
