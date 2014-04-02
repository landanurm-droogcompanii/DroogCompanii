package ru.droogcompanii.application.ui.screens.search_result_list.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.ui.screens.partner_details.PartnerDetailsActivity;

/**
 * Created by ls on 11.03.14.
 */
class PartnerPointSearchResultListItem implements SearchResultListItem {
    private final PartnerPoint partnerPoint;

    public PartnerPointSearchResultListItem(PartnerPoint partnerPoint) {
        this.partnerPoint = partnerPoint;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public int getLayoutId() {
        return R.layout.view_search_result_list_item_partner_point;
    }

    @Override
    public void init(View itemView) {
        TextView title = (TextView) itemView.findViewById(R.id.title);
        title.setText(partnerPoint.getTitle());
        TextView address = (TextView) itemView.findViewById(R.id.address);
        address.setText(partnerPoint.getAddress());
    }

    @Override
    public void onClick(Context context) {
        PartnerDetailsActivity.startWithoutFilters(context, partnerPoint);
    }
}
