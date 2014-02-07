package ru.droogcompanii.application.test.data.xml_parser.offers_xml_parser;

import junit.framework.TestCase;

import java.io.InputStream;
import java.util.List;

import ru.droogcompanii.application.data.offers.Offer;
import ru.droogcompanii.application.data.xml_parser.offers_xml_parser.OffersXmlParser;
import ru.droogcompanii.application.util.StringToInputStreamConvertor;

/**
 * Created by ls on 07.02.14.
 */
public class SimpleTestOffersXmlParser extends TestCase {

    final String simpleXml =
        "<offers type=\"array\">\n" +
        "\t<offer>\n" +
        "\t\t<id>12345</id>\n" +
        "\t\t<partner-id>34526</partner-id>\n" +
        "\t\t<image-url>/system/offers/38/medium/1371633650.jpg</image-url>\n" +
        "\t\t<short-description>Short</short-description>\n" +
        "\t\t<full-description>Full</full-description>\n" +
        "\t\t<duration>\n" +
        "\t\t\t<from>22.01.14</from>\n" +
        "\t\t\t<to>22.05.14</to>\n" +
        "\t\t</duration>\n" +
        "\t</offer>\n" +
        "</offers>";


    public void testParsing() {
        InputStream inputStream = StringToInputStreamConvertor.convert(simpleXml);
        OffersXmlParser parser = new OffersXmlParser();
        try {
            List<Offer> parsed = parser.parse(inputStream);
            assertTrue(parsed.size() == 1);
            assertEquals(expectedOffer(), parsed.get(0));
        } catch (Exception e) {
            throw new AssertionError("Parser should correct parse xml");
        }
    }

    private Offer expectedOffer() {
        // TODO:
        return null;
    }

}
