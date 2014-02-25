package ru.droogcompanii.application.ui.activity.personal_account;

import ru.droogcompanii.application.data.personal_details.PersonalDetails;

/**
 * Created by ls on 25.02.14.
 */
public interface DetailsReceiver {
    void onReceiveDetails(PersonalDetails details);
}
