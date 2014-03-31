package ru.droogcompanii.application.ui.screens.search_result_list.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.ui.screens.partner_details.PartnerDetailsActivity;

/**
 * Created by ls on 11.03.14.
 */
class PartnerSearchResultListItem implements SearchResultListItem {
    private final Partner partner;

    public PartnerSearchResultListItem(Partner partner) {
        this.partner = partner;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public int getLayoutId() {
        return R.layout.view_search_result_list_item_partner;
    }

    @Override
    public void init(View itemView) {
        TextView title = (TextView) itemView.findViewById(R.id.title);
        title.setText(partner.getTitle());
        TextView discountSize = (TextView) itemView.findViewById(R.id.discountSize);
        discountSize.setText(prepareDiscountText());
    }

    private String prepareDiscountText() {
        return partner.getDiscountType() + ": " + String.valueOf(partner.getDiscountSize()) + "%";
    }

    @Override
    public void onClick(Context context) {
        PartnerDetailsActivity.startWithoutFilters(context, partner);
    }
}
