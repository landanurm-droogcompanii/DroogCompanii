package ru.droogcompanii.application.ui.activity.partner_details;

import android.content.Context;

import java.io.Serializable;
import java.util.List;

import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.ui.helpers.task.Task;
import ru.droogcompanii.application.util.LogUtils;

/**
 * Created by ls on 13.02.14.
 */
public class PartnerDetailsTask extends Task {

    public static class Result implements Serializable {
        public Partner partner;
        public List<PartnerPoint> partnerPoints;
    }

    private final Context context;
    private final PartnerDetailsActivity.PartnerAndPartnerPointsProvider provider;

    public PartnerDetailsTask(PartnerDetailsActivity.PartnerAndPartnerPointsProvider provider, Context context) {
        this.context = context;
        this.provider = provider;
    }

    @Override
    protected Serializable doInBackground(Void... voids) {
        try {
            Thread.sleep(1400L);
        } catch (InterruptedException e) {
            LogUtils.debug(e.getMessage());
        }
        Result result = new Result();
        result.partner = provider.getPartner(context);
        result.partnerPoints = provider.getPartnerPoints(context);
        return result;
    }
}
