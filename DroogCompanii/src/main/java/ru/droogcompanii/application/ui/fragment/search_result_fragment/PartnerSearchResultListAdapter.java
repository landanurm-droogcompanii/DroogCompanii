package ru.droogcompanii.application.ui.fragment.search_result_fragment;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.data.searchable_sortable_listing.SearchResult;
import ru.droogcompanii.application.ui.helpers.SimpleArrayAdapter;
import ru.droogcompanii.application.util.LogUtils;

/**
 * Created by ls on 14.01.14.
 */
public class PartnerSearchResultListAdapter extends SimpleArrayAdapter<SearchResult<Partner>> {

    public PartnerSearchResultListAdapter(Context context, List<SearchResult<Partner>> partners) {
        super(context, partners, new ItemToTitleConvertor<SearchResult<Partner>>() {
            @Override
            public String getTitle(SearchResult<Partner> item) {
                return item.value().title;
            }
        });
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = super.getView(position, convertView, parent);
        SearchResult<Partner> item = getItem(position);
        TextView textView = (TextView) itemView.findViewById(android.R.id.text1);
        textView.setTextColor(textColorOf(item));
        return itemView;
    }

    private int textColorOf(SearchResult<Partner> item) {
        Resources resources = getContext().getResources();
        return resources.getColor(textColorIdOf(item));
    }

    private int textColorIdOf(SearchResult<Partner> item) {
        if (item.meetsSearchCriteria()) {
            LogUtils.debug("meetsCriteria");
            return R.color.textColorOfItemWhichMeetsCriteria;
        } else {
            LogUtils.debug("does not meetsCriteria");
            return R.color.textColorOfItemWhichDoesNotMeetCriteria;
        }
    }
}
