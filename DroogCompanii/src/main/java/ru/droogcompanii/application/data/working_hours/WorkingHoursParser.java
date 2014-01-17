package ru.droogcompanii.application.data.working_hours;

import ru.droogcompanii.application.data.time.TimeRange;
import ru.droogcompanii.application.data.time.TimeRangeParser;
import ru.droogcompanii.application.data.working_hours.working_hours_impl.DayAndNightWorkingHours;
import ru.droogcompanii.application.data.working_hours.working_hours_impl.WorkingHoursOnBusinessDay;
import ru.droogcompanii.application.data.working_hours.working_hours_impl.WorkingHoursOnHoliday;
import ru.droogcompanii.application.util.DroogCompaniiStringConstants.XmlConstants.TypesOfDay;

/**
 * Created by Leonid on 19.12.13.
 */
public class WorkingHoursParser {

    public WorkingHours parse(String typeOfDay, String text) {
        if (typeOfDay.equals(TypesOfDay.usualDay)) {
            return new WorkingHoursOnBusinessDay(parseTimeRange(text));
        } else if (typeOfDay.equals(TypesOfDay.dayAndNight)) {
            return new DayAndNightWorkingHours(text);
        } else if (typeOfDay.equals(TypesOfDay.holiday)) {
            return new WorkingHoursOnHoliday(text);
        }
        throw new IllegalArgumentException("Unknown type of day: " + typeOfDay);
    }

    private TimeRange parseTimeRange(String text) {
        TimeRangeParser parser = new TimeRangeParser();
        return parser.parse(text);
    }
}
