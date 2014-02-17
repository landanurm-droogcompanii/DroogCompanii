package ru.droogcompanii.application.ui.fragment.filter.standard.filter_impl;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.io.Serializable;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.data.searchable_sortable_listing.SearchCriterion;
import ru.droogcompanii.application.ui.fragment.filter.Filter;
import ru.droogcompanii.application.ui.fragment.filter.FilterSet;
import ru.droogcompanii.application.ui.fragment.filter.standard.search_criteria_and_comparators.partner_point.PartnerPointSearchClosestPointsCriterion;
import ru.droogcompanii.application.util.ConvertorToString;
import ru.droogcompanii.application.util.Objects;
import ru.droogcompanii.application.util.location_provider.SettingBaseLocationProvider;

/**
 * Created by ls on 14.02.14.
 */
class SearchClosestPointsFilter implements Filter, Serializable {

    private static final boolean DEFAULT_NEED_TO_SEARCH = true;
    private static final float DEFAULT_RADIUS_WITHIN_SEARCH = 8000.0f;

    private static class Key {
        public static final String NEED_TO_SEARCH = "needToSearch";
        public static final String RADIUS_WITHIN_SEARCH = "radiusWithinSearch";
    }

    private boolean needToSearch;
    private float radiusWithinSearch;

    public SearchClosestPointsFilter() {
        needToSearch = DEFAULT_NEED_TO_SEARCH;
        radiusWithinSearch = DEFAULT_RADIUS_WITHIN_SEARCH;
    }

    @Override
    public void readFrom(View view) {
        initCheckBoxListener(view);
        needToSearch = findCheckBox(view).isChecked();
        if (!needToSearch) {
            radiusWithinSearch = DEFAULT_RADIUS_WITHIN_SEARCH;
            return;
        }
        String textRadiusWithinSearch = findEditText(view).getText().toString().trim();
        try {
            radiusWithinSearch = Float.parseFloat(textRadiusWithinSearch);
        } catch (Throwable e) {
            needToSearch = false;
        }
    }

    private void initCheckBoxListener(final View view) {
        findCheckBox(view).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                updateView(view, isChecked);
            }
        });
    }

    private static void updateView(View view, boolean isChecked) {
        EditText editText = findEditText(view);
        if (isChecked) {
            editText.setVisibility(View.VISIBLE);
            requestFocusAndShowSoftKeyboard(editText);
        } else {
            hideSoftKeyboard(editText);
            editText.setVisibility(View.INVISIBLE);
        }
    }

    private static void requestFocusAndShowSoftKeyboard(EditText editText) {
        editText.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager)
                editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }

    private static void hideSoftKeyboard(EditText editText) {
        InputMethodManager inputMethodManager = (InputMethodManager)
                editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    private static CheckBox findCheckBox(View view) {
        return (CheckBox) view.findViewById(R.id.searchClosestPointsCheckBox);
    }

    private static EditText findEditText(View view) {
        return (EditText) view.findViewById(R.id.radiusWithinSearchEditText);
    }

    @Override
    public void displayOn(View view) {
        initCheckBoxListener(view);
        findCheckBox(view).setChecked(needToSearch);
        EditText editText = findEditText(view);
        editText.setText(textOfEditText());
        editText.setVisibility(visibilityOfEditText());
    }

    private String textOfEditText() {
        return needToSearch ? String.valueOf(radiusWithinSearch) : "";
    }

    private int visibilityOfEditText() {
        return needToSearch ? View.VISIBLE : View.INVISIBLE;
    }

    @Override
    public void restoreFrom(SharedPreferences sharedPreferences) {
        needToSearch = sharedPreferences.getBoolean(Key.NEED_TO_SEARCH, DEFAULT_NEED_TO_SEARCH);
        radiusWithinSearch = sharedPreferences.getFloat(Key.RADIUS_WITHIN_SEARCH, DEFAULT_RADIUS_WITHIN_SEARCH);
    }

    @Override
    public void saveInto(SharedPreferences.Editor editor) {
        editor.putBoolean(Key.NEED_TO_SEARCH, needToSearch);
        editor.putFloat(Key.RADIUS_WITHIN_SEARCH, radiusWithinSearch);
    }

    @Override
    public void includeIn(FilterSet filterSet) {
        if (needToSearch) {
            filterSet.addPartnerPointSearchCriterion(prepareSearchCriterion());
        }
    }

    private SearchCriterion<PartnerPoint> prepareSearchCriterion() {
        return new PartnerPointSearchClosestPointsCriterion(
                new SettingBaseLocationProvider(), radiusWithinSearch);
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
        return Objects.equals(needToSearch, other.needToSearch) &&
               Objects.equals(radiusWithinSearch, other.radiusWithinSearch);
    }

    @Override
    public int hashCode() {
        return Objects.hash(needToSearch, radiusWithinSearch);
    }

    @Override
    public String toString() {
        return ConvertorToString.buildFor(this)
                .withFieldNames("needToSearch", "radiusWithinSearch")
                .withFieldValues(needToSearch, radiusWithinSearch)
                .toString();
    }
}
