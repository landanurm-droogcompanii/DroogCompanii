package ru.droogcompanii.application.view.fragment.filter_fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.droogcompanii.application.SharedPreferencesProvider;
import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerCategory;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.util.Keys;
import ru.droogcompanii.application.view.fragment.filter_fragment.standard_filters.WorkerWithStandardPartnerPointFilters;
import ru.droogcompanii.application.view.fragment.filter_fragment.worker_with_filters.WorkerWithFilters;
import ru.droogcompanii.application.view.fragment.filter_fragment.worker_with_filters.WorkerWithFiltersBuilder;
import ru.droogcompanii.application.view.fragment.filter_fragment.worker_with_filters.WorkerWithPartnerPointFiltersBuilder;

/**
 * Created by ls on 21.01.14.
 */
public class FilterFragment extends android.support.v4.app.Fragment {

    private PartnerCategory partnerCategory;
    private SharedPreferences sharedPreferences;
    private View viewOfStandardFilters;
    private View viewOfMoreFilters;
    private WorkerWithFilters<PartnerPoint> workerWithStandardFilters;
    private WorkerWithFilters<PartnerPoint> workerWithMoreFilters;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_filter, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        sharedPreferences = SharedPreferencesProvider.get(getActivity());

        initStandardFilters();

        if (savedInstanceState == null) {
            initMoreFilters(getArguments());
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
        View viewOfFilters = workerWithFilters.prepareViewOfFilters(getActivity(), sharedPreferences);
        findViewGroupById(idOfContainer).addView(viewOfFilters);
        return viewOfFilters;
    }

    private ViewGroup findViewGroupById(int idOfViewGroup) {
        return (ViewGroup) getActivity().findViewById(idOfViewGroup);
    }

    private void initMoreFilters(Bundle bundle) {
        partnerCategory = extractPartnerCategoryFrom(bundle);
        WorkerWithFiltersBuilder<PartnerPoint> builder = new WorkerWithPartnerPointFiltersBuilder(partnerCategory);
        workerWithMoreFilters = builder.build(getActivity());
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
    public void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        workerWithStandardFilters.saveInto(editor, viewOfStandardFilters);
        workerWithMoreFilters.saveInto(editor, viewOfMoreFilters);
        editor.commit();
    }

    public Filters getFilters() {
        Filters filters = getStandardFilters();
        filters.add(getMoreFilters());
        return filters;
    }

    private Filters getStandardFilters() {
        return readFilters(R.id.containerOfStandardFilters, workerWithStandardFilters);
    }

    private Filters getMoreFilters() {
        return readFilters(R.id.containerOfMoreFilters, workerWithMoreFilters);
    }

    private Filters readFilters(int idOfContainer, WorkerWithFilters<PartnerPoint> workerWithFilters) {
        View containerOfFilters = findViewGroupById(idOfContainer);
        return workerWithFilters.readFilters(containerOfFilters);
    }
}
