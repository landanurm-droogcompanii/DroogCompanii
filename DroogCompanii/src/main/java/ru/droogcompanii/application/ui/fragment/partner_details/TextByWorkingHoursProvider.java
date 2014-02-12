package ru.droogcompanii.application.ui.fragment.partner_details;

import java.util.List;

import ru.droogcompanii.application.data.time.TimeRangeIncludedExcluded;
import ru.droogcompanii.application.data.working_hours.WorkingHours;
import ru.droogcompanii.application.data.working_hours.working_hours_impl.WorkingHoursOnBusinessDay;
import ru.droogcompanii.application.util.StringsCombiner;

/**
 * Created by ls on 12.02.14.
 */
public class TextByWorkingHoursProvider {
    public static String getOpeningTextFrom(WorkingHours workingHours) {
        if (isNotOnBusinessDay(workingHours)) {
            return workingHours.toString();
        }
        WorkingHoursOnBusinessDay onBusinessDay = (WorkingHoursOnBusinessDay) workingHours;
        return convertRangesToString(onBusinessDay.getIncludedRanges());
    }

    private static String convertRangesToString(List<TimeRangeIncludedExcluded> ranges) {
        return StringsCombiner.combine(ranges, "\n");
    }

    private static boolean isNotOnBusinessDay(WorkingHours workingHours) {
        return !(workingHours instanceof WorkingHoursOnBusinessDay);
    }

    public static String getLunchBreakTextFrom(WorkingHours workingHours) {
        if (isNotOnBusinessDay(workingHours)) {
            return "";
        }
        WorkingHoursOnBusinessDay onBusinessDay = (WorkingHoursOnBusinessDay) workingHours;
        return convertRangesToString(onBusinessDay.getExcludedRanges());
    }
}
