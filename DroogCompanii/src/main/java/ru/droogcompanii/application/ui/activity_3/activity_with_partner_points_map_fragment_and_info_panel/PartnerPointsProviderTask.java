package ru.droogcompanii.application.ui.activity_3.activity_with_partner_points_map_fragment_and_info_panel;

import android.content.Context;

import java.io.Serializable;

import ru.droogcompanii.application.ui.fragment.partner_points_map_fragment.PartnerPointsProvider;
import ru.droogcompanii.application.ui.helpers.task.Task;

/**
 * Created by ls on 30.01.14.
 */
public class PartnerPointsProviderTask extends Task {
    private final Context context;
    private final PartnerPointsProvider partnerPointsProvider;

    public PartnerPointsProviderTask(PartnerPointsProvider partnerPointsProvider, Context context) {
        this.partnerPointsProvider = partnerPointsProvider;
        this.context = context;
    }

    @Override
    protected Serializable doInBackground(Void... voids) {
        return (Serializable) partnerPointsProvider.getPartnerPoints(context);
    }
}
