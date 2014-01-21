package ru.droogcompanii.application.view.fragment.filter_fragment.worker_with_filters;

import android.content.Context;

import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerCategory;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;

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
        if (partnerCategory == null) {
            return DummyWorkerWithPartnerPointFilters.get();
        }
        // TODO:
        throw new RuntimeException("Not implemented yet");
    }
}
