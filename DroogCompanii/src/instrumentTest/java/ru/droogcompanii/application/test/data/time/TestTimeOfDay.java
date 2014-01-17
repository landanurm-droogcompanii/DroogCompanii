package ru.droogcompanii.application.test.data.time;

import junit.framework.TestCase;

import java.util.Calendar;

import ru.droogcompanii.application.data.time.Time;
import ru.droogcompanii.application.data.time.TimeOfDay;
import ru.droogcompanii.application.data.time.TimeRange;
import ru.droogcompanii.application.util.IteratorOverTimes;

/**
 * Created by ls on 09.01.14.
 */
public class TestTimeOfDay extends TestCase {

    private int hours;
    private int minutes;
    private TimeOfDay time;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        hours = 3;
        minutes = 40;
        time = new TimeOfDay(hours, minutes);
    }

    public void testConstructor() {
        assertTrue(time instanceof Time);
        assertEquals(hours, time.getHours());
        assertEquals(minutes, time.getMinutes());
    }

    public void testEquals() {
        TimeOfDay copy = new TimeOfDay(hours, minutes);
        assertEquals(time, copy);
    }

    public void testEqualsToItself() {
        assertEquals(time, time);
    }

    public void testNotEqualsToNull() {
        assertFalse(time.equals(null));
    }

    public void testNotEqualsToOtherTypeObject() {
        assertFalse(time.equals("String"));
    }

    public void testNotEqualsWithDifferent_Hours() {
        assertFalse(new TimeOfDay(1, 20).equals(new TimeOfDay(2, 20)));
    }

    public void testNotEqualsWithDifferent_Minutes() {
        assertFalse(new TimeOfDay(1, 20).equals(new TimeOfDay(1, 40)));
    }

    public void testHashCode() {
        TimeOfDay copy = new TimeOfDay(hours, minutes);
        assertEquals(time.hashCode(), copy.hashCode());
    }

    public void testToString() {
        assertEquals("10:50", new TimeOfDay(10, 50).toString());
        assertEquals("00:20", new TimeOfDay(0, 20).toString());
        assertEquals("11:05", new TimeOfDay(11, 5).toString());
        assertEquals("01:03", new TimeOfDay(1, 3).toString());
    }

    public void testBefore() {
        TimeOfDay before = new TimeOfDay(1, 10);
        TimeOfDay after = new TimeOfDay(2, 20);
        assertTrue(before.before(after));
        assertFalse(after.before(before));
        assertFalse(time.before(time));
    }

    public void testAfter() {
        TimeOfDay before = new TimeOfDay(4, 50);
        TimeOfDay after = new TimeOfDay(7, 30);
        assertTrue(after.after(before));
        assertFalse(before.after(after));
        assertFalse(time.after(time));
    }

    public void testFromCalendar() {
        Calendar calendar = Calendar.getInstance();
        int calendarHoursIn24HourFormat = calendar.get(Calendar.HOUR_OF_DAY);
        int calendarMinutes = calendar.get(Calendar.MINUTE);
        TimeOfDay expectedTimeOfDay = new TimeOfDay(calendarHoursIn24HourFormat, calendarMinutes);
        assertEquals(expectedTimeOfDay, TimeOfDay.from(calendar));
    }

    public void testWithin() {
        final TimeOfDay from = new TimeOfDay(8, 0);
        final TimeOfDay to = new TimeOfDay(19, 0);
        final TimeRange timeRange = new TimeRange(from, to);
        IteratorOverTimes.iterateAllTimesIn24Hours(new IteratorOverTimes.OnEachHandler() {
            @Override
            public void onEach(TimeOfDay time) {
                boolean expectedWithin = (!from.after(time)) && (!time.after(to));
                assertEquals(expectedWithin, time.within(timeRange));
            }
        });
    }
}
