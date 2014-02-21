package ru.droogcompanii.application.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * Created by ls on 07.02.14.
 */
public class StringToInputStreamConverter {

    public static InputStream convert(String str) {
        try {
            return new ByteArrayInputStream(str.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            return new ByteArrayInputStream(str.getBytes());
        }
    }
}
