package ru.droogcompanii.application.ui.activity.personal_account;

import android.content.Context;

import com.google.common.base.Optional;

import ru.droogcompanii.application.data.personal_details.AccountOwner;
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
    public Optional<AccountOwner> requestDetails(AuthenticationToken token) {
        try {
            return tryReceiveDetailsFromInet(token);
        } catch (Throwable e) {
            LogUtils.debug(e.getMessage());
            return saverLoader.load(token);
        }
    }

    private Optional<AccountOwner> tryReceiveDetailsFromInet(AuthenticationToken token) {
        Optional<AccountOwner> optionalDetails = requesterFromInet.requestDetails(token);
        saverLoader.saveIfPresent(token, optionalDetails);
        return optionalDetails;
    }

}
