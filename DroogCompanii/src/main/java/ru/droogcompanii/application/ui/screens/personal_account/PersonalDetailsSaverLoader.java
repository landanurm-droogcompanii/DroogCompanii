package ru.droogcompanii.application.ui.screens.personal_account;

import android.content.Context;

import com.google.common.base.Optional;

import ru.droogcompanii.application.data.personal_details.AccountOwner;
import ru.droogcompanii.application.data.personal_details.AccountOwnerImpl;
import ru.droogcompanii.application.ui.screens.personal_account.signin.AuthenticationToken;

/**
 * Created by ls on 21.02.14.
 */
public class PersonalDetailsSaverLoader {
    private final Context context;

    public PersonalDetailsSaverLoader(Context context) {
        this.context = context;
    }

    public Optional<AccountOwner> load(AuthenticationToken token) {
        // TODO
        AccountOwnerImpl personalDetails = new AccountOwnerImpl();
        personalDetails.setFirstName("Dummy First Name from SaverLoader");
        personalDetails.setLastName("Dummy Last Name from SaverLoader");
        return Optional.of((AccountOwner) personalDetails);
    }

    public void save(AuthenticationToken token, AccountOwner details) {
        // TODO
    }

    public void saveIfPresent(AuthenticationToken token, Optional<AccountOwner> optionalDetails) {
        if (optionalDetails.isPresent()) {
            save(token, optionalDetails.get());
        }
    }
}
