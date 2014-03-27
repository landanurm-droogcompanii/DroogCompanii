package ru.droogcompanii.application.ui.fragment.search_result_list.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.ui.activity.partner_details_2.PartnerDetailsActivity2;

/**
 * Created by ls on 11.03.14.
 */
class PartnerPointSearchResultListItem implements SearchResultListItem {
    private final PartnerPoint point;

    public PartnerPointSearchResultListItem(PartnerPoint point) {
        this.point = point;
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
        title.setText(point.getTitle());
        TextView address = (TextView) itemView.findViewById(R.id.address);
        address.setText(point.getAddress());
    }

    @Override
    public void onClick(Context context) {
        PartnerDetailsActivity2.startWithoutFilters(context, point);
    }
}
