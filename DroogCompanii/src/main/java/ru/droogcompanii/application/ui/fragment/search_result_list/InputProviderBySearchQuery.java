package ru.droogcompanii.application.ui.fragment.search_result_list;

import android.content.Context;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ls on 11.03.14.
 */
public class InputProviderBySearchQuery implements SearchResultListFragment.InputProvider, Serializable {

    private final String query;

    public InputProviderBySearchQuery(String query) {
        this.query = query;
    }

    @Override
    public String getTitle(Context context) {
        return query;
    }

    @Override
    public List<SearchResultListFragment.Input> getInput(Context context) {
        return null;
    }

}
