package ru.droogcompanii.application.ui.fragment.search_result_list;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.data.searchable_sortable_listing.SearchResult;

/**
 * Created by ls on 14.01.14.
 */
class SearchResultListAdapter extends ArrayAdapter<SearchResult<Partner>> {

    private static final int ROW_LAYOUT_ID = R.layout.view_search_result_list_item;

    private final SearchResultListItemViewMaker itemViewMaker;


    public SearchResultListAdapter(Context context, List<SearchResult<Partner>> partners) {
        super(context, ROW_LAYOUT_ID, partners);
        itemViewMaker = new SearchResultListItemViewMaker(context, ROW_LAYOUT_ID);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return itemViewMaker.make(convertView, getItem(position));
    }
}
