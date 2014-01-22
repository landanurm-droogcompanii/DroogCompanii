package ru.droogcompanii.application.data.working_hours.parser;

import java.util.ArrayList;
import java.util.Collection;

import ru.droogcompanii.application.data.time.TimeRangeIncludedExcluded;
import ru.droogcompanii.application.data.time.TimeRangeIncludedExcludedParser;
import ru.droogcompanii.application.data.working_hours.working_hours_impl.WorkingHoursOnBusinessDay;

/**
 * Created by ls on 17.01.14.
 */
class WorkingHoursOnBusinessDayParser {

    private static final String FORMAT_OF_WORKING_HOURS =
            "[range1],[range2],...,[rangeN] ([rangeX1],[rangeX2],...,[rangeXM])\n" +
            "[range1]-[rangeN]  --  ranges of working hours\n" +
            "[rangeX1]-[rangeXM]  --  ranges of lunch hours";

    private static final char START_OF_LUNCH_HOURS = '(';
    private static final char END_OF_LUNCH_HOURS = ')';
    private static final String SEPARATOR_OF_TIME_RANGES = ",";

    private static final TimeRangeIncludedExcludedParser timeRangeParser = new TimeRangeIncludedExcludedParser();


    public WorkingHoursOnBusinessDay parse(String textToParse) {
        textToParse = textToParse.trim();
        if (textToParse.indexOf(START_OF_LUNCH_HOURS) != -1) {
            return parseWithLunchHours(textToParse);
        } else {
            return parseWithoutLunchHours(textToParse);
        }
    }

    private WorkingHoursOnBusinessDay parseWithoutLunchHours(String textToParse) {
        try {
        } catch (Exception e) {
            throw createIllegalTextToParseException(textToParse);
        }
        return tryParseWithoutLunchHours(textToParse);
    }

    private RuntimeException createIllegalTextToParseException(String textToParse) {
        return new IllegalArgumentException(
                "Illegal text to parse:\n" + textToParse + "\n\n" +
                "Text must correspond to a format:\n" + FORMAT_OF_WORKING_HOURS
        );
    }

    private WorkingHoursOnBusinessDay tryParseWithoutLunchHours(String textToParse) {
        WorkingHoursOnBusinessDay workingHours = new WorkingHoursOnBusinessDay();
        Collection<TimeRangeIncludedExcluded> ranges = parseAtLeastOneTimeRange(textToParse);
        for (TimeRangeIncludedExcluded timeRange : ranges) {
            workingHours.include(timeRange);
        }
        return workingHours;
    }

    private Collection<TimeRangeIncludedExcluded> parseAtLeastOneTimeRange(String textToParse) {
        String[] timeRanges = textToParse.split(SEPARATOR_OF_TIME_RANGES);
        if (timeRanges.length == 0) {
            throw new IllegalArgumentException();
        }
        return parseTimeRanges(timeRanges);
    }

    private Collection<TimeRangeIncludedExcluded> parseTimeRanges(String[] timeRanges) {
        Collection<TimeRangeIncludedExcluded> result = new ArrayList<TimeRangeIncludedExcluded>(timeRanges.length);
        for (String timeRange : timeRanges) {
            result.add(timeRangeParser.parse(timeRange));
        }
        return result;
    }

    private WorkingHoursOnBusinessDay parseWithLunchHours(String textToParse) {
        int startOfLunchHoursIndex = textToParse.indexOf(START_OF_LUNCH_HOURS);
        int endOfLunchHoursIndex = textToParse.indexOf(END_OF_LUNCH_HOURS);
        if ((startOfLunchHoursIndex + 1 > endOfLunchHoursIndex - 1) ||
            (endOfLunchHoursIndex != textToParse.length() - 1)) {

            throw createIllegalTextToParseException(textToParse);
        }
        String workingHoursComponent = textToParse.substring(0, startOfLunchHoursIndex).trim();
        WorkingHoursOnBusinessDay workingHours = parseWithoutLunchHours(workingHoursComponent);
        String lunchHoursComponent = textToParse.substring(startOfLunchHoursIndex + 1, endOfLunchHoursIndex).trim();
        Collection<TimeRangeIncludedExcluded> timeRangesToExclude = parseAtLeastOneTimeRange(lunchHoursComponent);
        for (TimeRangeIncludedExcluded timeRange : timeRangesToExclude) {
            workingHours.exclude(timeRange);
        }
        return workingHours;
    }
}
