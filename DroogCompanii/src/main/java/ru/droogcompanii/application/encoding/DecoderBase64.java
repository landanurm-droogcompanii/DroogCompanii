package ru.droogcompanii.application.encoding;

import android.util.Base64;

/**
 * Created by ls on 21.02.14.
 */
public class DecoderBase64 implements Decoder {

    @Override
    public String decode(String toDecode) {
        return bytesToString(Base64.decode(toDecode, Base64.DEFAULT));
    }

    private static String bytesToString(byte[] bytes) {
        try {
            return new String(bytes, EncodingUtils.CHARSET_NAME);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public String decode(byte[] toDecode) {
        return bytesToString(Base64.decode(toDecode, Base64.DEFAULT));
    }
}
