package ru.droogcompanii.application.view.activity_3;

import android.content.Intent;

import ru.droogcompanii.application.util.Keys;
import ru.droogcompanii.application.view.activity_3.filter_activity.FilterActivity;
import ru.droogcompanii.application.view.fragment.filter_fragment.filters.Filters;
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
            Filters returnedFilters = extractReturnedFilters(data);
            applyFilters(returnedFilters);
        }
    }

    private Filters extractReturnedFilters(Intent data) {
        Filters filters = null;
        if (data != null) {
            filters = (Filters) data.getSerializableExtra(Keys.filters);
        }
        if (filters == null) {
            throw new RuntimeException(FilterActivity.class.getName() + " doesn't return filters");
        }
        return filters;
    }

    private void applyFilters(Filters filters) {
        getPartnerPointsMapFragment().setFilters(filters);
    }

    protected abstract PartnerPointsMapFragment getPartnerPointsMapFragment();
}
