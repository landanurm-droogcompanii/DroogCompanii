package ru.droogcompanii.application.ui.activity_3.search_result_activity;

import android.content.Context;

import java.io.Serializable;
import java.util.List;

import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.ui.activity_3.search_activity.SearchResultProvider;
import ru.droogcompanii.application.ui.helpers.task.Task;
import ru.droogcompanii.application.util.LogUtils;

/**
 * Created by ls on 30.01.14.
 */
public class SearchResultTask extends Task {
    private final Context context;
    private final SearchResultProvider searchResultProvider;

    public SearchResultTask(SearchResultProvider searchResultProvider, Context context) {
        this.searchResultProvider = searchResultProvider;
        this.context = context;
    }

    @Override
    protected Serializable doInBackground(Void... voids) {
        if (searchResultProvider == null) {
            LogUtils.debug("searchResultProvider == null");
        }
        List<Partner> partners = searchResultProvider.getPartners(context);
        if (partners == null) {
            LogUtils.debug("partners == null");
        } else {
            LogUtils.debug("partners count = " + partners.size());
        }
        return (Serializable) partners;
    }
}
