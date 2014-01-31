package ru.droogcompanii.application.ui.activity.activity_with_partner_points_map_fragment_and_info_panel;

import android.os.Bundle;

import ru.droogcompanii.application.ui.fragment.partner_points_map_fragment.PartnerPointsProvider;
import ru.droogcompanii.application.ui.helpers.task.Task;
import ru.droogcompanii.application.ui.helpers.task.TaskFragmentHolder;

/**
 * Created by ls on 30.01.14.
 */
public class PartnerPointsProviderTaskFragmentHolder extends TaskFragmentHolder {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isActivityFirstLaunched()) {
            startTask();
        }
    }

    private boolean isActivityFirstLaunched() {
        ActivityWithPartnerPointsMapFragmentAndInfoPanel activity =
                (ActivityWithPartnerPointsMapFragmentAndInfoPanel) getActivity();
        return activity.isFirstLaunched();
    }

    @Override
    protected String getTaskDialogTitle() {
        return "";
    }

    @Override
    protected Task prepareTask() {
        ActivityWithPartnerPointsMapFragmentAndInfoPanel activity =
                (ActivityWithPartnerPointsMapFragmentAndInfoPanel) getActivity();
        PartnerPointsProvider partnerPointsProvider = activity.preparePartnerPointsProvider();
        return new PartnerPointsProviderTask(partnerPointsProvider, activity);
    }
}
