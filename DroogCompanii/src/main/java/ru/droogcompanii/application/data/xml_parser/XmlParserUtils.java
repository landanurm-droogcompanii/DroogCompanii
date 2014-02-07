package ru.droogcompanii.application.data.xml_parser;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ls on 07.02.14.
 */
public class XmlParserUtils {

    public static final String NAMESPACE = null;

    public static XmlPullParser prepareParser(InputStream in) throws Exception {
        XmlPullParser parser = Xml.newPullParser();
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(in, null);
        parser.nextTag();
        return parser;
    }

    public static void tryClose(InputStream inputStream) {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                // do nothing
            }
        }
    }

    public static void skip(XmlPullParser parser) throws Exception {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

    public static Map<String, String> parseAttributes(XmlPullParser parser) {
        int attributeCount = parser.getAttributeCount();
        Map<String, String> attributes = new HashMap<String, String>(attributeCount);
        for(int i = 0; i < attributeCount; i++) {
            attributes.put(parser.getAttributeName(i), parser.getAttributeValue(i));
        }
        return attributes;
    }

    public static void requireAttributes(XmlPullParser parser, String... requiredAttributes) {
        int attributeCount = parser.getAttributeCount();
        if (attributeCount == -1) {
            throw new IllegalArgumentException("Current event type is not START_TAG");
        }
        String[] actualAttributes = new String[attributeCount];
        for (int i = 0; i < attributeCount; ++i) {
            String attribute = parser.getAttributeName(i);
            actualAttributes[i] = attribute;
        }
        Arrays.sort(requiredAttributes);
        Arrays.sort(actualAttributes);
        boolean allRight = Arrays.equals(requiredAttributes, actualAttributes);
        if (!allRight) {
            StringBuilder illegalArgumentExceptionMessage = new StringBuilder(
                    "For WorkingHours of day need " + requiredAttributes.length + " attribute(s): "
            );
            for (String attribute : requiredAttributes) {
                illegalArgumentExceptionMessage.append(attribute);
            }
            throw new IllegalArgumentException(illegalArgumentExceptionMessage.toString());
        }
    }

    public static String parseTextByTag(XmlPullParser parser, String tag) throws Exception {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, tag);
        String value = parseText(parser);
        parser.require(XmlPullParser.END_TAG, NAMESPACE, tag);
        return value;
    }

    public static String parseText(XmlPullParser parser) throws Exception {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    public static int parseInteger(XmlPullParser parser) throws Exception {
        return Integer.valueOf(parseText(parser));
    }

    public static double parseDouble(XmlPullParser parser) throws Exception {
        return Double.valueOf(parseText(parser));
    }

    public static int parseIntegerByTag(XmlPullParser parser, String tag) throws Exception {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, tag);
        int value = XmlParserUtils.parseInteger(parser);
        parser.require(XmlPullParser.END_TAG, NAMESPACE, tag);
        return value;
    }

    public static double parseDoubleByTag(XmlPullParser parser, String tag) throws Exception {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, tag);
        double result = XmlParserUtils.parseDouble(parser);
        parser.require(XmlPullParser.END_TAG, NAMESPACE, tag);
        return result;
    }

}
