package ru.droogcompanii.application.data.working_hours.working_hours_impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ru.droogcompanii.application.data.time.TimeOfDay;
import ru.droogcompanii.application.data.time.TimeRange;
import ru.droogcompanii.application.data.working_hours.WorkingHours;
import ru.droogcompanii.application.util.StringsCombiner;

/**
 * Created by ls on 17.01.14.
 */
public class WorkingHoursOnBusinessDay implements WorkingHours, Serializable {

    private final List<TimeRange> includedRanges;
    private final List<TimeRange> excludedRanges;

    public WorkingHoursOnBusinessDay() {
        includedRanges = new ArrayList<TimeRange>();
        excludedRanges = new ArrayList<TimeRange>();
    }

    @Override
    public boolean includes(TimeOfDay time) {
        for (TimeRange excludedRange : excludedRanges) {
            if (excludedRange.includes(time)) {
                return false;
            }
        }
        for (TimeRange includedRange : includedRanges) {
            if (includedRange.includes(time)) {
                return true;
            }
        }
        return false;
    }

    public WorkingHoursOnBusinessDay include(TimeRange timeRange) {
        includedRanges.add(timeRange);
        return this;
    }

    public WorkingHoursOnBusinessDay exclude(TimeRange timeRange) {
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

    public List<TimeRange> getIncludedRanges() {
        return new ArrayList<TimeRange>(includedRanges);
    }

    public List<TimeRange> getExcludedRanges() {
        return new ArrayList<TimeRange>(excludedRanges);
    }
}
