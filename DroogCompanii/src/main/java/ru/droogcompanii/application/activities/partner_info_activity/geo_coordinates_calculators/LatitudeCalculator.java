package ru.droogcompanii.application.activities.partner_info_activity.geo_coordinates_calculators;

/**
 * Created by ls on 26.12.13.
 */
public class LatitudeCalculator {

    private static final double MIN_LATITUDE = -90.0;
    private static final double MAX_LATITUDE = 90.0;

    // Must provide the following cases:
    // (X) +- (0)  =>  (X)
    // (X) - (X)  =>  (0)
    // (X) +- (180)  =>  (X)
    // (-90) - (10)  =>  (-80)
    // (20) - (30)  =>  (-10)
    // (-80) - (30)  =>  (-70)
    // (-90) - (90)  =>  (0)
    // (-90) - (80)  =>  (-10)
    // (-90) - (2)  =>  (-88)
    // (-88) - (3)  =>  (-89)
    // (0) - (5)  =>  (-5)
    // (10) - (5)  =>  (5)
    // (20) - (30)  =>  (-10)
    // (90) - (20)  =>  (70)
    // (90) - (90)  =>  (0)

    public static double add(double to, double what) {
        return to + what;
    }

    public static double subtract(double from, double what) {
        return from - what;
    }

}
