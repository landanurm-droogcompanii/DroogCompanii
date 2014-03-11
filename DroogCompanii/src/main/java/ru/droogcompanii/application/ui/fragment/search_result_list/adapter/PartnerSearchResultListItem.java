package ru.droogcompanii.application.ui.fragment.search_result_list.adapter;

import android.content.Context;
import android.view.View;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;

/**
 * Created by ls on 11.03.14.
 */
class PartnerSearchResultListItem implements SearchResultListItem {
    private final Partner partner;

    public PartnerSearchResultListItem(Partner partner) {
        this.partner = partner;
    }

    @Override
    public int getLayoutId() {
        return R.layout.view_search_result_list_item_partner;
    }

    @Override
    public void init(View itemView) {

    }

    @Override
    public void onClick(Context context) {

    }
}
