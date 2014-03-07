package ru.droogcompanii.application.ui.fragment.search_result_list;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.data.searchable_sortable_listing.SearchResult;
import ru.droogcompanii.application.ui.util.able_to_start_task.TaskNotBeInterrupted;

/**
 * Created by ls on 24.02.14.
 */
public class SortTask extends TaskNotBeInterrupted {
    private final Comparator<Partner> comparator;
    private final List<SearchResult<Partner>> listToSort;

    public SortTask(List<SearchResult<Partner>> searchResults,
                    Comparator<Partner> comparator) {
        listToSort = searchResults;
        this.comparator = comparator;
    }

    @Override
    protected Serializable doInBackground(Void... voids) {
        List<SearchResult<Partner>> result = new ArrayList<SearchResult<Partner>>(listToSort);
        Collections.sort(result, new Comparator<SearchResult<Partner>>() {
            @Override
            public int compare(SearchResult<Partner> res1, SearchResult<Partner> res2) {
                return comparator.compare(res1.value(), res2.value());
            }
        });
        return (Serializable) result;
    }
}