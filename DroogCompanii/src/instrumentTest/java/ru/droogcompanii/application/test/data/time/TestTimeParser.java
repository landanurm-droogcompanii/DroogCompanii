package ru.droogcompanii.application.test.data.time;

import junit.framework.TestCase;

import ru.droogcompanii.application.data.time.TimeOfDay;
import ru.droogcompanii.application.data.time.TimeParser;
import ru.droogcompanii.application.test.UtilsForTest;
import ru.droogcompanii.application.util.IteratorOverTimes;

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
            public void onEach(TimeOfDay time) {
                assertParseTimeWorksCorrectly(time);
            }
        });
    }

    private void assertParseTimeWorksCorrectly(TimeOfDay time) {
        TimeOfDay actual = timeParser.parse(time.toString());
        TimeOfDay expected = time;
        assertEquals(expected, actual);
    }

    public void testParseIllegalText() {
        UtilsForTest.assertExpectedException(IllegalArgumentException.class, new Runnable() {
            @Override
            public void run() {
                TimeOfDay time = new TimeOfDay(15, 45);
                String illegalText = time.toString() + "1";
                timeParser.parse(illegalText);
            }
        });
    }
}
