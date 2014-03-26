package ru.droogcompanii.application.ui.util.constants;

/**
 * Created by ls on 18.03.14.
 */
public class ZoomLevel {

    public static float min() {
        return 2.0f;
    }

    public static float max() {
        return 21.0f;
    }

    public static float range() {
        return max() - min();
    }
}
