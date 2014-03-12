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

    private final SearchResultListFragment.Input input;
    private List<SearchResultListItem> items;


    public static List<SearchResultListItem> convert(SearchResultListFragment.Input input) {
        return new InputToItemsConvertor(input).convert();
    }

    private InputToItemsConvertor(SearchResultListFragment.Input input) {
        this.input = input;
    }

    private List<SearchResultListItem> convert() {
        items = new ArrayList<SearchResultListItem>();
        includeCategories();
        includePartners();
        includePoints();
        return items;
    }

    private void includeCategories() {
        include(input.categories, R.string.categories, new ConvertorToItem<PartnerCategory>() {
            @Override
            public SearchResultListItem convert(PartnerCategory toConvert) {
                return new PartnerCategorySearchResultListItem(toConvert);
            }
        });
    }

    private static interface ConvertorToItem<T> {
        SearchResultListItem convert(T toConvert);
    }

    private <E> void include(List<E> listToInclude, int groupTitleId, ConvertorToItem<E> convertorToItem) {
        if (listToInclude.isEmpty()) {
            return;
        }
        if (!items.isEmpty()) {
            items.add(new DividerSearchResultListItem());
        }
        items.add(new GroupTitleSearchResultListItem(groupTitleId));
        for (E each : listToInclude) {
            items.add(convertorToItem.convert(each));
        }
    }

    private void includePartners() {
        include(input.partners, R.string.partners, new ConvertorToItem<Partner>() {
            @Override
            public SearchResultListItem convert(Partner toConvert) {
                return new PartnerSearchResultListItem(toConvert);
            }
        });
    }

    private void includePoints() {
        include(input.points, R.string.partner_points, new ConvertorToItem<PartnerPoint>() {
            @Override
            public SearchResultListItem convert(PartnerPoint toConvert) {
                return new PartnerPointSearchResultListItem(toConvert);
            }
        });
    }
}
