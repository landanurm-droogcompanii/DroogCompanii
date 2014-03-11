package ru.droogcompanii.application.ui.fragment.search_result_list.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import ru.droogcompanii.application.R;

/**
 * Created by ls on 11.03.14.
 */
class GroupTitleSearchResultListItem implements SearchResultListItem {
    private final int titleId;

    public GroupTitleSearchResultListItem(int titleId) {
        this.titleId = titleId;
    }

    @Override
    public int getLayoutId() {
        return R.layout.view_search_result_list_item_group_title;
    }

    @Override
    public void init(View itemView) {
        TextView titleTextView = (TextView) itemView.findViewById(R.id.title);
        titleTextView.setText(titleId);
        itemView.setClickable(false);
    }

    @Override
    public void onClick(Context context) {

    }
}
