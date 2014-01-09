package ru.droogcompanii.application.data.data_structure.working_hours;

import junit.framework.TestCase;

import ru.droogcompanii.application.data.data_structure.working_hours.working_hours_impl.WorkingHoursBuilder;
import ru.droogcompanii.application.data.data_structure.working_hours.working_hours_impl.WorkingHoursOnBusinessDay;
import ru.droogcompanii.application.data.data_structure.working_hours.working_hours_impl.WorkingHoursOnHoliday;

/**
 * Created by ls on 09.01.14.
 */
public class TestWorkingHoursParser extends TestCase {

    private WorkingHoursParser parser;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        parser = new WorkingHoursParser();
    }

    public void testParseWorkingHoursOnHoliday() {
        String text = "Message";
        WorkingHours expectedWorkingHours = new WorkingHoursOnHoliday(text);
        assertEquals(expectedWorkingHours, parser.parse(text));
    }

    public void testParseWorkingHoursOnBusinessDay() {
        Time from = new Time(9, 0);
        Time to = new Time(18, 30);
        String text = from + WorkingHoursOnBusinessDay.SEPARATOR_BETWEEN_TIMES + to;
        WorkingHours expectedWorkingHours = new WorkingHoursOnBusinessDay(from, to);
        assertEquals(expectedWorkingHours, parser.parse(text));
    }

    public void testParseDayAndNightWorkingHours() {
        final Time TIME_00_00 = new Time(0, 0);
        String text = TIME_00_00 + WorkingHoursOnBusinessDay.SEPARATOR_BETWEEN_TIMES + TIME_00_00;
        WorkingHours expectedWorkingHours = new WorkingHoursBuilder().onBusinessDay(TIME_00_00, TIME_00_00);
        assertEquals(expectedWorkingHours, parser.parse(text));
    }
}
