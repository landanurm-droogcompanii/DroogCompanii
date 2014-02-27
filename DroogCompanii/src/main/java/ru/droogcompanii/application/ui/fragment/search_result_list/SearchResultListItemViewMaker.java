package ru.droogcompanii.application.ui.fragment.search_result_list;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.data.searchable_sortable_listing.SearchResult;
import ru.droogcompanii.application.ui.helpers.IsFavoriteViewUtils;

/**
 * Created by Leonid on 27.02.14.
 */
class SearchResultListItemViewMaker {

    private final Context context;
    private final int rowLayoutId;
    private final IsFavoriteViewUtils isFavoriteViewUtils;

    public SearchResultListItemViewMaker(Context context, int rowLayoutId) {
        this.context = context;
        this.rowLayoutId = rowLayoutId;
        this.isFavoriteViewUtils = new IsFavoriteViewUtils(context);
    }

    public View make(View convertView, SearchResult<Partner> item) {
        View itemView = (convertView != null) ? convertView : inflateItemView();
        fill(itemView, item);
        return itemView;
    }

    private void fill(View itemView, SearchResult<Partner> item) {
        TextView textView = (TextView) itemView.findViewById(R.id.textView);
        textView.setTextColor(textColorOf(item));
        Partner partner = item.value();
        textView.setText(partner.getTitle());
        CheckBox checkBox = (CheckBox) itemView.findViewById(R.id.isFavorite);
        isFavoriteViewUtils.init(checkBox, partner.getId());
    }

    private View inflateItemView() {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        return layoutInflater.inflate(rowLayoutId, null);
    }

    private int textColorOf(SearchResult<Partner> item) {
        Resources resources = context.getResources();
        return resources.getColor(textColorIdOf(item));
    }

    private static int textColorIdOf(SearchResult<Partner> item) {
        if (item.meetsSearchCriteria()) {
            return R.color.textColorOfItemWhichMeetsCriteria;
        } else {
            return R.color.textColorOfItemWhichDoesNotMeetCriteria;
        }
    }
}
