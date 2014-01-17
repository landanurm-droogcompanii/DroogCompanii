package ru.droogcompanii.application.test.util;

import junit.framework.TestCase;

import java.util.Arrays;
import java.util.List;

import ru.droogcompanii.application.util.StringsCombiner;

/**
 * Created by ls on 09.01.14.
 */
public class TestStringsCombiner extends TestCase {

    public void testSimpleExample() {
        String expected =
                "abcde\n" +
                "12345\n" +
                "zzzzyrtur\n" +
                "okay";
        List<String> lines = Arrays.asList(expected.split("\n"));
        String actual = StringsCombiner.combine(lines);
        assertEquals(expected, actual);
    }
}
