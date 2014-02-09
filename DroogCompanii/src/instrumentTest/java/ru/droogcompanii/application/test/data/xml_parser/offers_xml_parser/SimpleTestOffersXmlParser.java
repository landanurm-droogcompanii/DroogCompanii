package ru.droogcompanii.application.test.data.xml_parser.offers_xml_parser;

import junit.framework.TestCase;

import java.io.InputStream;
import java.util.Calendar;
import java.util.List;

import ru.droogcompanii.application.data.offers.CalendarRange;
import ru.droogcompanii.application.data.offers.Offer;
import ru.droogcompanii.application.data.offers.OfferImpl;
import ru.droogcompanii.application.data.xml_parser.offers_xml_parser.OffersXmlParser;
import ru.droogcompanii.application.util.CalendarUtils;
import ru.droogcompanii.application.util.StringConstants;
import ru.droogcompanii.application.util.StringToInputStreamConvertor;

/**
 * Created by ls on 07.02.14.
 */
public class SimpleTestOffersXmlParser extends TestCase {

    private static StringConstants.OffersXmlConstants.Tags TAGS = new StringConstants.OffersXmlConstants.Tags();

    private static final int ID = 12345;
    private static final int PARTNER_ID = 34526;
    private static final String IMAGE_URL = "/system/offers/38/medium/1371633650.jpg";
    private static final String SHORT_DESCRIPTION = "Short description";
    private static final String FULL_DESCRIPTION = "Full description";
    private static final Calendar FROM = CalendarUtils.createByDayMonthYear(22, 1, 2014);
    private static final Calendar TO = CalendarUtils.createByDayMonthYear(22, 5, 2015);
    private static final CalendarRange DURATION = new CalendarRange(FROM, TO);

    private static final String simpleXml =
        "<" + TAGS.offers + " type=\"array\">\n" +
        "\t<" + TAGS.offer + ">\n" +
        "\t\t<" + TAGS.id + ">" + ID + "</" + TAGS.id + ">\n" +
        "\t\t<" + TAGS.partnerId + ">" + PARTNER_ID + "</" + TAGS.partnerId + ">\n" +
        "\t\t<" + TAGS.imageUrl + ">" + IMAGE_URL + "</" + TAGS.imageUrl + ">\n" +
        "\t\t<" + TAGS.shortDescription + ">" + SHORT_DESCRIPTION + "</" + TAGS.shortDescription + ">\n" +
        "\t\t<" + TAGS.fullDescription + ">" + FULL_DESCRIPTION + "</" + TAGS.fullDescription + ">\n" +
        "\t\t<" + TAGS.duration + ">\n" +
        "\t\t\t<" + TAGS.from + ">" + CalendarUtils.convertToString(FROM) + "</" + TAGS.from + ">\n" +
        "\t\t\t<" + TAGS.to + ">" + CalendarUtils.convertToString(TO) + "</" + TAGS.to + ">\n" +
        "\t\t</" + TAGS.duration + ">\n" +
        "\t</" + TAGS.offer + ">\n" +
        "</" + TAGS.offers + ">";


    public void testParsing() {
        InputStream inputStream = StringToInputStreamConvertor.convert(simpleXml);
        OffersXmlParser parser = new OffersXmlParser();
        List<Offer> parsed = null;
        try {
            parsed = parser.parse(inputStream);
        } catch (Exception e) {
            fail("Exception was occurred: " + e.getMessage());
        }
        assertTrue(parsed.size() == 1);
        assertEquals(expectedOffer(), parsed.get(0));
    }

    private Offer expectedOffer() {
        OfferImpl offer = new OfferImpl();
        offer.id = ID;
        offer.partnerId = PARTNER_ID;
        offer.imageUrl = IMAGE_URL;
        offer.shortDescription = SHORT_DESCRIPTION;
        offer.fullDescription = FULL_DESCRIPTION;
        offer.duration = DURATION;
        return offer;
    }

}
