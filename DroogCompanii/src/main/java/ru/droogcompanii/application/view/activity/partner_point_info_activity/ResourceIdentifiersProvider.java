package ru.droogcompanii.application.view.activity.partner_point_info_activity;

import ru.droogcompanii.application.R;

/**
 * Created by ls on 25.12.13.
 */
class ResourceIdentifiersProvider {

    private static final int[] idsOfIndicatorsTextViews = {
            R.id.mondayIndicator,
            R.id.tuesdayIndicator,
            R.id.wednesdayIndicator,
            R.id.thursdayIndicator,
            R.id.fridayIndicator,
            R.id.saturdayIndicator,
            R.id.sundayIndicator
    };

    private static final int[] idsOfDayTextViews = {
            R.id.monday,
            R.id.tuesday,
            R.id.wednesday,
            R.id.thursday,
            R.id.friday,
            R.id.saturday,
            R.id.sunday
    };

    private static final int[] idsOfWorkingHoursTextViews = {
            R.id.mondayWorkingHours,
            R.id.tuesdayWorkingHours,
            R.id.wednesdayWorkingHours,
            R.id.thursdayWorkingHours,
            R.id.fridayWorkingHours,
            R.id.saturdayWorkingHours,
            R.id.sundayWorkingHours
    };

    public static int getIdOfIndicatorTextView(int indexOfRow) {
        return idsOfIndicatorsTextViews[indexOfRow];
    }

    public static int getIdOfDayTextView(int indexOfRow) {
        return idsOfDayTextViews[indexOfRow];
    }

    public static int getIdOfWorkingHoursTextView(int indexOfRow) {
        return idsOfWorkingHoursTextViews[indexOfRow];
    }
}
