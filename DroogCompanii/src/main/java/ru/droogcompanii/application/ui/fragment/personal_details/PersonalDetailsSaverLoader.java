package ru.droogcompanii.application.ui.fragment.personal_details;

import android.content.Context;

import com.google.common.base.Optional;

import ru.droogcompanii.application.data.personal_details.PersonalDetails;
import ru.droogcompanii.application.data.personal_details.PersonalDetailsImpl;
import ru.droogcompanii.application.ui.activity.signin.AuthenticationToken;

/**
 * Created by ls on 21.02.14.
 */
public class PersonalDetailsSaverLoader {

    private final AuthenticationToken token;
    private final Context context;

    public PersonalDetailsSaverLoader(Context context, AuthenticationToken token) {
        this.context = context;
        this.token = token;
    }

    public Optional<PersonalDetails> load() {
        // TODO
        PersonalDetailsImpl personalDetails = new PersonalDetailsImpl();
        personalDetails.firstName = "Dummy First Name from SaverLoader";
        personalDetails.lastName = "Dummy Last Name from SaverLoader";
        return Optional.of((PersonalDetails) personalDetails);
    }

    public void save(PersonalDetails details) {
        // TODO
    }

    public void saveIfPresent(Optional<PersonalDetails> optionalDetails) {
        if (optionalDetails.isPresent()) {
            save(optionalDetails.get());
        }
    }
}
