package ru.droogcompanii.application.data;

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

    private static class Tags {
        public static final String partnerCategories = "partner-categories";
        public static final String partnerCategory = "partner-category";
        public static final String title = "title";
        public static final String partners = "partners";
        public static final String partner = "partner";
        public static final String id = "id";
        public static final String fullTitle = "full-title";
        public static final String saleType = "sale-type";
        public static final String partnerPoints = "partner-points";
        public static final String partnerPoint = "partner-point";
        public static final String address = "address";
        public static final String longitude = "longitude";
        public static final String latitude = "latitude";
        public static final String phones = "phones";
        public static final String phone = "phone";
        public static final String paymentMethods = "payment-methods";
        public static final String workinghours = "workinghours";

        public static class DayOfWeek {
            public static final String monday = "mon";
            public static final String tuesday = "tue";
            public static final String wednesday = "wed";
            public static final String thursday = "thu";
            public static final String friday = "fri";
            public static final String saturday = "sat";
            public static final String sunday = "sun";
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
        parser.require(XmlPullParser.START_TAG, NAMESPACE, Tags.partnerCategories);

        int partnerCategoryId = 0;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String tag = parser.getName();
            if (tag.equals(Tags.partnerCategory)) {
                ++partnerCategoryId;
                PartnerCategory partnerCategory = parsePartnerCategory(parser, partnerCategoryId);
                outPartnerCategories.add(partnerCategory);
            } else {
                skip(parser);
            }
        }
    }

    private PartnerCategory parsePartnerCategory(XmlPullParser parser, int partnerCategoryId) throws Exception {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, Tags.partnerCategory);

        String title = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String tag = parser.getName();
            if (tag.equals(Tags.title)) {
                title = parseTitle(parser);
            } else if (tag.equals(Tags.partners)) {
                parsePartners(parser, partnerCategoryId);
            } else {
                skip(parser);
            }
        }
        return new PartnerCategory(partnerCategoryId, title);
    }

    private String parseTitle(XmlPullParser parser) throws Exception {
        return parseTextByTag(parser, Tags.title);
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
        parser.require(XmlPullParser.START_TAG, NAMESPACE, Tags.partners);

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String tag = parser.getName();
            if (tag.equals(Tags.partner)) {
                Partner partner = parsePartner(parser, partnerCategoryId);
                outPartners.add(partner);
            } else {
                skip(parser);
            }
        }
    }

    private Partner parsePartner(XmlPullParser parser, int partnerCategoryId) throws Exception {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, Tags.partner);

        int id = 0;
        String title = null;
        String fullTitle = null;
        String saleType = null;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String tag = parser.getName();
            if (tag.equals(Tags.id)) {
                id = parseId(parser);
            } else if (tag.equals(Tags.title)) {
                title = parseTitle(parser);
            } else if (tag.equals(Tags.fullTitle)) {
                fullTitle = parseFullTitle(parser);
            } else if (tag.equals(Tags.saleType)) {
                saleType = parseSaleType(parser);
            } else if (tag.equals(Tags.partnerPoints)) {
                parsePartnerPoints(parser, id);
            } else {
                skip(parser);
            }
        }
        return new Partner(id, title, fullTitle, saleType, partnerCategoryId);
    }

    private Integer parseId(XmlPullParser parser) throws Exception {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, Tags.id);
        int id = parseInteger(parser);
        parser.require(XmlPullParser.END_TAG, NAMESPACE, Tags.id);
        return id;
    }

    private int parseInteger(XmlPullParser parser) throws Exception {
        return Integer.valueOf(parseText(parser));
    }

    private String parseFullTitle(XmlPullParser parser) throws Exception {
        return parseTextByTag(parser, Tags.fullTitle);
    }

    private String parseSaleType(XmlPullParser parser) throws Exception {
        return parseTextByTag(parser, Tags.saleType);
    }

    private void parsePartnerPoints(XmlPullParser parser, int partnerId) throws Exception {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, Tags.partnerPoints);

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String tag = parser.getName();
            if (tag.equals(Tags.partnerPoint)) {
                outPartnerPoints.add(parsePartnerPoint(parser, partnerId));
            } else {
                skip(parser);
            }
        }
    }

    private PartnerPoint parsePartnerPoint(XmlPullParser parser, int partnerId) throws Exception {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, Tags.partnerPoint);

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
            if (tag.equals(Tags.title)) {
                title = parseTitle(parser);
            } else if (tag.equals(Tags.address)) {
                address = parseAddress(parser);
            } else if (tag.equals(Tags.longitude)) {
                longitude = parseLongitude(parser);
            } else if (tag.equals(Tags.latitude)) {
                latitude = parseLatitude(parser);
            } else if (tag.equals(Tags.phones)) {
                phones = parsePhones(parser);
            } else if (tag.equals(Tags.paymentMethods)) {
                paymentMethods = parsePaymentMethods(parser);
            } else if (tag.equals(Tags.workinghours)) {
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
        return parseTextByTag(parser, Tags.address);
    }

    private double parseLongitude(XmlPullParser parser) throws Exception {
        return parseDoubleByTag(parser, Tags.longitude);
    }

    private double parseLatitude(XmlPullParser parser) throws Exception {
        return parseDoubleByTag(parser, Tags.latitude);
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
        parser.require(XmlPullParser.START_TAG, NAMESPACE, Tags.phones);

        List<String> phones = new ArrayList<String>();
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String tag = parser.getName();
            if (tag.equals(Tags.phone)) {
                phones.add(parsePhone(parser));
            } else {
                skip(parser);
            }
        }
        return phones;
    }

    private String parsePhone(XmlPullParser parser) throws Exception {
        return parseTextByTag(parser, Tags.phone);
    }

    private String parsePaymentMethods(XmlPullParser parser) throws Exception {
        return parseTextByTag(parser, Tags.paymentMethods);
    }

    private WeekWorkingHours parseWeekWorkingHours(XmlPullParser parser) throws Exception {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, Tags.workinghours);

        WorkingHoursForEachDayOfWeek workingHoursForEachDayOfWeek = new WorkingHoursForEachDayOfWeek();

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String dayOfWeekTagName = parser.getName();

            if (dayOfWeekTagName.equals(Tags.DayOfWeek.monday)) {
                workingHoursForEachDayOfWeek.onMonday = parseWorkingHours(parser, dayOfWeekTagName);

            } else if (dayOfWeekTagName.equals(Tags.DayOfWeek.tuesday)) {
                workingHoursForEachDayOfWeek.onTuesday = parseWorkingHours(parser, dayOfWeekTagName);

            } else if (dayOfWeekTagName.equals(Tags.DayOfWeek.wednesday)) {
                workingHoursForEachDayOfWeek.onWednesday = parseWorkingHours(parser, dayOfWeekTagName);

            } else if (dayOfWeekTagName.equals(Tags.DayOfWeek.thursday)) {
                workingHoursForEachDayOfWeek.onThursday = parseWorkingHours(parser, dayOfWeekTagName);

            } else if (dayOfWeekTagName.equals(Tags.DayOfWeek.friday)) {
                workingHoursForEachDayOfWeek.onFriday = parseWorkingHours(parser, dayOfWeekTagName);

            } else if (dayOfWeekTagName.equals(Tags.DayOfWeek.saturday)) {
                workingHoursForEachDayOfWeek.onSaturday = parseWorkingHours(parser, dayOfWeekTagName);

            } else if (dayOfWeekTagName.equals(Tags.DayOfWeek.sunday)) {
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
