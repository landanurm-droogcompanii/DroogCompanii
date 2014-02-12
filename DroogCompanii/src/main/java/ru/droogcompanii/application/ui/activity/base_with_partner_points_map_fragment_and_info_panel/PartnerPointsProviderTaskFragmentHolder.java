package ru.droogcompanii.application.ui.activity.base_with_partner_points_map_fragment_and_info_panel;

import android.os.Bundle;

import ru.droogcompanii.application.ui.fragment.partner_points_map.PartnerPointsProvider;
import ru.droogcompanii.application.ui.helpers.task.Task;
import ru.droogcompanii.application.ui.helpers.task.TaskFragmentHolder;

/**
 * Created by ls on 30.01.14.
 */
public class PartnerPointsProviderTaskFragmentHolder extends TaskFragmentHolder {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            startTask();
        }
    }

    @Override
    protected Integer getTaskDialogTitleId() {
        return null;
    }

    @Override
    protected Task prepareTask() {
        BaseActivityWithPartnerPointsMapFragmentAndInfoPanel activity =
                (BaseActivityWithPartnerPointsMapFragmentAndInfoPanel) getActivity();
        PartnerPointsProvider partnerPointsProvider = activity.getPartnerPointsProvider();
        return new PartnerPointsProviderTask(partnerPointsProvider, activity);
    }
}
