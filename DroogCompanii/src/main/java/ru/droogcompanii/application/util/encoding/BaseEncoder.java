package ru.droogcompanii.application.util.encoding;

import java.security.MessageDigest;

import ru.droogcompanii.application.util.LogUtils;

/**
 * Created by ls on 21.02.14.
 */
abstract class BaseEncoder implements Encoder {

    @Override
    public String encodeToString(String toEncode) {
        byte[] bytes = encode(toEncode);
        return ByteArrayToHexStringConverter.convert(bytes);
    }

    @Override
    public byte[] encode(String toEncode) {
        try {
            return tryEncode(toEncode);
        } catch(Exception e) {
            LogUtils.debug(e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private byte[] tryEncode(String toEncode) throws Exception {
        MessageDigest crypt = MessageDigest.getInstance(getAlgorithm());
        crypt.reset();
        byte[] bytes = toEncode.getBytes(EncodingUtils.CHARSET_NAME);
        crypt.update(bytes);
        return crypt.digest();
    }

    protected abstract String getAlgorithm();
}
