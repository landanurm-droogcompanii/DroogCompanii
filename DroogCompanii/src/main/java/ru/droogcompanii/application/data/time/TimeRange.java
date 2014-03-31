package ru.droogcompanii.application.data.time;

/**
 * Created by ls on 20.01.14.
 */
public interface TimeRange {
    static final String SEPARATOR = "-";

    boolean includes(TimeOfDay time);
    TimeOfDay from();
    TimeOfDay to();
}
