package ru.droogcompanii.application.test.data.time;

import junit.framework.TestCase;

import ru.droogcompanii.application.data.time.TimeOfDay;
import ru.droogcompanii.application.data.time.TimeRange;
import ru.droogcompanii.application.data.time.TimeRangeIncludedExcluded;
import ru.droogcompanii.application.data.time.TimeRangeIncludedExcludedParser;
import ru.droogcompanii.application.test.TestingUtils;

/**
 * Created by ls on 16.01.14.
 */
public class TestTimeRangeParser extends TestCase {

    private TimeOfDay from;
    private TimeOfDay to;
    private TimeRangeIncludedExcluded expectedTimeRange;
    private TimeRangeIncludedExcludedParser parser;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        parser = new TimeRangeIncludedExcludedParser();
        from = new TimeOfDay(9, 0);
        to = new TimeOfDay(18, 30);
        expectedTimeRange = new TimeRangeIncludedExcluded(from, to);
    }

    public void testParse() {
        String text = from + TimeRange.SEPARATOR + to;
        assertEquals(expectedTimeRange, parser.parse(text));
    }

    public void testParseIllegalText() {
        TestingUtils.assertExpectedException(IllegalArgumentException.class, new Runnable() {
            @Override
            public void run() {
                String illegalText = from + "2" + TimeRange.SEPARATOR + to;
                parser.parse(illegalText);
            }
        });
    }
}
