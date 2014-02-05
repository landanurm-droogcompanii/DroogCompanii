package ru.droogcompanii.application.ui.fragment.filter_fragment.filters;

import android.content.SharedPreferences;
import android.view.View;

import java.io.Serializable;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerCategory;
import ru.droogcompanii.application.ui.fragment.filter_fragment.Filter;
import ru.droogcompanii.application.ui.fragment.filter_fragment.FilterSet;

/**
 * Created by ls on 24.01.14.
 */
public class Filters implements Filter, Serializable {
    private final StandardFilters standardFilters;
    private final MoreFilters moreFilters;

    public Filters(PartnerCategory partnerCategory) {
        standardFilters = new StandardFilters();
        moreFilters = new MoreFilters(partnerCategory);
    }

    public Filters(PartnerCategory partnerCategory, SharedPreferences sharedPreferences) {
        this(partnerCategory);
        restoreFrom(sharedPreferences);
    }

    @Override
    public void restoreFrom(SharedPreferences sharedPreferences) {
        standardFilters.restoreFrom(sharedPreferences);
        moreFilters.restoreFrom(sharedPreferences);
    }

    @Override
    public void saveInto(SharedPreferences.Editor editor) {
        standardFilters.saveInto(editor);
        moreFilters.saveInto(editor);
    }

    @Override
    public void displayOn(View containerOfFilters) {
        standardFilters.displayOn(containerOfFilters.findViewById(R.id.containerOfStandardFilters));
        moreFilters.displayOn(containerOfFilters.findViewById(R.id.containerOfMoreFilters));
    }

    @Override
    public void readFrom(View containerOfFilters) {
        standardFilters.readFrom(containerOfFilters.findViewById(R.id.containerOfStandardFilters));
        moreFilters.readFrom(containerOfFilters.findViewById(R.id.containerOfMoreFilters));
    }

    @Override
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

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("\n=======================\n");
        result.append("\nStandardFilters:\n");
        result.append(standardFilters.toString());
        result.append("\nMoreFilters:\n");
        result.append(moreFilters.toString());
        result.append("\n=======================\n---\n");
        return result.toString();
    }
}
