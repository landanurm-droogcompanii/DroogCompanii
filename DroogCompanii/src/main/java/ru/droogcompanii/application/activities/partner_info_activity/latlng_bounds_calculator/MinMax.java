package ru.droogcompanii.application.activities.partner_info_activity.latlng_bounds_calculator;

/**
 * Created by ls on 25.12.13.
 */

class MinMax {
    private double min = Double.MAX_VALUE;
    private double max = Double.MIN_VALUE;

    public void add(double val) {
        if (val < min) {
            min = val;
        }
        if (val > max) {
            max = val;
        }
    }

    public double min() {
        return min;
    }

    public double max() {
        return max;
    }

    public double diff() {
        return Math.abs(max - min);
    }
}
