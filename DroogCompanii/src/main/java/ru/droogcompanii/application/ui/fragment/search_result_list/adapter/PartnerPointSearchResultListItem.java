package ru.droogcompanii.application.ui.fragment.search_result_list.adapter;

import android.content.Context;
import android.view.View;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;

/**
 * Created by ls on 11.03.14.
 */
class PartnerPointSearchResultListItem implements SearchResultListItem {
    private final PartnerPoint point;

    public PartnerPointSearchResultListItem(PartnerPoint point) {
        this.point = point;
    }

    @Override
    public int getLayoutId() {
        return R.layout.view_search_result_list_item_partner_point;
    }

    @Override
    public void init(View itemView) {

    }

    @Override
    public void onClick(Context context) {

    }
}
