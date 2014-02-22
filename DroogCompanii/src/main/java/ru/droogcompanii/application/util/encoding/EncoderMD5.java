package ru.droogcompanii.application.util.encoding;

/**
 * Created by ls on 21.02.14.
 */
public class EncoderMD5 extends BaseEncoder {

    @Override
    protected String getAlgorithm() {
        return "MD5";
    }
}
