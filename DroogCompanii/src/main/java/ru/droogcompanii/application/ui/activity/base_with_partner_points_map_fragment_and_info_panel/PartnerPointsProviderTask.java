package ru.droogcompanii.application.ui.activity.base_with_partner_points_map_fragment_and_info_panel;

import android.content.Context;

import java.io.Serializable;

import ru.droogcompanii.application.ui.fragment.partner_points_map.PartnerPointsProvider;
import ru.droogcompanii.application.ui.helpers.task.Task;
import ru.droogcompanii.application.util.LogUtils;

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
        try {
            Thread.sleep(3000L);
        } catch (InterruptedException e) {
            LogUtils.debug(e.getMessage());
        }
        return (Serializable) partnerPointsProvider.getPartnerPoints(context);
    }
}
