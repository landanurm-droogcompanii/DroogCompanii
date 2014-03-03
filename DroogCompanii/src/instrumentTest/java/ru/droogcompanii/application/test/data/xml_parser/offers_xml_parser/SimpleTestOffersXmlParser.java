package ru.droogcompanii.application.test.data.xml_parser.offers_xml_parser;

import junit.framework.TestCase;

import java.io.InputStream;
import java.util.List;

import ru.droogcompanii.application.data.offers.Offer;
import ru.droogcompanii.application.data.xml_parser.offers_xml_parser.OffersXmlParser;
import ru.droogcompanii.application.util.StringToInputStreamConverter;

/**
 * Created by ls on 07.02.14.
 */
public class SimpleTestOffersXmlParser extends TestCase {

    public void testParsing() {
        DummyOfferUtils dummyOfferUtils = new DummyOfferUtils(5);
        InputStream inputStream = StringToInputStreamConverter.convert(dummyOfferUtils.getXmlText());
        OffersXmlParser parser = new OffersXmlParser();
        List<Offer> parsed = null;
        try {
            parsed = parser.parse(inputStream);
        } catch (Exception e) {
            fail("Exception was occurred: " + e.getMessage());
        }
        assertEquals(dummyOfferUtils.getExpectedOffers(), parsed);
    }

}
