package ru.droogcompanii.application.ui.fragment.search_result_list.adapter;

import java.util.ArrayList;
import java.util.List;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerCategory;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.ui.fragment.search_result_list.SearchResultListFragment;

/**
 * Created by ls on 11.03.14.
 */
class InputToItemsConvertor {

    public static List<SearchResultListItem> convert(SearchResultListFragment.Input input) {
        List<SearchResultListItem> items = new ArrayList<SearchResultListItem>();
        if (!input.categories.isEmpty()) {
            items.add(new GroupTitleSearchResultListItem(R.string.categories));
            for (PartnerCategory eachCategory : input.categories) {
                items.add(new PartnerCategorySearchResultListItem(eachCategory));
            }
        }
        if (!input.partners.isEmpty()) {
            items.add(new GroupTitleSearchResultListItem(R.string.partners));
            for (Partner eachPartner : input.partners) {
                items.add(new PartnerSearchResultListItem(eachPartner));
            }
        }
        if (!input.points.isEmpty()) {
            items.add(new GroupTitleSearchResultListItem(R.string.partner_points));
            for (PartnerPoint eachPoint : input.points) {
                items.add(new PartnerPointSearchResultListItem(eachPoint));
            }
        }
        return items;
    }
}
