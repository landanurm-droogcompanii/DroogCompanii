package ru.droogcompanii.application.view.activity_3.filter_activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.data_structure.PartnerCategory;
import ru.droogcompanii.application.data.data_structure.PartnerPoint;
import ru.droogcompanii.application.util.Keys;
import ru.droogcompanii.application.view.activity_3.filter_activity.filter.DummyWorkerWithPartnerPointFiltersBuilder;
import ru.droogcompanii.application.view.activity_3.filter_activity.filter.Filter;
import ru.droogcompanii.application.view.activity_3.filter_activity.filter.WorkerWithFilters;
import ru.droogcompanii.application.view.activity_3.filter_activity.filter.WorkerWithFiltersBuilder;
import ru.droogcompanii.application.view.activity_3.filter_activity.filter.WorkerWithPartnerPointFiltersBuilder;

/**
 * Created by ls on 15.01.14.
 */

// This activity returns to previous activity list of filters
public class FilterActivity extends Activity {

    private PartnerCategory partnerCategory;
    private WorkerWithFilters<PartnerPoint> workerWithStandardFilters;
    private WorkerWithFilters<PartnerPoint> workerWithMoreFilters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3_filter);

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
        fillContainerWithFilters(R.id.containerOfStandardFilters, workerWithStandardFilters);
    }

    private void fillContainerWithFilters(int idOfContainer, WorkerWithFilters<?> workerWithFilters) {
        View viewOfFilters = workerWithFilters.prepareViewOfFilters(this);
        findViewGroupById(idOfContainer).addView(viewOfFilters);
    }

    private ViewGroup findViewGroupById(int idOfViewGroup) {
        return (ViewGroup) findViewById(idOfViewGroup);
    }

    private void initMoreFilters(Bundle bundle) {
        if (bundle == null) {
            partnerCategory = null;
        } else {
            partnerCategory = (PartnerCategory) bundle.getSerializable(Keys.partnerCategory);
        }
        initMoreFilters(partnerCategory);
    }

    private void initMoreFilters(PartnerCategory partnerCategory) {
        WorkerWithFiltersBuilder<PartnerPoint> builder;
        if (partnerCategory == null) {
            builder = new DummyWorkerWithPartnerPointFiltersBuilder();
        } else {
            builder = new WorkerWithPartnerPointFiltersBuilder(partnerCategory);
        }
        workerWithMoreFilters = builder.build(this);
        fillContainerWithFilters(R.id.containerOfMoreFilters, workerWithMoreFilters);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(Keys.partnerCategory, partnerCategory);
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(Keys.filters, getFilters());
        setResult(RESULT_OK, returnIntent);
        super.onBackPressed();
    }

    private Serializable getFilters() {
        List<Filter<PartnerPoint>> filters = getStandardFilters();
        filters.addAll(getMoreFilters());
        return (Serializable) filters;
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
