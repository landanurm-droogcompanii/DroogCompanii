package ru.droogcompanii.application.util.encoding;

import android.util.Base64;

/**
 * Created by ls on 21.02.14.
 */
public class EncoderBase64 implements Encoder {

    @Override
    public String encodeToString(String toEncode) {
        try {
            return tryEncodeToString(toEncode);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private String tryEncodeToString(String toEncode) throws Exception {
        byte[] bytes = toEncode.getBytes(EncodingUtils.CHARSET_NAME);
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    @Override
    public byte[] encode(String toEncode) {
        try {
            return tryEncode(toEncode);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private byte[] tryEncode(String toEncode) throws Exception {
        byte[] bytes = toEncode.getBytes(EncodingUtils.CHARSET_NAME);
        return Base64.encode(bytes, Base64.DEFAULT);
    }
}
