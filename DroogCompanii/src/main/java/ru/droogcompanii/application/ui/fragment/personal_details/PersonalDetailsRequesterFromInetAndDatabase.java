package ru.droogcompanii.application.ui.fragment.personal_details;

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

    public PersonalDetailsRequesterFromInetAndDatabase(Context context, AuthenticationToken token) {
        this.requesterFromInet = new PersonalDetailsRequesterFromInet(context, token);
        this.saverLoader = new PersonalDetailsSaverLoader(context, token);
    }

    @Override
    public Optional<PersonalDetails> requestDetails() {
        try {
            return tryReceiveDetailsFromInet();
        } catch (Throwable e) {
            LogUtils.debug(e.getMessage());
            return saverLoader.load();
        }
    }

    private Optional<PersonalDetails> tryReceiveDetailsFromInet() {
        Optional<PersonalDetails> optional = requesterFromInet.requestDetails();
        saverLoader.saveIfPresent(optional);
        return optional;
    }
}
