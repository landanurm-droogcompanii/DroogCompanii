package ru.droogcompanii.application.data.time;

/**
 * Created by ls on 20.01.14.
 */
public interface TimeRange {
    static final String SEPARATOR = "-";

    TimeOfDay from();
    TimeOfDay to();
    boolean includes(TimeOfDay time);
}
