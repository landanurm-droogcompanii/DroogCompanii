package ru.droogcompanii.application.ui.activity.login;

/**
 * Created by ls on 19.02.14.
 */
public class CheckResult {
    boolean isSuccessful;
    int errorDescriptionTextId;

    public static CheckResult successful() {
        CheckResult result = new CheckResult();
        result.isSuccessful = true;
        return result;
    }

    public static CheckResult withError(int errorDescriptionTextId) {
        CheckResult result = new CheckResult();
        result.errorDescriptionTextId = errorDescriptionTextId;
        return result;
    }
}
