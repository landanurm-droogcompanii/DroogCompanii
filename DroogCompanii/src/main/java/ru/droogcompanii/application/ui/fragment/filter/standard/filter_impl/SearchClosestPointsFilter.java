package ru.droogcompanii.application.ui.fragment.filter.standard.filter_impl;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.data.searchable_sortable_listing.SearchCriterion;
import ru.droogcompanii.application.ui.fragment.filter.Filter;
import ru.droogcompanii.application.ui.fragment.filter.FilterSet;
import ru.droogcompanii.application.ui.fragment.filter.standard.search_criteria_and_comparators.partner_point.PartnerPointSearchClosestPointsCriterion;
import ru.droogcompanii.application.util.location.ActualBaseLocationProvider;
import ru.droogcompanii.application.util.constants.ApiVersionUtils;
import ru.droogcompanii.application.util.ConverterToString;

/**
 * Created by ls on 14.02.14.
 */
class SearchClosestPointsFilter implements Filter, Serializable {

    private static class DistanceOption {
        public final boolean needToSearch;
        public final float radiusWithinSearch;
        public final int id;
        public final int textId;

        public static DistanceOption noNeedToSearch() {
            return new DistanceOption(false, 0.0f,
                    R.id.distanceOption_NoNeedToSearch,
                    R.string.distanceOption_No);
        }

        public static DistanceOption from(float radius, int id, int textId) {
            return new DistanceOption(true, radius, id, textId);
        }

        private DistanceOption(boolean needToSearch, float radiusWithinSearch, int id, int textId) {
            this.needToSearch = needToSearch;
            this.radiusWithinSearch = radiusWithinSearch;
            this.id = id;
            this.textId = textId;
        }
    }

    private static final List<DistanceOption> DISTANCE_OPTIONS = Arrays.asList(
            DistanceOption.noNeedToSearch(),
            DistanceOption.from(1000.0f, R.id.distanceOption_1000meters, R.string.distanceOption_1000meters),
            DistanceOption.from(3000.0f, R.id.distanceOption_3000meters, R.string.distanceOption_3000meters),
            DistanceOption.from(5000.0f, R.id.distanceOption_5000meters, R.string.distanceOption_5000meters)
    );

    private static final String KEY_ID_OF_DISTANCE_OPTION = "IdOfDistanceOption";

    private static final int DEFAULT_ID_OF_DISTANCE_OPTION = DISTANCE_OPTIONS.get(0).id;

    private int idOfDistanceOption;


    public SearchClosestPointsFilter() {
        idOfDistanceOption = DEFAULT_ID_OF_DISTANCE_OPTION;
    }

    @Override
    public void readFrom(View view) {
        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.distanceOptionRadioGroup);
        idOfDistanceOption = radioGroup.getCheckedRadioButtonId();
    }

    @Override
    public void displayOn(View view) {
        ViewGroup container = (ViewGroup) view.findViewById(R.id.containerOfSearchClosestPointsFilter);
        RadioGroup radioGroup = (RadioGroup) container.findViewById(R.id.distanceOptionRadioGroup);
        if (radioGroup == null) {
            radioGroup = insertRadioGroup(container);
        }
        radioGroup.check(idOfDistanceOption);
    }

    private RadioGroup insertRadioGroup(ViewGroup container) {
        RadioGroup radioGroup = prepareRadioGroup(container.getContext());
        final int WIDTH = ViewGroup.LayoutParams.MATCH_PARENT;
        final int HEIGHT = ViewGroup.LayoutParams.WRAP_CONTENT;
        container.addView(radioGroup, new ViewGroup.LayoutParams(WIDTH, HEIGHT));
        return radioGroup;
    }

    private RadioGroup prepareRadioGroup(Context context) {
        RadioGroup radioGroup = new RadioGroup(context);
        radioGroup.setId(R.id.distanceOptionRadioGroup);
        radioGroup.setOrientation(RadioGroup.HORIZONTAL);

        radioGroup.setWeightSum(DISTANCE_OPTIONS.size());
        setSpaceBetweenRadioButtons(radioGroup);

        RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(
                RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT, 1.0f
        );

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        for (DistanceOption each : DISTANCE_OPTIONS) {
            RadioButton radioButton = prepareRadioButton(layoutInflater, each);
            radioGroup.addView(radioButton, layoutParams);
        }
        return radioGroup;
    }

    @SuppressLint("NewApi")
    private static void setSpaceBetweenRadioButtons(RadioGroup radioGroup) {
        if (ApiVersionUtils.isCurrentVersionLowerThan(14)) {
            return;
        }
        radioGroup.setShowDividers(RadioGroup.SHOW_DIVIDER_MIDDLE);
        Resources res = radioGroup.getResources();
        int dividerPadding = res.getDimensionPixelSize(R.dimen.dividerPaddingOfRadioGroup);
        radioGroup.setDividerPadding(dividerPadding);
    }

    private RadioButton prepareRadioButton(LayoutInflater layoutInflater, DistanceOption distanceOption) {
        RadioButton radioButton = (RadioButton)
                layoutInflater.inflate(R.layout.view_distance_option_radio_button, null);
        radioButton.setText(distanceOption.textId);
        radioButton.setId(distanceOption.id);
        setMinHeight(radioButton, R.dimen.minHeightOfDistanceOptionRadioButton);
        return radioButton;
    }

    private void setMinHeight(RadioButton radioButton, int minHeightResId) {
        Resources res = radioButton.getContext().getResources();
        int minHeight = res.getDimensionPixelSize(minHeightResId);
        if (radioButton.getHeight() < minHeight) {
            radioButton.setHeight(minHeight);
        }
    }

    @Override
    public void restoreFrom(SharedPreferences sharedPreferences) {
        idOfDistanceOption = sharedPreferences.getInt(KEY_ID_OF_DISTANCE_OPTION, DEFAULT_ID_OF_DISTANCE_OPTION);
    }

    @Override
    public void saveInto(SharedPreferences.Editor editor) {
        editor.putInt(KEY_ID_OF_DISTANCE_OPTION, idOfDistanceOption);
    }

    @Override
    public void includeIn(FilterSet filterSet) {
        DistanceOption option = getCurrentDistanceOption();
        if (option.needToSearch) {
            SearchCriterion<PartnerPoint> searchCriterion =
                    prepareSearchCriterion(option.radiusWithinSearch);
            filterSet.addPartnerPointSearchCriterion(searchCriterion);
        }
    }

    private DistanceOption getCurrentDistanceOption() {
        for (DistanceOption each : DISTANCE_OPTIONS) {
            if (each.id == idOfDistanceOption) {
                return each;
            }
        }
        throw new IllegalStateException("cannot find DistanceOption with id: idOfDistanceOption");
    }

    private SearchCriterion<PartnerPoint> prepareSearchCriterion(float radiusWithinSearch) {
        return new PartnerPointSearchClosestPointsCriterion(
                new ActualBaseLocationProvider(), radiusWithinSearch);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        SearchClosestPointsFilter other = (SearchClosestPointsFilter) obj;
        return idOfDistanceOption == other.idOfDistanceOption;
    }

    @Override
    public int hashCode() {
        return idOfDistanceOption;
    }

    @Override
    public String toString() {
        return ConverterToString.buildFor(this)
                .withFieldNames("idOfDistanceOption")
                .withFieldValues(idOfDistanceOption)
                .toString();
    }
}
