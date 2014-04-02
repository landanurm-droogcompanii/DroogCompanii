package ru.droogcompanii.application.ui.screens.search_result_list.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerCategory;
import ru.droogcompanii.application.ui.screens.partner_list.PartnerListActivity;
import ru.droogcompanii.application.ui.screens.partner_details.InputProviderByPartnerCategory;

/**
 * Created by ls on 11.03.14.
 */
class PartnerCategorySearchResultListItem implements SearchResultListItem {
    private final PartnerCategory category;

    public PartnerCategorySearchResultListItem(PartnerCategory category) {
        this.category = category;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public int getLayoutId() {
        return R.layout.view_search_result_list_item_partner_category;
    }

    @Override
    public void init(View itemView) {
        TextView title = (TextView) itemView.findViewById(R.id.title);
        title.setText(category.getTitle());
        TextView counter = (TextView) itemView.findViewById(R.id.counter);
        int countOfPartners = PartnersCounter.countPartnersInCategory(itemView.getContext(), category);
        counter.setText(String.valueOf(countOfPartners));
    }


    @Override
    public void onClick(Context context) {
        PartnerListActivity.start(context, new InputProviderByPartnerCategory(category));
    }
}
