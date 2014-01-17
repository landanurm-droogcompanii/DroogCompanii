package ru.droogcompanii.application.test.util;

import junit.framework.TestCase;

import java.io.Serializable;
import java.util.Arrays;

import ru.droogcompanii.application.util.SerializationUtils;

/**
 * Created by ls on 09.01.14.
 */
public class TestSerializationUtils extends TestCase {

    public void test() {
        assertSerializationUtilsWorksCorrectly(Integer.valueOf(5));
        assertSerializationUtilsWorksCorrectly("123456789");
        assertSerializationUtilsWorksCorrectly((Serializable) Arrays.asList(new Double[] { 2.3, 3.4, 4.5 }));
    }

    private void assertSerializationUtilsWorksCorrectly(Serializable serializable) {
        byte[] bytes = SerializationUtils.serialize(serializable);
        Object deserialized = SerializationUtils.deserialize(bytes);
        assertEquals((Object) serializable, deserialized);
    }
}
