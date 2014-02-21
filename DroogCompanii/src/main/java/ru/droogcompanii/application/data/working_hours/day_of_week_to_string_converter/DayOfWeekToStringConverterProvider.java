package ru.droogcompanii.application.data.working_hours.day_of_week_to_string_converter;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Leonid on 19.12.13.
 */
public class DayOfWeekToStringConverterProvider {

    private static DayOfWeekToStringConverter currentConverter = new SimpleDayOfWeekToStringConverter();

    public static DayOfWeekToStringConverter getCurrentConverter() {
        return currentConverter;
    }

}

class SimpleDayOfWeekToStringConverter implements DayOfWeekToStringConverter {

    private static final Map<Integer, String> abbreviatedEnglishNames = prepareAbbreviatedEnglishNames();

    private static Map<Integer, String> prepareAbbreviatedEnglishNames() {
        Map<Integer, String> names = new HashMap<Integer, String>();
        names.put(Calendar.MONDAY, "mon");
        names.put(Calendar.TUESDAY, "tue");
        names.put(Calendar.WEDNESDAY, "wed");
        names.put(Calendar.THURSDAY, "thu");
        names.put(Calendar.FRIDAY, "fri");
        names.put(Calendar.SATURDAY, "sat");
        names.put(Calendar.SUNDAY, "sun");
        return names;
    }

    @Override
    public String dayOfWeekToString(int dayOfWeek) {
        return abbreviatedEnglishNames.get(dayOfWeek);
    }
}
