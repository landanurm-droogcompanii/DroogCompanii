package ru.droogcompanii.application.ui.fragment.filter_fragment.filters;

import android.content.SharedPreferences;
import android.view.View;

import java.io.Serializable;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerCategory;
import ru.droogcompanii.application.ui.fragment.filter_fragment.FilterSet;

/**
 * Created by ls on 24.01.14.
 */
public class Filters implements Serializable {
    private StandardFilters standardFilters;
    private MoreFilters moreFilters;

    public Filters(PartnerCategory partnerCategory) {
        standardFilters = new StandardFilters();
        moreFilters = new MoreFilters(partnerCategory);
    }

    public Filters(PartnerCategory partnerCategory, SharedPreferences sharedPreferences) {
        this(partnerCategory);
        restoreFrom(sharedPreferences);
    }

    public void restoreFrom(SharedPreferences sharedPreferences) {
        standardFilters.restoreFrom(sharedPreferences);
        moreFilters.restoreFrom(sharedPreferences);
    }

    public void saveInto(SharedPreferences.Editor editor) {
        standardFilters.saveInto(editor);
        moreFilters.saveInto(editor);
    }

    public void displayOn(View containerOfFilters) {
        standardFilters.displayOn(containerOfFilters.findViewById(R.id.containerOfStandardFilters));
        moreFilters.displayOn(containerOfFilters.findViewById(R.id.containerOfMoreFilters));
    }

    public void readFrom(View containerOfFilters) {
        standardFilters.readFrom(containerOfFilters.findViewById(R.id.containerOfStandardFilters));
        moreFilters.readFrom(containerOfFilters.findViewById(R.id.containerOfMoreFilters));
    }

    public void includeIn(FilterSet filterSet) {
        standardFilters.includeIn(filterSet);
        moreFilters.includeIn(filterSet);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Filters other = (Filters) obj;
        return (standardFilters.equals(other.standardFilters) &&
                moreFilters.equals(other.moreFilters));
    }

    @Override
    public int hashCode() {
        int result = standardFilters != null ? standardFilters.hashCode() : 0;
        result = 31 * result + (moreFilters != null ? moreFilters.hashCode() : 0);
        return result;
    }
}
