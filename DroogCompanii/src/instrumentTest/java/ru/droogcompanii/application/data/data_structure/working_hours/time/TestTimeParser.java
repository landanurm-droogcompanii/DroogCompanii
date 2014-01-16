package ru.droogcompanii.application.data.data_structure.working_hours.time;

import junit.framework.TestCase;

import ru.droogcompanii.application.UtilsForTest;
import ru.droogcompanii.application.data.data_structure.working_hours.IteratorOverTimes;

/**
 * Created by ls on 16.01.14.
 */
public class TestTimeParser extends TestCase {
    private TimeParser timeParser;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        timeParser = new TimeParser();
    }

    public void testParse() {
        IteratorOverTimes.iterateAllTimesIn24Hours(new IteratorOverTimes.OnEachHandler() {
            @Override
            public void onEach(Time time) {
                assertParseTimeWorksCorrectly(time);
            }
        });
    }

    private void assertParseTimeWorksCorrectly(Time time) {
        Time actual = timeParser.parse(time.toString());
        Time expected = time;
        assertEquals(expected, actual);
    }

    public void testParseIllegalText() {
        UtilsForTest.assertExpectedException(IllegalArgumentException.class, new Runnable() {
            @Override
            public void run() {
                Time time = new Time(15, 45);
                String illegalText = time.toString() + "1";
                timeParser.parse(illegalText);
            }
        });
    }
}
