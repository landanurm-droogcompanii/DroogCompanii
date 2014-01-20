package ru.droogcompanii.application.view.activity_3.filter_activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;
import java.util.List;

import ru.droogcompanii.application.DroogCompaniiSharedPreferences;
import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerCategory;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.data.searchable_sortable.filter.Filter;
import ru.droogcompanii.application.util.Keys;
import ru.droogcompanii.application.view.activity_3.filter_activity.standard_filter.WorkerWithStandardPartnerPointFilters;
import ru.droogcompanii.application.view.activity_3.filter_activity.worker_with_filters.WorkerWithFilters;
import ru.droogcompanii.application.view.activity_3.filter_activity.worker_with_filters.WorkerWithFiltersBuilder;
import ru.droogcompanii.application.view.activity_3.filter_activity.worker_with_filters.WorkerWithPartnerPointFiltersBuilder;

/**
 * Created by ls on 15.01.14.
 */

// This activity returns <List of Filters> to previous activity
public class FilterActivity extends Activity {

    public static final int REQUEST_CODE = 14235;

    private PartnerCategory partnerCategory;
    private SharedPreferences sharedPreferences;
    private View viewOfStandardFilters;
    private View viewOfMoreFilters;
    private WorkerWithFilters<PartnerPoint> workerWithStandardFilters;
    private WorkerWithFilters<PartnerPoint> workerWithMoreFilters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3_filter);

        sharedPreferences = DroogCompaniiSharedPreferences.get(this);

        initStandardFilters();

        if (savedInstanceState == null) {
            Bundle args = getIntent().getExtras();
            initMoreFilters(args);
        } else {
            initMoreFilters(savedInstanceState);
        }
    }

    private void initStandardFilters() {
        workerWithStandardFilters = new WorkerWithStandardPartnerPointFilters();
        viewOfStandardFilters = includeFiltersViewInContainer(
                R.id.containerOfStandardFilters, workerWithStandardFilters
        );
    }

    private View includeFiltersViewInContainer(int idOfContainer, WorkerWithFilters<?> workerWithFilters) {
        View viewOfFilters = workerWithFilters.prepareViewOfFilters(this, sharedPreferences);
        findViewGroupById(idOfContainer).addView(viewOfFilters);
        return viewOfFilters;
    }

    private ViewGroup findViewGroupById(int idOfViewGroup) {
        return (ViewGroup) findViewById(idOfViewGroup);
    }

    private void initMoreFilters(Bundle bundle) {
        partnerCategory = extractPartnerCategoryFrom(bundle);
        WorkerWithFiltersBuilder<PartnerPoint> builder = new WorkerWithPartnerPointFiltersBuilder(partnerCategory);
        workerWithMoreFilters = builder.build(this);
        viewOfMoreFilters = includeFiltersViewInContainer(
                R.id.containerOfMoreFilters, workerWithMoreFilters
        );
    }

    private PartnerCategory extractPartnerCategoryFrom(Bundle bundle) {
        if (bundle == null) {
            return null;
        } else {
            return (PartnerCategory) bundle.getSerializable(Keys.partnerCategory);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        workerWithStandardFilters.saveInto(editor, viewOfStandardFilters);
        workerWithMoreFilters.saveInto(editor, viewOfMoreFilters);
        editor.commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(Keys.partnerCategory, partnerCategory);
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(Keys.filters, (Serializable) getFilters());
        setResult(RESULT_OK, returnIntent);
        super.onBackPressed();
    }

    private List<Filter<PartnerPoint>> getFilters() {
        List<Filter<PartnerPoint>> filters = getStandardFilters();
        filters.addAll(getMoreFilters());
        return filters;
    }

    private List<Filter<PartnerPoint>> getStandardFilters() {
        return getFilters(R.id.containerOfStandardFilters, workerWithStandardFilters);
    }

    private List<Filter<PartnerPoint>> getMoreFilters() {
        return getFilters(R.id.containerOfMoreFilters, workerWithMoreFilters);
    }

    private List<Filter<PartnerPoint>> getFilters(int idOfContainer,
                                 WorkerWithFilters<PartnerPoint> workerWithFilters) {
        View containerOfFilters = findViewGroupById(idOfContainer);
        List<Filter<PartnerPoint>> filters = workerWithFilters.readFilters(containerOfFilters);
        return filters;
    }

}
