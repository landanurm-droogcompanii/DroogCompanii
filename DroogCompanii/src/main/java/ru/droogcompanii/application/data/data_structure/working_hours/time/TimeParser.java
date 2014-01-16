package ru.droogcompanii.application.data.data_structure.working_hours.time;

/**
 * Created by ls on 16.01.14.
 */
public class TimeParser {

    private static final int NUMBER_OF_TIME_COMPONENTS = 2;
    private static final String SEPARATOR_OF_TIME_COMPONENTS = ":";

    public Time parse(String text) {
        text = text.trim();
        String[] timeComponents = text.split(SEPARATOR_OF_TIME_COMPONENTS);
        try {
            return timeFromTimeComponents(timeComponents);
        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot convert <" + text + "> to time");
        }
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
