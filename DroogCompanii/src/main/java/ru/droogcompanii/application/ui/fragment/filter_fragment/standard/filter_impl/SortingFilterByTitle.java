package ru.droogcompanii.application.ui.fragment.filter_fragment.standard.filter_impl;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.ui.fragment.filter_fragment.FilterSet;
import ru.droogcompanii.application.ui.fragment.filter_fragment.standard.search_criteria_and_comparators.ComparatorByTitle;

/**
 * Created by ls on 21.01.14.
 */
class SortingFilterByTitle extends FilterWithOneCheckbox {
    @Override
    protected boolean isNeedToIncludeByDefault() {
        return true;
    }

    @Override
    protected int getIdOfCheckbox() {
        return R.id.sortByTitleCheckBox;
    }

    @Override
    protected void necessarilyIncludeIn(FilterSet filterSet) {
        filterSet.add(new ComparatorByTitle());
    }
}
