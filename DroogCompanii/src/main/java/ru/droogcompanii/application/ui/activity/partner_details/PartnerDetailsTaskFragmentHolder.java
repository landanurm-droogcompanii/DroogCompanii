package ru.droogcompanii.application.ui.activity.partner_details;

import android.app.Activity;
import android.os.Bundle;

import ru.droogcompanii.application.ui.helpers.task.TaskNotBeInterrupted;
import ru.droogcompanii.application.ui.helpers.task.TaskFragment;
import ru.droogcompanii.application.ui.helpers.task.TaskFragmentHolder;

/**
 * Created by ls on 13.02.14.
 */
public class PartnerDetailsTaskFragmentHolder extends TaskFragmentHolder {

    private PartnerDetailsActivity.PartnerAndPartnerPointsProvider provider;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Bundle args = getArguments();
        provider = (PartnerDetailsActivity.PartnerAndPartnerPointsProvider)
                args.getSerializable(PartnerDetailsActivity.Key.PARTNER_AND_PARTNER_POINTS_PROVIDER);
    }

    @Override
    protected Integer getTaskDialogTitleId() {
        return TaskFragment.NO_TITLE_ID;
    }

    @Override
    protected TaskNotBeInterrupted prepareTask() {
        return new PartnerDetailsTask(provider, getActivity());
    }
}
