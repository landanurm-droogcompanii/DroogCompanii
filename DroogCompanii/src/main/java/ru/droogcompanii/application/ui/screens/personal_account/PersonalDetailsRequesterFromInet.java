package ru.droogcompanii.application.ui.screens.personal_account;

import android.content.Context;

import com.google.common.base.Optional;

import ru.droogcompanii.application.data.personal_details.AccountOwner;
import ru.droogcompanii.application.ui.screens.signin.AuthenticationToken;

/**
 * Created by ls on 21.02.14.
 */
public class PersonalDetailsRequesterFromInet implements PersonalDetailsRequester {

    private final Context context;

    public PersonalDetailsRequesterFromInet(Context context) {
        this.context = context;
    }

    @Override
    public Optional<AccountOwner> requestDetails(AuthenticationToken token) {
        // TODO
        return Optional.of(DummyAccountOwnerMaker.make());
    }
}