package ru.droogcompanii.application.data.xml_parser.offers_xml_parser;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import ru.droogcompanii.application.data.offers.CalendarRange;
import ru.droogcompanii.application.data.offers.Offer;
import ru.droogcompanii.application.data.offers.OfferImpl;
import ru.droogcompanii.application.data.xml_parser.XmlParserUtils;
import ru.droogcompanii.application.util.CalendarUtils;
import ru.droogcompanii.application.util.StringConstants;
import ru.droogcompanii.application.util.StringsCombiner;

/**
 * Created by ls on 07.02.14.
 */
public class OffersXmlParser {

    private static final StringConstants.OffersXmlConstants.Tags
            TAGS = new StringConstants.OffersXmlConstants.Tags();

    private static final Collection<String> DATE_COMPONENTS = Arrays.asList("dd", "mm", "yyyy");
    private static final String FORMAT_OF_DATE =
            StringsCombiner.combine(DATE_COMPONENTS, CalendarUtils.SEPARATOR_OF_DATE_COMPONENTS);

    private static final int INDEX_OF_DAY_COMPONENT = 0;
    private static final int INDEX_OF_MONTH_COMPONENT = 1;
    private static final int INDEX_OF_YEAR_COMPONENT = 2;

    private static final String NAMESPACE = null;

    private List<Offer> outOffers;


    public List<Offer> parse(InputStream in) throws Exception {
        try {
            return tryParse(in);
        } finally {
            XmlParserUtils.tryClose(in);
        }
    }

    private List<Offer> tryParse(InputStream in) throws Exception {
        outOffers = new ArrayList<Offer>();
        parseOffers(XmlParserUtils.prepareParser(in));
        return outOffers;
    }

    private void parseOffers(XmlPullParser parser) throws Exception {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, TAGS.offers);

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String tag = parser.getName();
            if (tag.equals(TAGS.offer)) {
                outOffers.add(parseOffer(parser));
            } else {
                XmlParserUtils.skip(parser);
            }
        }
    }

    private Offer parseOffer(XmlPullParser parser) throws Exception {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, TAGS.offer);

        OfferImpl offer = new OfferImpl();

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String tag = parser.getName();
            if (tag.equals(TAGS.id)) {
                offer.id = XmlParserUtils.parseIntegerByTag(parser, tag);
            } else if (tag.equals(TAGS.partnerId)) {
                offer.partnerId = XmlParserUtils.parseIntegerByTag(parser, tag);
            } else if (tag.equals(TAGS.imageUrl)) {
                offer.imageUrl = XmlParserUtils.parseTextByTag(parser, tag);
            } else if (tag.equals(TAGS.shortDescription)) {
                offer.shortDescription = XmlParserUtils.parseTextByTag(parser, tag);
            } else if (tag.equals(TAGS.fullDescription)) {
                offer.fullDescription = XmlParserUtils.parseTextByTag(parser, tag);
            } else if (tag.equals(TAGS.duration)) {
                offer.duration = parseDuration(parser);
            } else {
                XmlParserUtils.skip(parser);
            }
        }

        return offer;
    }

    private CalendarRange parseDuration(XmlPullParser parser) throws Exception {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, TAGS.duration);

        Calendar from = null;
        Calendar to = null;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String tag = parser.getName();
            if (tag.equals(TAGS.from)) {
                from = calendarFromText(XmlParserUtils.parseTextByTag(parser, tag));
            } else if (tag.equals(TAGS.to)) {
                to = calendarFromText(XmlParserUtils.parseTextByTag(parser, tag));
            } else {
                XmlParserUtils.skip(parser);
            }
        }

        return new CalendarRange(from, to);
    }

    private static Calendar calendarFromText(String text) {
        String[] components = text.trim().split("\\" + CalendarUtils.SEPARATOR_OF_DATE_COMPONENTS);
        if (components.length != DATE_COMPONENTS.size()) {
            throw new IllegalArgumentException("Expected format of date: " + FORMAT_OF_DATE);
        }
        int day = Integer.parseInt(components[INDEX_OF_DAY_COMPONENT]);
        int month = Integer.parseInt(components[INDEX_OF_MONTH_COMPONENT]);
        int year = Integer.parseInt(components[INDEX_OF_YEAR_COMPONENT]);
        return CalendarUtils.createByDayMonthYear(day, month, year);
    }

}
