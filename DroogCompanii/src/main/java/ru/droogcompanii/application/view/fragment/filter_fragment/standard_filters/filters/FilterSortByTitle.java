package ru.droogcompanii.application.view.fragment.filter_fragment.standard_filters.filters;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.view.fragment.filter_fragment.Filters;
import ru.droogcompanii.application.view.fragment.filter_fragment.standard_filters.search_criteria_and_comparators.ComparatorByTitle;

/**
 * Created by ls on 21.01.14.
 */
public class FilterSortByTitle extends FilterWithOneCheckbox {
    @Override
    protected boolean isNeedToIncludeByDefault() {
        return true;
    }

    @Override
    protected int getIdOfCheckbox() {
        return R.id.sortByTitleCheckBox;
    }

    @Override
    protected void necessarilyIncludeIn(Filters filters) {
        filters.add(new ComparatorByTitle());
    }
}
