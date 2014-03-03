package ru.droogcompanii.application.ui.activity.personal_account;

import ru.droogcompanii.application.data.personal_details.AccountOwner;

/**
 * Created by ls on 25.02.14.
 */
public interface DetailsReceiver {
    void onReceiveDetails(AccountOwner details);
}
