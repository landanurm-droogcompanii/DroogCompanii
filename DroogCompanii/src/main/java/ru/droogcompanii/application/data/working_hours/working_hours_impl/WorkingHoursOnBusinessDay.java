package ru.droogcompanii.application.data.working_hours.working_hours_impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ru.droogcompanii.application.data.time.TimeOfDay;
import ru.droogcompanii.application.data.time.TimeRangeIncludedExcluded;
import ru.droogcompanii.application.data.working_hours.WorkingHours;
import ru.droogcompanii.application.util.StringsCombiner;

/**
 * Created by ls on 17.01.14.
 */
public class WorkingHoursOnBusinessDay implements WorkingHours, Serializable {

    private final List<TimeRangeIncludedExcluded> includedRanges;
    private final List<TimeRangeIncludedExcluded> excludedRanges;

    public WorkingHoursOnBusinessDay() {
        includedRanges = new ArrayList<TimeRangeIncludedExcluded>();
        excludedRanges = new ArrayList<TimeRangeIncludedExcluded>();
    }

    @Override
    public boolean includes(TimeOfDay time) {
        for (TimeRangeIncludedExcluded excludedRange : excludedRanges) {
            if (excludedRange.includes(time)) {
                return false;
            }
        }
        for (TimeRangeIncludedExcluded includedRange : includedRanges) {
            if (includedRange.includes(time)) {
                return true;
            }
        }
        return false;
    }

    public WorkingHoursOnBusinessDay include(TimeRangeIncludedExcluded timeRange) {
        includedRanges.add(timeRange);
        return this;
    }

    public WorkingHoursOnBusinessDay exclude(TimeRangeIncludedExcluded timeRange) {
        excludedRanges.add(timeRange);
        return this;
    }

    @Override
    public String toString() {
        String includedComponent = StringsCombiner.combine(includedRanges, ", ");
        if (excludedRanges.isEmpty()) {
            return includedComponent;
        } else {
            return includedComponent + " (" + StringsCombiner.combine(excludedRanges, ", ") + ")";
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof WorkingHoursOnBusinessDay)) {
            return false;
        }
        WorkingHoursOnBusinessDay other = (WorkingHoursOnBusinessDay) obj;
        return (includedRanges.equals(other.includedRanges) &&
                excludedRanges.equals(other.excludedRanges));
    }

    @Override
    public int hashCode() {
        return includedRanges.hashCode() + excludedRanges.hashCode();
    }

    public List<TimeRangeIncludedExcluded> getIncludedRanges() {
        return new ArrayList<TimeRangeIncludedExcluded>(includedRanges);
    }

    public List<TimeRangeIncludedExcluded> getExcludedRanges() {
        return new ArrayList<TimeRangeIncludedExcluded>(excludedRanges);
    }
}
