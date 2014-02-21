package ru.droogcompanii.application.util.encoding;

/**
 * Created by ls on 21.02.14.
 */
public class EncoderMD5 extends BaseCompositeEncoder {


    protected EncoderMD5(Encoder encoder) {
        super(encoder);
    }

    @Override
    protected String getAlgorithm() {
        return "MD5";
    }
}
