package ru.droogcompanii.application.data.data_structure.working_hours.time;

import junit.framework.TestCase;

import ru.droogcompanii.application.UtilsForTest;

/**
 * Created by ls on 16.01.14.
 */
public class TestTimeRangeParser extends TestCase {

    private Time from;
    private Time to;
    private TimeRange expectedTimeRange;
    private TimeRangeParser parser;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        parser = new TimeRangeParser();
        from = new Time(9, 0);
        to = new Time(18, 30);
        expectedTimeRange = new TimeRange(from, to);
    }

    public void testParse() {
        String text = from + TimeRange.SEPARATOR + to;
        assertEquals(expectedTimeRange, parser.parse(text));
    }

    public void testParseIllegalText() {
        UtilsForTest.assertExpectedException(IllegalArgumentException.class, new Runnable() {
            @Override
            public void run() {
                String illegalText = from + "2" + TimeRange.SEPARATOR + to;
                parser.parse(illegalText);
            }
        });
    }
}
