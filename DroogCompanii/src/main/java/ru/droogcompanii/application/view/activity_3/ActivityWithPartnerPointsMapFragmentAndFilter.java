package ru.droogcompanii.application.view.activity_3;

import android.content.Intent;

import ru.droogcompanii.application.util.Keys;
import ru.droogcompanii.application.view.activity_3.filter_activity.FilterActivity;
import ru.droogcompanii.application.view.fragment.filter_fragment.FilterSet;
import ru.droogcompanii.application.view.fragment.partner_points_map_fragment.PartnerPointsMapFragment;

/**
 * Created by ls on 22.01.14.
 */
public abstract class ActivityWithPartnerPointsMapFragmentAndFilter extends android.support.v4.app.FragmentActivity {

    protected void onFilter() {
        Intent intent = new Intent(this, FilterActivity.class);
        startActivityForResult(intent, FilterActivity.REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode == FilterActivity.REQUEST_CODE) && (resultCode == RESULT_OK)) {
            FilterSet returnedFilterSet = extractReturnedFilters(data);
            applyFilters(returnedFilterSet);
        }
    }

    private FilterSet extractReturnedFilters(Intent data) {
        FilterSet filterSet = null;
        if (data != null) {
            filterSet = (FilterSet) data.getSerializableExtra(Keys.filterSet);
        }
        if (filterSet == null) {
            throw new RuntimeException(FilterActivity.class.getName() + " doesn't return filterSet");
        }
        return filterSet;
    }

    private void applyFilters(FilterSet filterSet) {
        getPartnerPointsMapFragment().setFilterSet(filterSet);
    }

    protected abstract PartnerPointsMapFragment getPartnerPointsMapFragment();
}
