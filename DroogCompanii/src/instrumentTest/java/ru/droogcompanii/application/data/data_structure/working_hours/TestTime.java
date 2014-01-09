package ru.droogcompanii.application.data.data_structure.working_hours;

import com.google.android.gms.internal.be;

import junit.framework.TestCase;

import java.util.Calendar;

/**
 * Created by ls on 09.01.14.
 */
public class TestTime extends TestCase {

    private int hours;
    private int minutes;
    private Time time;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        hours = 3;
        minutes = 40;
        time = new Time(hours, minutes);
    }

    public void testConstructor() {
        assertEquals(hours, time.hours);
        assertEquals(minutes, time.minutes);
    }

    public void testEquals() {
        Time copy = new Time(hours, minutes);
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
        assertFalse(new Time(1, 20).equals(new Time(2, 20)));
    }

    public void testNotEqualsWithDifferent_Minutes() {
        assertFalse(new Time(1, 20).equals(new Time(1, 40)));
    }

    public void testHashCode() {
        Time copy = new Time(hours, minutes);
        assertEquals(time.hashCode(), copy.hashCode());
    }

    public void testToString() {
        assertEquals("10:50", new Time(10, 50).toString());
        assertEquals("00:20", new Time(0, 20).toString());
        assertEquals("11:05", new Time(11, 5).toString());
        assertEquals("01:03", new Time(1, 3).toString());
    }

    public void testBefore() {
        Time before = new Time(1, 10);
        Time after = new Time(2, 20);
        assertTrue(before.before(after));
        assertFalse(after.before(before));
        assertFalse(time.before(time));
    }

    public void testAfter() {
        Time before = new Time(4, 50);
        Time after = new Time(7, 30);
        assertTrue(after.after(before));
        assertFalse(before.after(after));
        assertFalse(time.after(time));
    }

    public void testFromCalendar() {
        Calendar calendar = Calendar.getInstance();
        int calendarHoursIn24HourFormat = calendar.get(Calendar.HOUR_OF_DAY);
        int calendarMinutes = calendar.get(Calendar.MINUTE);
        Time expectedTime = new Time(calendarHoursIn24HourFormat, calendarMinutes);
        assertEquals(expectedTime, Time.from(calendar));
    }

    public void testWithin() {
        final Time from = new Time(8, 0);
        final Time to = new Time(19, 0);
        IteratorOverTimes.iterateAllTimesIn24Hours(new IteratorOverTimes.OnEachHandler() {
            @Override
            public void onEach(Time time) {
                boolean expectedWithin = (!from.after(time)) && (!time.after(to));
                assertEquals(expectedWithin, time.within(from, to));
            }
        });
    }
}
