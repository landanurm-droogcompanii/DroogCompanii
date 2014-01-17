package ru.droogcompanii.application.data.working_hours.working_hours_impl;

import java.util.HashSet;
import java.util.Set;

import ru.droogcompanii.application.data.time.TimeOfDay;
import ru.droogcompanii.application.data.time.TimeRange;
import ru.droogcompanii.application.data.working_hours.WorkingHours;

/**
 * Created by ls on 17.01.14.
 */
public class WorkingHoursOnBusinessDayNewGen implements WorkingHours {

    private final Set<TimeRange> includedRanges;
    private final Set<TimeRange> excludedRanges;
    private String text;

    public WorkingHoursOnBusinessDayNewGen() {
        includedRanges = new HashSet<TimeRange>();
        excludedRanges = new HashSet<TimeRange>();
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

    public void include(TimeRange timeRange) {
        includedRanges.add(timeRange);
    }

    public void exclude(TimeRange timeRange) {
        excludedRanges.add(timeRange);
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        if (text != null) {
            return text;
        }
        return includedRanges.toString() + " (" + excludedRanges.toString() + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof WorkingHoursOnBusinessDayNewGen)) {
            return false;
        }
        WorkingHoursOnBusinessDayNewGen other = (WorkingHoursOnBusinessDayNewGen) obj;
        return (includedRanges.equals(other.includedRanges) &&
                excludedRanges.equals(other.excludedRanges));
    }

    @Override
    public int hashCode() {
        return includedRanges.hashCode() + excludedRanges.hashCode();
    }
}
