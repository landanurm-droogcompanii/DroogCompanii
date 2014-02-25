package ru.droogcompanii.application.ui.activity.personal_account;

import com.google.common.base.Optional;

import ru.droogcompanii.application.data.personal_details.PersonalDetails;
import ru.droogcompanii.application.ui.activity.signin.AuthenticationToken;

/**
 * Created by ls on 21.02.14.
 */
public interface PersonalDetailsRequester {
    Optional<PersonalDetails> requestDetails(AuthenticationToken token);
}
