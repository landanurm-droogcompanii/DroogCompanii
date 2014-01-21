package ru.droogcompanii.application.view.fragment.filter_fragment.standard_filters.state;

import android.content.SharedPreferences;
import android.view.View;
import android.widget.CheckBox;

import ru.droogcompanii.application.R;

/**
 * Created by ls on 20.01.14.
 */
public class StandardFiltersStateReader {

    private final FiltersStateReader stateReader;

    public StandardFiltersStateReader(View viewOfFilters) {
        stateReader = new FiltersStateReaderFromView(viewOfFilters);
    }

    public StandardFiltersStateReader(SharedPreferences sharedPreferences) {
        stateReader = new FiltersStateReaderFromSharedPreferences(sharedPreferences);
    }

    public StandardFiltersState read() {
        return stateReader.read();
    }
}


interface FiltersStateReader {
    StandardFiltersState read();
}

class FiltersStateReaderFromView implements FiltersStateReader {
    private final View viewOfFilters;

    FiltersStateReaderFromView(View viewOfFilters) {
        this.viewOfFilters = viewOfFilters;
    }

    @Override
    public StandardFiltersState read() {
        StandardFiltersState state = new StandardFiltersState();
        state.sortByTitle = checkBoxIsMarked(R.id.sortByTitleCheckBox);
        state.sortByDistance = checkBoxIsMarked(R.id.sortByDistanceCheckBox);
        state.sortByDiscount = checkBoxIsMarked(R.id.sortByDiscountCheckBox);
        state.cashlessPayments = checkBoxIsMarked(R.id.cashlessPaymentsCheckBox);
        state.worksNow = checkBoxIsMarked(R.id.worksNowCheckBox);
        state.discountTypeBonus = checkBoxIsMarked(R.id.bonusCheckBox);
        state.discountTypeDiscount = checkBoxIsMarked(R.id.discountCheckBox);
        state.discountTypeCashBack = checkBoxIsMarked(R.id.cashBackCheckBox);
        return state;
    }

    private boolean checkBoxIsMarked(int idOfCheckBox) {
        CheckBox checkBox = (CheckBox) viewOfFilters.findViewById(idOfCheckBox);
        return checkBox.isChecked();
    }
}

class FiltersStateReaderFromSharedPreferences implements FiltersStateReader {
    private final SharedPreferences sharedPreferences;

    FiltersStateReaderFromSharedPreferences(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public StandardFiltersState read() {
        StandardFiltersState state = new StandardFiltersState();
        state.restoreFrom(sharedPreferences);
        return state;
    }
}
