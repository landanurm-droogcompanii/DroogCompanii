package ru.droogcompanii.application.ui.fragment.personal_details;

import com.google.common.base.Optional;

import ru.droogcompanii.application.data.personal_details.PersonalDetails;

/**
 * Created by ls on 21.02.14.
 */
public interface PersonalDetailsRequester {
    Optional<PersonalDetails> requestDetails();
}
