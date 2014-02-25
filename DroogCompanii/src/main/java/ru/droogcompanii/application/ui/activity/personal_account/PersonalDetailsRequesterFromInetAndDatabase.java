package ru.droogcompanii.application.ui.activity.personal_account;

import android.content.Context;

import com.google.common.base.Optional;

import ru.droogcompanii.application.data.personal_details.PersonalDetails;
import ru.droogcompanii.application.ui.activity.signin.AuthenticationToken;
import ru.droogcompanii.application.util.LogUtils;

/**
 * Created by ls on 21.02.14.
 */
public class PersonalDetailsRequesterFromInetAndDatabase implements PersonalDetailsRequester {

    private final PersonalDetailsRequester requesterFromInet;
    private final PersonalDetailsSaverLoader saverLoader;

    public PersonalDetailsRequesterFromInetAndDatabase(Context context) {
        this.requesterFromInet = new PersonalDetailsRequesterFromInet(context);
        this.saverLoader = new PersonalDetailsSaverLoader(context);
    }

    @Override
    public Optional<PersonalDetails> requestDetails(AuthenticationToken token) {
        try {
            log("try receive details from inet");

            return tryReceiveDetailsFromInet(token);
        } catch (Throwable e) {
            log("load details from database");

            LogUtils.debug(e.getMessage());
            return saverLoader.load(token);
        }
    }

    private Optional<PersonalDetails> tryReceiveDetailsFromInet(AuthenticationToken token) {
        Optional<PersonalDetails> optionalDetails = requesterFromInet.requestDetails(token);
        saverLoader.saveIfPresent(token, optionalDetails);
        return optionalDetails;
    }

    private static void log(String message) {
        LogUtils.debug(PersonalDetailsRequesterFromInetAndDatabase.class.getSimpleName() + ": " + message);
    }
}
