package ru.droogcompanii.application.util;

/**
 * Created by ls on 18.02.14.
 */
public class Snorlax {
    private static final long SLEEP_TIME_IN_MILLISECONDS = 2000L;

    public static void sleep() {
        actuallySleep();
    }

    private static void doNotSleep() {
        // do nothing
    }

    private static void actuallySleep() {
        try {
            Thread.currentThread().sleep(SLEEP_TIME_IN_MILLISECONDS);
        } catch (InterruptedException e) {
            LogUtils.debug(Snorlax.class.getName() + ".sleep():  " + e.getMessage());
        }
    }
}
