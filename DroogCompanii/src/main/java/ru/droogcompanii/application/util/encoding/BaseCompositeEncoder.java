package ru.droogcompanii.application.util.encoding;

/**
 * Created by ls on 21.02.14.
 */
abstract class BaseCompositeEncoder extends BaseEncoder {

    private final Encoder encoderToEncodeBefore;

    protected BaseCompositeEncoder(Encoder encoder) {
        encoderToEncodeBefore = encoder;
    }

    @Override
    public String encodeToString(String toEncode) {
        toEncode = encoderToEncodeBefore.encodeToString(toEncode);
        return super.encodeToString(toEncode);
    }

    @Override
    public byte[] encode(String toEncode) {
        toEncode = encoderToEncodeBefore.encodeToString(toEncode);
        return super.encode(toEncode);
    }
}
