package ru.droogcompanii.application.ui.activity.base_with_partner_points_map_fragment_and_info_panel;

import android.content.Context;

import java.io.Serializable;

import ru.droogcompanii.application.ui.fragment.partner_points_map.PartnerPointsProvider;
import ru.droogcompanii.application.ui.util.able_to_start_task.TaskNotBeInterruptedDuringConfigurationChange;
import ru.droogcompanii.application.util.Snorlax;

/**
 * Created by ls on 30.01.14.
 */
public class PartnerPointsProviderTask extends TaskNotBeInterruptedDuringConfigurationChange {
    private final Context context;
    private final PartnerPointsProvider partnerPointsProvider;

    public PartnerPointsProviderTask(PartnerPointsProvider partnerPointsProvider, Context context) {
        this.partnerPointsProvider = partnerPointsProvider;
        this.context = context;
    }

    @Override
    protected Serializable doInBackground(Void... voids) {
        Snorlax.sleep();
        return (Serializable) partnerPointsProvider.getPartnerPoints(context);
    }
}
