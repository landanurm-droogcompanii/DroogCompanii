package ru.droogcompanii.application.util.encoding;

/**
 * Created by ls on 21.02.14.
 */
public interface Encoder {
    byte[] encode(String toEncode);
    String encodeToString(String toEncode);
}
