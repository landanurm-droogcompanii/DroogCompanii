package ru.droogcompanii.application.ui.fragment.personal_details;

import java.io.Serializable;

import ru.droogcompanii.application.ui.helpers.task.TaskNotBeInterrupted;
import ru.droogcompanii.application.util.Snorlax;

/**
 * Created by ls on 21.02.14.
 */
public class PersonalDetailsRequestTask extends TaskNotBeInterrupted {
    private final PersonalDetailsRequester requester;

    public PersonalDetailsRequestTask(PersonalDetailsRequester requester) {
        this.requester = requester;
    }

    @Override
    protected Serializable doInBackground(Void... voids) {
        Snorlax.sleep();
        return requester.requestDetails();
    }
}
