package ru.droogcompanii.application.encoding;

/**
 * Created by ls on 21.02.14.
 */
public interface Encoder {
    byte[] encode(String toEncode);
    String encodeToString(String toEncode);
}
