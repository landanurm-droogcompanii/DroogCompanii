package ru.droogcompanii.application.util.encoding;

import java.util.Formatter;

/**
 * Created by ls on 21.02.14.
 */
public class ByteArrayToHexStringConverter {

    public static String convert(final byte[] bytes) {
        Formatter formatter = new Formatter();
        for (byte each : bytes) {
            formatter.format("%02x", each);
        }
        formatter.close();
        return formatter.toString();
    }
}
