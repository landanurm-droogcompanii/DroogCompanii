package ru.droogcompanii.application.ui.activity.main_screen_2;

/**
 * Created by ls on 18.03.14.
 */
public class ClickedCircleRadiusCalculator {

    private static final double MIN_RADIUS = 2.0;

    private static final double A = -0.35;
    private static final double B = 2.0;
    private static final double C = 120.0;

    public static double calculate(float currentZoom, float maxZoom) {
        return Math.max(MIN_RADIUS, calculateUsingQuadraticFunction(currentZoom + 2));
    }

    private static double calculateUsingQuadraticFunction(double x) {
        return A * x * x + B * x + C;
    }
}
