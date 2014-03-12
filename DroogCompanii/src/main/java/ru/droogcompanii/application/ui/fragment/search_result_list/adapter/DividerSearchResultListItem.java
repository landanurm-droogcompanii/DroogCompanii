package ru.droogcompanii.application.ui.fragment.search_result_list.adapter;

import android.content.Context;
import android.view.View;

import ru.droogcompanii.application.R;

/**
 * Created by ls on 12.03.14.
 */
public class DividerSearchResultListItem implements SearchResultListItem {
    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public int getLayoutId() {
        return R.layout.view_search_result_list_item_divider;
    }

    @Override
    public void init(View itemView) {
        // do nothing
    }

    @Override
    public void onClick(Context context) {
        // do nothing
    }
}
