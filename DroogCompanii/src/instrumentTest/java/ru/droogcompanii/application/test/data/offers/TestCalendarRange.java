package ru.droogcompanii.application.test.data.offers;

import junit.framework.TestCase;

import java.util.Calendar;

import ru.droogcompanii.application.util.CalendarCreator;
import ru.droogcompanii.application.data.offers.CalendarRange;
import ru.droogcompanii.application.test.TestingUtils;

public class TestCalendarRange extends TestCase {
	private Calendar from;
	private Calendar to;
	private Calendar differentFrom;
	private Calendar differentTo;
	private CalendarRange calendarRange;


	@Override
	public void setUp() throws Exception {
		super.setUp();

		from = CalendarCreator.byDate(1, Calendar.JANUARY, 2014);
		differentFrom = CalendarCreator.byDate(12, Calendar.JANUARY, 2014);
		to = CalendarCreator.byDate(10, Calendar.FEBRUARY, 2014);
		differentTo = CalendarCreator.byDate(25, Calendar.FEBRUARY, 2014);
		calendarRange = new CalendarRange(from, to);
	}
	
	public void testConstructor() {
		assertEquals(from, calendarRange.from());
		assertEquals(to, calendarRange.to());
	}
	
	public void testConstructorThrowsExceptionIfArgument_To_NotAfterArgument_From() {
		TestingUtils.assertExpectedException(IllegalArgumentException.class, new Runnable() {
            @Override
            public void run() {
                new CalendarRange(to, from);
            }
        });
	}	
	
	public void testConstructorThrowsExceptionIf_From_IsNull() {
		TestingUtils.assertExpectedException(IllegalArgumentException.class, new Runnable() {
			@Override
			public void run() {
				new CalendarRange(null, to);
			}		
		});
	}
	
	public void testConstructorThrowsExceptionIf_To_IsNull() {
		TestingUtils.assertExpectedException(IllegalArgumentException.class, new Runnable() {
			@Override
			public void run() {
				new CalendarRange(from, null);
			}		
		});
	}
	
	public void testEquals() {
		CalendarRange copy = new CalendarRange(from, to);
		assertEquals(calendarRange, copy);
	}
	
	public void testNotEqualsIf_From_IsDifferent() {
		CalendarRange notCopy = new CalendarRange(differentFrom, to);
		assertFalse(calendarRange.equals(notCopy));
	}
	
	public void testNotEqualsIf_To_IsDifferent() {
		CalendarRange notCopy = new CalendarRange(from, differentTo);
		assertFalse(calendarRange.equals(notCopy));
	}
	
	public void testHashCodesOfTwoEqualObjectsAreEqual() {
		CalendarRange copy = new CalendarRange(from, to);
		assertEquals(calendarRange.hashCode(), copy.hashCode());
	}
	
}
