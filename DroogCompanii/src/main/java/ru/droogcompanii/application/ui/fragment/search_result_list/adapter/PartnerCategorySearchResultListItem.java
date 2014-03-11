package ru.droogcompanii.application.ui.fragment.search_result_list.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerCategory;

/**
 * Created by ls on 11.03.14.
 */
class PartnerCategorySearchResultListItem implements SearchResultListItem {
    private final PartnerCategory category;

    public PartnerCategorySearchResultListItem(PartnerCategory category) {
        this.category = category;
    }

    @Override
    public int getLayoutId() {
        return R.layout.view_search_result_list_item_partner_category;
    }

    @Override
    public void init(View itemView) {
        TextView titleTextView = (TextView) itemView.findViewById(R.id.title);
        TextView counterTextView = (TextView) itemView.findViewById(R.id.counter);
        int count = PartnersCounter.countPartnersInCategory(itemView.getContext(), category);
        titleTextView.setText(category.getTitle());
        counterTextView.setText(String.valueOf(count));
    }


    @Override
    public void onClick(Context context) {

    }
}
