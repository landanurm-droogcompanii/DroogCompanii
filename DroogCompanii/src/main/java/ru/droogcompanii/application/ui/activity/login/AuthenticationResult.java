package ru.droogcompanii.application.ui.activity.login;

import java.io.InputStream;
import java.io.Serializable;

import ru.droogcompanii.application.util.StringToInputStreamConvertor;

/**
 * Created by ls on 20.02.14.
 */
public class AuthenticationResult implements Serializable {

    public static AuthenticationResult createDummyResult() {
        return new AuthenticationResult();
    }

    public boolean isSuccessful() {
        return false;
    }

    public InputStream getInputStream() {
        return StringToInputStreamConvertor.convert("");
    }
}
