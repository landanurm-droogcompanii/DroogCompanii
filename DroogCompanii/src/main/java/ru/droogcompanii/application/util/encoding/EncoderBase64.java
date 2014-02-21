package ru.droogcompanii.application.util.encoding;

import android.util.Base64;

import ru.droogcompanii.application.util.LogUtils;

/**
 * Created by ls on 21.02.14.
 */
public class EncoderBase64 implements Encoder {

    private final Encoder encoderToEncodeBefore;

    public EncoderBase64(Encoder encoder) {
        encoderToEncodeBefore = encoder;
    }

    @Override
    public String encodeToString(String toEncode) {
        try {
            return tryEncodeToString(toEncode);
        } catch (Exception e) {
            LogUtils.debug(e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private String tryEncodeToString(String toEncode) throws Exception {
        toEncode = encoderToEncodeBefore.encodeToString(toEncode);

        byte[] bytes = toEncode.getBytes(EncodingUtils.CHARSET_NAME);
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    @Override
    public byte[] encode(String toEncode) {
        try {
            return tryEncode(toEncode);
        } catch (Exception e) {
            LogUtils.debug(e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private byte[] tryEncode(String toEncode) throws Exception {
        toEncode = encoderToEncodeBefore.encodeToString(toEncode);

        byte[] bytes = toEncode.getBytes(EncodingUtils.CHARSET_NAME);
        return Base64.encode(bytes, Base64.DEFAULT);
    }
}
