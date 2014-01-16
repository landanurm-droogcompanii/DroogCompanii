package ru.droogcompanii.application;

import junit.framework.AssertionFailedError;

/**
 * Created by ls on 16.01.14.
 */
public class UtilsForTest {
    public static void assertExpectedException(Class<? extends Exception> exceptionClass, Runnable runnable) {
        try {
            runnable.run();
            throw new AssertionFailedError("Expected exception: <" + exceptionClass.getName() + ">");
        } catch (Exception e) {
            if (!exceptionClass.equals(e.getClass())) {
                throw new AssertionFailedError(
                        "Expected exception: <" + exceptionClass.getName() + "> " +
                        "but was: <" + e.getClass().getName() + ">"
                );
            }
        }
    }
}
