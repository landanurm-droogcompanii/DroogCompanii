package ru.droogcompanii.application.util;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
