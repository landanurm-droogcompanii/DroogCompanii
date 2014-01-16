package ru.droogcompanii.application.data.data_structure.working_hours;

import ru.droogcompanii.application.data.data_structure.working_hours.time.TimeRange;
import ru.droogcompanii.application.data.data_structure.working_hours.time.TimeRangeParser;
import ru.droogcompanii.application.data.data_structure.working_hours.working_hours_impl.DayAndNightWorkingHours;
import ru.droogcompanii.application.data.data_structure.working_hours.working_hours_impl.WorkingHoursOnBusinessDay;
import ru.droogcompanii.application.data.data_structure.working_hours.working_hours_impl.WorkingHoursOnHoliday;
import ru.droogcompanii.application.util.DroogCompaniiStringConstants.XmlConstants.TypesOfDay;

/**
 * Created by Leonid on 19.12.13.
 */
public class WorkingHoursParser {

    private static final int NUMBER_OF_WORKING_HOURS_COMPONENTS = 2;
    private static final int NUMBER_OF_TIME_COMPONENTS = 2;
    private static final String SEPARATOR_OF_WORKING_HOURS_COMPONENTS = "-";
    private static final String SEPARATOR_OF_TIME_COMPONENTS = ":";


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
