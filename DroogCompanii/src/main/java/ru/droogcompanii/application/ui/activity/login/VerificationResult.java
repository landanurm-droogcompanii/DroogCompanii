package ru.droogcompanii.application.ui.activity.login;

/**
 * Created by ls on 19.02.14.
 */
public class VerificationResult {
    boolean isSuccessful;
    int personId;

    public static VerificationResult successful() {
        VerificationResult result = new VerificationResult();
        result.isSuccessful = true;
        return result;
    }

    public static VerificationResult unsuccessful() {
        VerificationResult result = new VerificationResult();
        result.isSuccessful = false;
        return result;
    }

}
