package ru.droogcompanii.application.data.data_structure.working_hours;

import ru.droogcompanii.application.data.data_structure.working_hours.working_hours_impl.WorkingHoursBuilder;

/**
 * Created by Leonid on 19.12.13.
 */
public class WorkingHoursParser {

    private static final int NUMBER_OF_WORKING_HOURS_COMPONENTS = 2;
    private static final int NUMBER_OF_TIME_COMPONENTS = 2;
    private static final String SEPARATOR_OF_WORKING_HOURS_COMPONENTS = "-";
    private static final String SEPARATOR_OF_TIME_COMPONENTS = ":";

    private WorkingHoursBuilder workingHoursBuilder;


    public WorkingHoursParser() {
        workingHoursBuilder = new WorkingHoursBuilder();
    }

    public WorkingHours parse(String workingHoursText) {
        try {
            return tryParseWorkingHoursOnBusinessDay(workingHoursText);
        } catch (Exception e) {
            return workingHoursBuilder.onHoliday(workingHoursText);
        }
    }

    private WorkingHours tryParseWorkingHoursOnBusinessDay(String workingHoursText) {
        workingHoursText = workingHoursText.trim();
        String[] workingHoursComponents = workingHoursText.split(SEPARATOR_OF_WORKING_HOURS_COMPONENTS);
        if (workingHoursComponents.length != NUMBER_OF_WORKING_HOURS_COMPONENTS) {
            throw new IllegalArgumentException();
        }
        Time from = timeFromText(workingHoursComponents[0]);
        Time to = timeFromText(workingHoursComponents[1]);
        return workingHoursBuilder.onBusinessDay(from, to);
    }

    private Time timeFromText(String timeText) {
        timeText = timeText.trim();
        String[] timeComponents = timeText.split(SEPARATOR_OF_TIME_COMPONENTS);
        return timeFromTimeComponents(timeComponents);
    }

    private Time timeFromTimeComponents(String[] timeComponents) {
        if (timeComponents.length != NUMBER_OF_TIME_COMPONENTS) {
            throw new IllegalArgumentException();
        }
        int hours = parseHours(timeComponents[0]);
        int minutes = parseMinutes(timeComponents[1]);
        return new Time(hours, minutes);
    }

    private int parseHours(String hoursText) {
        return parseTwoDigitNumberTextAtRange(hoursText, 0, 23);
    }

    private int parseMinutes(String minutesText) {
        return parseTwoDigitNumberTextAtRange(minutesText, 0, 59);
    }

    private int parseTwoDigitNumberTextAtRange(String numberText, int fromIncluded, int toIncluded) {
        numberText = numberText.trim();
        checkWhetherTextIsTwoDigitNumber(numberText);
        int number = Integer.parseInt(numberText);
        boolean numberAtRange = (fromIncluded <= number) && (number <= toIncluded);
        if (!numberAtRange) {
            throw new IllegalArgumentException();
        }
        return number;
    }

    private void checkWhetherTextIsTwoDigitNumber(String numberText) {
        if (numberText.length() != 2) {
            throw new IllegalArgumentException();
        }
        char firstChar = numberText.charAt(0);
        char secondChar = numberText.charAt(1);
        if (!Character.isDigit(firstChar) || !Character.isDigit(secondChar)) {
            throw new IllegalArgumentException();
        }
    }
}
