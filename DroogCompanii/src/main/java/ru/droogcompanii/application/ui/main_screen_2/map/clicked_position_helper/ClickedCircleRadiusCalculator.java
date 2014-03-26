package ru.droogcompanii.application.ui.main_screen_2.map.clicked_position_helper;

import ru.droogcompanii.application.ui.util.constants.ZoomLevel;

/**
 * Created by ls on 18.03.14.
 */
class ClickedCircleRadiusCalculator {

    private static final double A = -0.5;
    private static final double B = 2.0;
    private static final double C = 69.25;

    public static double calculate(double currentZoom) {
        return minRadius(currentZoom);
    }

    private static double minRadius(double zoom) {
        double base = ZoomLevel.max() - zoom + 1;
        double log = Math.log(base + 1) / Math.log(2) + 1;
        double logLog = Math.log(log);
        return 1.7 * base * log * Math.max(1, Math.pow(logLog, logLog));
    }

    private static double calculateUsingQuadraticFunction(double x) {
        return A * x * x + B * x + C;
    }
}
