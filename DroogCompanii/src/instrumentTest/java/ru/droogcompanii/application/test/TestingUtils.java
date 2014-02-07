package ru.droogcompanii.application.test;

import junit.framework.AssertionFailedError;

import java.io.Serializable;

import ru.droogcompanii.application.util.SerializationUtils;

/**
 * Created by ls on 16.01.14.
 */
public class TestingUtils {

    public static void assertExpectedException(Class<? extends Exception> exceptionClass, Runnable runnable) {
        try {
            runnable.run();
            throw new AssertionFailedError(
                "Expected exception: <" + exceptionClass.getName() + ">, but was not thrown"
            );
        } catch (Exception e) {
            if (!exceptionClass.equals(e.getClass())) {
                throw new AssertionFailedError(
                        "Expected exception: <" + exceptionClass.getName() + "> " +
                        "but was: <" + e.getClass().getName() + ">"
                );
            }
        }
    }

    public static <T extends Serializable> T serializeAndDeserialize(T obj) {
        byte[] bytes = SerializationUtils.serialize(obj);
        return (T) SerializationUtils.deserialize(bytes);
    }
}
