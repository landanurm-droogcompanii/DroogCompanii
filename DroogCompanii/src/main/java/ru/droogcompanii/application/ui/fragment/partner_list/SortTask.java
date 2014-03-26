package ru.droogcompanii.application.ui.fragment.partner_list;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.util.able_to_start_task.TaskNotBeInterruptedDuringConfigurationChange;

/**
 * Created by ls on 24.02.14.
 */
public class SortTask extends TaskNotBeInterruptedDuringConfigurationChange {
    private final Comparator<Partner> comparator;
    private final List<Partner> listToSort;

    public SortTask(List<Partner> partners,
                    Comparator<Partner> comparator) {
        this.listToSort = partners;
        this.comparator = comparator;
    }

    @Override
    protected Serializable doInBackground(Void... voids) {
        List<Partner> result = new ArrayList<Partner>(listToSort);
        Collections.sort(result, comparator);
        return (Serializable) result;
    }
}