package ru.droogcompanii.application.ui.activity.login;

/**
 * Created by ls on 19.02.14.
 */
public class PasswordVerifier {

    /**
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "79091123344:123456",
            "79193342255:123456"
    };

    public VerificationResult verify(String login, String password) {
        // TODO: attempt authentication against a network service.

        for (String credential : DUMMY_CREDENTIALS) {
            String[] pieces = credential.split(":");
            String credentialLogin = pieces[0];
            String credentialPassword = pieces[1];
            if (credentialLogin.equals(login)) {
                return (credentialPassword.equals(password))
                        ? VerificationResult.successful()
                        : VerificationResult.unsuccessful();
            }
        }
        return VerificationResult.unsuccessful();
    }

}
