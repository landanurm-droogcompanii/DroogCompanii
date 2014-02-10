package ru.droogcompanii.application.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ru.droogcompanii.application.data.offers.CalendarRange;
import ru.droogcompanii.application.data.offers.Offer;
import ru.droogcompanii.application.data.offers.OfferImpl;
import ru.droogcompanii.application.util.CalendarUtils;
import ru.droogcompanii.application.util.StringConstants;

/**
 * Created by ls on 10.02.14.
 */
public class DummyOfferUtils {

    private static StringConstants.OffersXmlConstants.Tags
            TAGS = new StringConstants.OffersXmlConstants.Tags();

    private static class XmlValues {
        public int id;
        public int partnerId;
        public String imageUrl;
        public String shortDescription;
        public String fullDescription;
        public Calendar from;
        public Calendar to;

        public String toOfferString() {
            return (
                    "\t<" + TAGS.offer + ">\n" +
                    "\t\t<" + TAGS.id + ">" + id + "</" + TAGS.id + ">\n" +
                    "\t\t<" + TAGS.partnerId + ">" + partnerId + "</" + TAGS.partnerId + ">\n" +
                    "\t\t<" + TAGS.imageUrl + ">" + imageUrl + "</" + TAGS.imageUrl + ">\n" +
                    "\t\t<" + TAGS.shortDescription + ">" + shortDescription + "</" + TAGS.shortDescription + ">\n" +
                    "\t\t<" + TAGS.fullDescription + ">" + fullDescription + "</" + TAGS.fullDescription + ">\n" +
                    "\t\t<" + TAGS.duration + ">\n" +
                    "\t\t\t<" + TAGS.from + ">" + CalendarUtils.convertToString(from) + "</" + TAGS.from + ">\n" +
                    "\t\t\t<" + TAGS.to + ">" + CalendarUtils.convertToString(to) + "</" + TAGS.to + ">\n" +
                    "\t\t</" + TAGS.duration + ">\n" +
                    "\t</" + TAGS.offer + ">\n"
            );
        }
    }


    private final int numberOfOffers;


    public DummyOfferUtils(int numberOfOffers) {
        this.numberOfOffers = numberOfOffers;
    }

    public String getXmlText() {
        StringBuilder builder = new StringBuilder();
        builder.append("<" + TAGS.offers + " type=\"array\">\n");
        for (int i = 1; i <= numberOfOffers; ++i) {
            builder.append(dummyXmlValues(i).toOfferString());
        }
        builder.append("</" + TAGS.offers + ">");
        return builder.toString();
    }

    private static XmlValues dummyXmlValues(int index) {
        XmlValues xmlValues = new XmlValues();
        xmlValues.id = index;
        xmlValues.partnerId = index;
        xmlValues.imageUrl = "http://server.com/" + "12552" + index + ".jpg";
        xmlValues.shortDescription = index + ". This is short description.";
        xmlValues.fullDescription = index + ". This is FULL description.";
        xmlValues.from = CalendarUtils.createByDayMonthYear(index, Calendar.JANUARY, 2014);
        xmlValues.to = CalendarUtils.createByDayMonthYear(index, Calendar.APRIL, 2014);
        return xmlValues;
    }

    public List<Offer> getExpectedOffers() {
        List<Offer> offers = new ArrayList<Offer>(numberOfOffers);
        for (int i = 1; i <= numberOfOffers; ++i) {
            offers.add(offerFrom(dummyXmlValues(i)));
        }
        return offers;
    }

    private static Offer offerFrom(XmlValues values) {
        OfferImpl offer = new OfferImpl();
        offer.id = values.id;
        offer.partnerId = values.partnerId;
        offer.imageUrl = values.imageUrl;
        offer.shortDescription = values.shortDescription;
        offer.fullDescription = values.fullDescription;
        offer.duration = new CalendarRange(values.from, values.to);
        return offer;
    }
}
