package ru.droogcompanii.application.data.xml_parser;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import ru.droogcompanii.application.data.data_structure.Partner;
import ru.droogcompanii.application.data.data_structure.PartnerCategory;
import ru.droogcompanii.application.data.data_structure.PartnerPoint;
import ru.droogcompanii.application.data.data_structure.working_hours.WeekWorkingHours;
import ru.droogcompanii.application.data.data_structure.working_hours.WorkingHours;
import ru.droogcompanii.application.data.data_structure.working_hours.WorkingHoursForEachDayOfWeek;
import ru.droogcompanii.application.data.data_structure.working_hours.WorkingHoursParser;

/**
 * Created by Leonid on 02.12.13.
 */
public class DroogCompaniiXmlParser {

    public static class ParsedData {
        public final List<PartnerCategory> partnerCategories;
        public final List<Partner> partners;
        public final List<PartnerPoint> partnerPoints;

        public ParsedData(List<PartnerCategory> partnerCategories,
                          List<Partner> partners,
                          List<PartnerPoint> partnerPoints) {
            this.partnerCategories = partnerCategories;
            this.partners = partners;
            this.partnerPoints = partnerPoints;
        }
    }

    private static final String NAMESPACE = null;

    public ParsedData parse(InputStream in) throws Exception {
        try {
            return parse(prepareParser(in));
        } finally {
            tryClose(in);
        }
    }

    private XmlPullParser prepareParser(InputStream in) throws Exception {
        XmlPullParser parser = Xml.newPullParser();
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(in, null);
        parser.nextTag();
        return parser;
    }

    private void tryClose(InputStream inputStream) {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                // do nothing
            }
        }
    }


    private List<PartnerCategory> outPartnerCategories;
    private List<Partner> outPartners;
    private List<PartnerPoint> outPartnerPoints;


    private ParsedData parse(XmlPullParser parser) throws Exception {
        outPartnerCategories = new ArrayList<PartnerCategory>();
        outPartners = new ArrayList<Partner>();
        outPartnerPoints = new ArrayList<PartnerPoint>();
        parsePartnerCategories(parser);
        return new ParsedData(outPartnerCategories, outPartners, outPartnerPoints);
    }

    private void parsePartnerCategories(XmlPullParser parser) throws Exception {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, XmlTags.partnerCategories);

        int partnerCategoryId = 0;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String tag = parser.getName();
            if (tag.equals(XmlTags.partnerCategory)) {
                ++partnerCategoryId;
                PartnerCategory partnerCategory = parsePartnerCategory(parser, partnerCategoryId);
                outPartnerCategories.add(partnerCategory);
            } else {
                skip(parser);
            }
        }
    }

    private PartnerCategory parsePartnerCategory(XmlPullParser parser, int partnerCategoryId) throws Exception {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, XmlTags.partnerCategory);

        String title = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String tag = parser.getName();
            if (tag.equals(XmlTags.title)) {
                title = parseTitle(parser);
            } else if (tag.equals(XmlTags.partners)) {
                parsePartners(parser, partnerCategoryId);
            } else {
                skip(parser);
            }
        }
        return new PartnerCategory(partnerCategoryId, title);
    }

    private String parseTitle(XmlPullParser parser) throws Exception {
        return parseTextByTag(parser, XmlTags.title);
    }

    private String parseTextByTag(XmlPullParser parser, String tag) throws Exception {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, tag);
        String value = parseText(parser);
        parser.require(XmlPullParser.END_TAG, NAMESPACE, tag);
        return value;
    }

    private String parseText(XmlPullParser parser) throws Exception {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private void parsePartners(XmlPullParser parser, int partnerCategoryId) throws Exception {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, XmlTags.partners);

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String tag = parser.getName();
            if (tag.equals(XmlTags.partner)) {
                Partner partner = parsePartner(parser, partnerCategoryId);
                outPartners.add(partner);
            } else {
                skip(parser);
            }
        }
    }

    private Partner parsePartner(XmlPullParser parser, int partnerCategoryId) throws Exception {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, XmlTags.partner);

        int id = 0;
        String title = null;
        String fullTitle = null;
        String saleType = null;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String tag = parser.getName();
            if (tag.equals(XmlTags.id)) {
                id = parseId(parser);
            } else if (tag.equals(XmlTags.title)) {
                title = parseTitle(parser);
            } else if (tag.equals(XmlTags.fullTitle)) {
                fullTitle = parseFullTitle(parser);
            } else if (tag.equals(XmlTags.saleType)) {
                saleType = parseSaleType(parser);
            } else if (tag.equals(XmlTags.partnerPoints)) {
                parsePartnerPoints(parser, id);
            } else {
                skip(parser);
            }
        }
        return new Partner(id, title, fullTitle, saleType, partnerCategoryId);
    }

    private Integer parseId(XmlPullParser parser) throws Exception {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, XmlTags.id);
        int id = parseInteger(parser);
        parser.require(XmlPullParser.END_TAG, NAMESPACE, XmlTags.id);
        return id;
    }

    private int parseInteger(XmlPullParser parser) throws Exception {
        return Integer.valueOf(parseText(parser));
    }

    private String parseFullTitle(XmlPullParser parser) throws Exception {
        return parseTextByTag(parser, XmlTags.fullTitle);
    }

    private String parseSaleType(XmlPullParser parser) throws Exception {
        return parseTextByTag(parser, XmlTags.saleType);
    }

    private void parsePartnerPoints(XmlPullParser parser, int partnerId) throws Exception {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, XmlTags.partnerPoints);

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String tag = parser.getName();
            if (tag.equals(XmlTags.partnerPoint)) {
                outPartnerPoints.add(parsePartnerPoint(parser, partnerId));
            } else {
                skip(parser);
            }
        }
    }

    private PartnerPoint parsePartnerPoint(XmlPullParser parser, int partnerId) throws Exception {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, XmlTags.partnerPoint);

        String title = null;
        String address = null;
        double longitude = 0.0;
        double latitude = 0.0;
        List<String> phones = null;
        String paymentMethods = null;
        WeekWorkingHours weekWorkingHours = null;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String tag = parser.getName();
            if (tag.equals(XmlTags.title)) {
                title = parseTitle(parser);
            } else if (tag.equals(XmlTags.address)) {
                address = parseAddress(parser);
            } else if (tag.equals(XmlTags.longitude)) {
                longitude = parseLongitude(parser);
            } else if (tag.equals(XmlTags.latitude)) {
                latitude = parseLatitude(parser);
            } else if (tag.equals(XmlTags.phones)) {
                phones = parsePhones(parser);
            } else if (tag.equals(XmlTags.paymentMethods)) {
                paymentMethods = parsePaymentMethods(parser);
            } else if (tag.equals(XmlTags.workinghours)) {
                weekWorkingHours = parseWeekWorkingHours(parser);
            } else {
                skip(parser);
            }
        }
        return new PartnerPoint(
            title, address, phones, weekWorkingHours, paymentMethods, longitude, latitude, partnerId
        );
    }

    private String parseAddress(XmlPullParser parser) throws Exception {
        return parseTextByTag(parser, XmlTags.address);
    }

    private double parseLongitude(XmlPullParser parser) throws Exception {
        return parseDoubleByTag(parser, XmlTags.longitude);
    }

    private double parseLatitude(XmlPullParser parser) throws Exception {
        return parseDoubleByTag(parser, XmlTags.latitude);
    }

    private double parseDoubleByTag(XmlPullParser parser, String tag) throws Exception {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, tag);
        double result = parseDouble(parser);
        parser.require(XmlPullParser.END_TAG, NAMESPACE, tag);
        return result;
    }

    private double parseDouble(XmlPullParser parser) throws Exception {
        return Double.valueOf(parseText(parser));
    }

    private List<String> parsePhones(XmlPullParser parser) throws Exception {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, XmlTags.phones);

        List<String> phones = new ArrayList<String>();
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String tag = parser.getName();
            if (tag.equals(XmlTags.phone)) {
                phones.add(parsePhone(parser));
            } else {
                skip(parser);
            }
        }
        return phones;
    }

    private String parsePhone(XmlPullParser parser) throws Exception {
        return parseTextByTag(parser, XmlTags.phone);
    }

    private String parsePaymentMethods(XmlPullParser parser) throws Exception {
        return parseTextByTag(parser, XmlTags.paymentMethods);
    }

    private WeekWorkingHours parseWeekWorkingHours(XmlPullParser parser) throws Exception {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, XmlTags.workinghours);

        WorkingHoursForEachDayOfWeek workingHoursForEachDayOfWeek = new WorkingHoursForEachDayOfWeek();

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String dayOfWeekTagName = parser.getName();

            if (dayOfWeekTagName.equals(XmlTags.DayOfWeek.monday)) {
                workingHoursForEachDayOfWeek.onMonday = parseWorkingHours(parser, dayOfWeekTagName);

            } else if (dayOfWeekTagName.equals(XmlTags.DayOfWeek.tuesday)) {
                workingHoursForEachDayOfWeek.onTuesday = parseWorkingHours(parser, dayOfWeekTagName);

            } else if (dayOfWeekTagName.equals(XmlTags.DayOfWeek.wednesday)) {
                workingHoursForEachDayOfWeek.onWednesday = parseWorkingHours(parser, dayOfWeekTagName);

            } else if (dayOfWeekTagName.equals(XmlTags.DayOfWeek.thursday)) {
                workingHoursForEachDayOfWeek.onThursday = parseWorkingHours(parser, dayOfWeekTagName);

            } else if (dayOfWeekTagName.equals(XmlTags.DayOfWeek.friday)) {
                workingHoursForEachDayOfWeek.onFriday = parseWorkingHours(parser, dayOfWeekTagName);

            } else if (dayOfWeekTagName.equals(XmlTags.DayOfWeek.saturday)) {
                workingHoursForEachDayOfWeek.onSaturday = parseWorkingHours(parser, dayOfWeekTagName);

            } else if (dayOfWeekTagName.equals(XmlTags.DayOfWeek.sunday)) {
                workingHoursForEachDayOfWeek.onSunday = parseWorkingHours(parser, dayOfWeekTagName);

            } else {
                skip(parser);
            }
        }
        return new WeekWorkingHours(workingHoursForEachDayOfWeek);
    }

    private WorkingHours parseWorkingHours(XmlPullParser parser, String dayOfWeekTagName) throws Exception {
        String workingHoursText = parseTextByTag(parser, dayOfWeekTagName);
        WorkingHoursParser workingHoursParser = new WorkingHoursParser();
        return workingHoursParser.parse(workingHoursText);
    }

    private void skip(XmlPullParser parser) throws Exception {
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
}
