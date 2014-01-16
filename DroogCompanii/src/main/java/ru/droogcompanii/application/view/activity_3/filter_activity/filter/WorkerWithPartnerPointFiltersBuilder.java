package ru.droogcompanii.application.view.activity_3.filter_activity.filter;

import android.content.Context;

import ru.droogcompanii.application.data.data_structure.PartnerCategory;
import ru.droogcompanii.application.data.data_structure.PartnerPoint;

/**
 * Created by ls on 15.01.14.
 */
public class WorkerWithPartnerPointFiltersBuilder implements WorkerWithFiltersBuilder<PartnerPoint> {
    private final PartnerCategory partnerCategory;

    public WorkerWithPartnerPointFiltersBuilder(PartnerCategory partnerCategory) {
        this.partnerCategory = partnerCategory;
    }

    @Override
    public WorkerWithFilters<PartnerPoint> build(Context context) {
        // TODO:
        throw new RuntimeException("Not implemented yet");
    }
}
