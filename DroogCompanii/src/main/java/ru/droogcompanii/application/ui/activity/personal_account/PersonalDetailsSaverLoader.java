package ru.droogcompanii.application.ui.activity.personal_account;

import android.content.Context;

import com.google.common.base.Optional;

import ru.droogcompanii.application.data.personal_details.PersonalDetails;
import ru.droogcompanii.application.data.personal_details.PersonalDetailsImpl;
import ru.droogcompanii.application.ui.activity.signin.AuthenticationToken;

/**
 * Created by ls on 21.02.14.
 */
public class PersonalDetailsSaverLoader {

    private final Context context;

    public PersonalDetailsSaverLoader(Context context) {
        this.context = context;
    }

    public Optional<PersonalDetails> load(AuthenticationToken token) {
        // TODO
        PersonalDetailsImpl personalDetails = new PersonalDetailsImpl();
        personalDetails.firstName = "Dummy First Name from SaverLoader";
        personalDetails.lastName = "Dummy Last Name from SaverLoader";
        return Optional.of((PersonalDetails) personalDetails);
    }

    public void save(AuthenticationToken token, PersonalDetails details) {
        // TODO
    }

    public void saveIfPresent(AuthenticationToken token, Optional<PersonalDetails> optionalDetails) {
        if (optionalDetails.isPresent()) {
            save(token, optionalDetails.get());
        }
    }
}
