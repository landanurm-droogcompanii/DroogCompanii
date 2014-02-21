package ru.droogcompanii.application.ui.fragment.personal_details;

import android.content.Context;

import com.google.common.base.Optional;

import ru.droogcompanii.application.data.personal_details.PersonalDetails;
import ru.droogcompanii.application.data.personal_details.PersonalDetailsImpl;
import ru.droogcompanii.application.ui.activity.login.AuthenticationToken;

/**
 * Created by ls on 21.02.14.
 */
public class PersonalDetailsRequesterFromInet implements PersonalDetailsRequester {

    private final AuthenticationToken token;
    private final Context context;

    public PersonalDetailsRequesterFromInet(Context context, AuthenticationToken token) {
        this.context = context;
        this.token = token;
    }

    @Override
    public Optional<PersonalDetails> requestDetails() {
        // TODO
        PersonalDetailsImpl personalDetails = new PersonalDetailsImpl();
        personalDetails.firstName = "Dummy First Name from Inet";
        personalDetails.lastName = "Dummy Last Name from Inet";
        return Optional.of((PersonalDetails) personalDetails);
    }
}
