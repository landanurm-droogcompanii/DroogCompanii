package ru.droogcompanii.application.data.data_structure.working_hours;

import ru.droogcompanii.application.data.data_structure.working_hours.working_hours_impl.WorkingHoursBuilder;

/**
 * Created by Leonid on 19.12.13.
 */
public class WorkingHoursParser {
    private WorkingHoursBuilder workingHoursBuilder;

    private static final int NUMBER_OF_TIME_COMPONENTS = 2;

    public WorkingHoursParser() {
        workingHoursBuilder = new WorkingHoursBuilder();
    }

    public WorkingHours parse(String workingHoursText) {
        try {
            WorkingHours workingHoursOnBusinessDay = tryParseWorkingHoursOnBusinessDay(workingHoursText);
            return workingHoursOnBusinessDay;
        } catch (Exception e) {
            return workingHoursBuilder.onHoliday(workingHoursText);
        }
    }

    private WorkingHours tryParseWorkingHoursOnBusinessDay(String workingHoursText) {
        String[] workingHoursComponents = workingHoursText.split("-");
        String fromText = workingHoursComponents[0];
        String toText = workingHoursComponents[1];
        Time from = timeFromText(fromText);
        Time to = timeFromText(toText);
        return workingHoursBuilder.onBusinessDay(from, to);
    }

    private boolean twoDigitNumberTextAtRange(String numberText, int fromIncluded, int toIncluded) {
        if (numberText.length() != 2) {
            return false;
        }
        try {
            int number = Integer.parseInt(numberText);
            return (fromIncluded <= number) && (number <= toIncluded);
        } catch (Exception e) {
            return false;
        }
    }

    private Time timeFromText(String timeText) {
        if (!allowableTimeTextIn24HoursFormat(timeText)) {
            throw new IllegalArgumentException("Illegal time: " + timeText);
        }
        String[] timeComponents = timeText.split(":");
        String hours = timeComponents[0];
        String minutes = timeComponents[1];
        return new Time(Integer.parseInt(hours), Integer.parseInt(minutes));
    }

    private boolean allowableTimeTextIn24HoursFormat(String timeText) {
        String[] timeComponents = timeText.split(":");
        if (timeComponents.length != NUMBER_OF_TIME_COMPONENTS) {
            return false;
        }
        return allowableHoursTextIn24HoursFormat(timeComponents[0]) &&
                allowableMinutesTextIn24HoursFormat(timeComponents[1]);
    }

    private boolean allowableHoursTextIn24HoursFormat(String hoursText) {
        return twoDigitNumberTextAtRange(hoursText, 0, 23);
    }

    private boolean allowableMinutesTextIn24HoursFormat(String minutesText) {
        return twoDigitNumberTextAtRange(minutesText, 0, 59);
    }
}
