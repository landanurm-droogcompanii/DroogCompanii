package ru.droogcompanii.application.data.xml_parser.partners_xml_parser;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerCategory;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.data.working_hours.WeekWorkingHours;
import ru.droogcompanii.application.data.working_hours.WorkingHours;
import ru.droogcompanii.application.data.working_hours.WorkingHoursForEachDayOfWeek;
import ru.droogcompanii.application.data.working_hours.parser.WorkingHoursParser;
import ru.droogcompanii.application.data.xml_parser.XmlParserUtils;
import ru.droogcompanii.application.util.StringConstants;

/**
 * Created by Leonid on 02.12.13.
 */
public class PartnersXmlParser {

    public static class ParsedData {
        public final Collection<PartnerCategory> partnerCategories;
        public final Collection<Partner> partners;
        public final Collection<PartnerPoint> partnerPoints;

        public ParsedData(Collection<PartnerCategory> partnerCategories,
                          Collection<Partner> partners,
                          Collection<PartnerPoint> partnerPoints) {
            this.partnerCategories = partnerCategories;
            this.partners = partners;
            this.partnerPoints = partnerPoints;
        }
    }

    private static final StringConstants.PartnersXmlConstants.Tags
            TAGS = new StringConstants.PartnersXmlConstants.Tags();

    private static final String NAMESPACE = null;

    private Collection<PartnerCategory> outPartnerCategories;
    private Collection<Partner> outPartners;
    private Collection<PartnerPoint> outPartnerPoints;


    public ParsedData parse(InputStream in) throws Exception {
        try {
            return parse(XmlParserUtils.prepareParser(in));
        } finally {
            XmlParserUtils.tryClose(in);
        }
    }

    private ParsedData parse(XmlPullParser parser) throws Exception {
        init();
        parsePartnerCategories(parser);
        return new ParsedData(outPartnerCategories, outPartners, outPartnerPoints);
    }

    private void init() {
        outPartnerCategories = new ArrayList<PartnerCategory>();

        // Несмотря на то, что возможно дублирование партнеров, входящих в несколько категорий,
        // необходимо сохранять всех партнеров, так как в каждом из них хранится ссылка
        // на категорию, а один партнер может одновременно входить в несколько категорий.
        // Поэтому используется тип данных список (List).
        // Если не сохранять копии партнера, то мы потеряем ссылки от партнера к категориям,
        // в которые он входит. В итоге получится, что те партнеры, которые входят в несколько
        // категорий, после парсинга будут относится только к одной категории.
        // С другой стороны, если понадобится выводить всех партнеров, нужно будет избежать
        // дублирования партнеров, которые входит в несколько категорий.
        // Поэтому при выводе всех партнеров нужно будет использовать коллекцию типа множество (Set).
        outPartners = new ArrayList<Partner>();

        // Тип коллекции партнерских точек должен быть Множеством (Set),
        // для того, чтобы избежать дублирование точек тех партнеров,
        // которые одновременно входят в несколько категорий.
        // Сохранив однажды партнерскую точку, мы можем не сохранять больше
        // ее копии, так как партнерская точка всегда относится к одному и
        // тому же партнеру, она не может относиться к нескольким партнерам.
        outPartnerPoints = new HashSet<PartnerPoint>();
    }

    private void parsePartnerCategories(XmlPullParser parser) throws Exception {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, TAGS.partnerCategories);

        int partnerCategoryId = 0;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String tag = parser.getName();
            if (tag.equals(TAGS.partnerCategory)) {
                ++partnerCategoryId;
                PartnerCategory partnerCategory = parsePartnerCategory(parser, partnerCategoryId);
                outPartnerCategories.add(partnerCategory);
            } else {
                XmlParserUtils.skip(parser);
            }
        }
    }

    private PartnerCategory parsePartnerCategory(
                            XmlPullParser parser, int partnerCategoryId) throws Exception {

        parser.require(XmlPullParser.START_TAG, NAMESPACE, TAGS.partnerCategory);

        String title = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String tag = parser.getName();
            if (tag.equals(TAGS.title)) {
                title = parseTitle(parser);
            } else if (tag.equals(TAGS.partners)) {
                parsePartners(parser, partnerCategoryId);
            } else {
                XmlParserUtils.skip(parser);
            }
        }
        return new PartnerCategory(partnerCategoryId, title);
    }

    private static String parseTitle(XmlPullParser parser) throws Exception {
        return XmlParserUtils.parseTextByTag(parser, TAGS.title);
    }

    private void parsePartners(XmlPullParser parser, int partnerCategoryId) throws Exception {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, TAGS.partners);

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String tag = parser.getName();
            if (tag.equals(TAGS.partner)) {
                Partner partner = parsePartner(parser, partnerCategoryId);
                outPartners.add(partner);
            } else {
                XmlParserUtils.skip(parser);
            }
        }
    }

    private Partner parsePartner(XmlPullParser parser, int partnerCategoryId) throws Exception {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, TAGS.partner);

        int id = 0;
        String title = null;
        String fullTitle = null;
        String discountType = null;
        int discount = 0;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String tag = parser.getName();
            if (tag.equals(TAGS.id)) {
                id = parseId(parser);
            } else if (tag.equals(TAGS.title)) {
                title = parseTitle(parser);
            } else if (tag.equals(TAGS.fullTitle)) {
                fullTitle = parseFullTitle(parser);
            } else if (tag.equals(TAGS.discountType)) {
                discountType = parseDiscountType(parser);
            } else if (tag.equals(TAGS.discount)) {
                discount = parseDiscount(parser);
            } else if (tag.equals(TAGS.partnerPoints)) {
                parsePartnerPoints(parser, id);
            } else {
                XmlParserUtils.skip(parser);
            }
        }
        return new Partner(id, title, fullTitle, discountType, discount, partnerCategoryId);
    }

    private static Integer parseId(XmlPullParser parser) throws Exception {
        return XmlParserUtils.parseIntegerByTag(parser, TAGS.id);
    }

    private static String parseFullTitle(XmlPullParser parser) throws Exception {
        return XmlParserUtils.parseTextByTag(parser, TAGS.fullTitle);
    }

    private static String parseDiscountType(XmlPullParser parser) throws Exception {
        return XmlParserUtils.parseTextByTag(parser, TAGS.discountType);
    }

    private static int parseDiscount(XmlPullParser parser) throws Exception {
        return XmlParserUtils.parseIntegerByTag(parser, TAGS.discount);
    }

    private void parsePartnerPoints(XmlPullParser parser, int partnerId) throws Exception {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, TAGS.partnerPoints);

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String tag = parser.getName();
            if (tag.equals(TAGS.partnerPoint)) {
                outPartnerPoints.add(parsePartnerPoint(parser, partnerId));
            } else {
                XmlParserUtils.skip(parser);
            }
        }
    }

    private PartnerPoint parsePartnerPoint(XmlPullParser parser, int partnerId) throws Exception {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, TAGS.partnerPoint);

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
            if (tag.equals(TAGS.title)) {
                title = parseTitle(parser);
            } else if (tag.equals(TAGS.address)) {
                address = parseAddress(parser);
            } else if (tag.equals(TAGS.longitude)) {
                longitude = parseLongitude(parser);
            } else if (tag.equals(TAGS.latitude)) {
                latitude = parseLatitude(parser);
            } else if (tag.equals(TAGS.phones)) {
                phones = parsePhones(parser);
            } else if (tag.equals(TAGS.paymentMethods)) {
                paymentMethods = parsePaymentMethods(parser);
            } else if (tag.equals(TAGS.workinghours)) {
                weekWorkingHours = parseWeekWorkingHours(parser);
            } else {
                XmlParserUtils.skip(parser);
            }
        }
        return new PartnerPoint(
            title, address, phones, weekWorkingHours, paymentMethods, longitude, latitude, partnerId
        );
    }

    private static String parseAddress(XmlPullParser parser) throws Exception {
        return XmlParserUtils.parseTextByTag(parser, TAGS.address);
    }

    private static double parseLongitude(XmlPullParser parser) throws Exception {
        return XmlParserUtils.parseDoubleByTag(parser, TAGS.longitude);
    }

    private static double parseLatitude(XmlPullParser parser) throws Exception {
        return XmlParserUtils.parseDoubleByTag(parser, TAGS.latitude);
    }

    private static List<String> parsePhones(XmlPullParser parser) throws Exception {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, TAGS.phones);

        List<String> phones = new ArrayList<String>();
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String tag = parser.getName();
            if (tag.equals(TAGS.phone)) {
                phones.add(parsePhone(parser));
            } else {
                XmlParserUtils.skip(parser);
            }
        }
        return phones;
    }

    private static String parsePhone(XmlPullParser parser) throws Exception {
        return XmlParserUtils.parseTextByTag(parser, TAGS.phone);
    }

    private static String parsePaymentMethods(XmlPullParser parser) throws Exception {
        return XmlParserUtils.parseTextByTag(parser, TAGS.paymentMethods);
    }

    private static WeekWorkingHours parseWeekWorkingHours(XmlPullParser parser) throws Exception {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, TAGS.workinghours);

        WorkingHoursForEachDayOfWeek workingHoursForEachDayOfWeek = new WorkingHoursForEachDayOfWeek();

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String dayOfWeekTagName = parser.getName();
            String typeOfDay = parseTypeOfDayAttribute(parser);

            if (dayOfWeekTagName.equals(StringConstants.PartnersXmlConstants.DayOfWeek.monday)) {
                workingHoursForEachDayOfWeek.onMonday =
                        parseWorkingHours(parser, dayOfWeekTagName, typeOfDay);

            } else if (dayOfWeekTagName.equals(StringConstants.PartnersXmlConstants.DayOfWeek.tuesday)) {
                workingHoursForEachDayOfWeek.onTuesday =
                        parseWorkingHours(parser, dayOfWeekTagName, typeOfDay);

            } else if (dayOfWeekTagName.equals(StringConstants.PartnersXmlConstants.DayOfWeek.wednesday)) {
                workingHoursForEachDayOfWeek.onWednesday =
                        parseWorkingHours(parser, dayOfWeekTagName, typeOfDay);

            } else if (dayOfWeekTagName.equals(StringConstants.PartnersXmlConstants.DayOfWeek.thursday)) {
                workingHoursForEachDayOfWeek.onThursday =
                        parseWorkingHours(parser, dayOfWeekTagName, typeOfDay);

            } else if (dayOfWeekTagName.equals(StringConstants.PartnersXmlConstants.DayOfWeek.friday)) {
                workingHoursForEachDayOfWeek.onFriday =
                        parseWorkingHours(parser, dayOfWeekTagName, typeOfDay);

            } else if (dayOfWeekTagName.equals(StringConstants.PartnersXmlConstants.DayOfWeek.saturday)) {
                workingHoursForEachDayOfWeek.onSaturday =
                        parseWorkingHours(parser, dayOfWeekTagName, typeOfDay);

            } else if (dayOfWeekTagName.equals(StringConstants.PartnersXmlConstants.DayOfWeek.sunday)) {
                workingHoursForEachDayOfWeek.onSunday =
                        parseWorkingHours(parser, dayOfWeekTagName, typeOfDay);

            } else {
                XmlParserUtils.skip(parser);
            }
        }
        return new WeekWorkingHours(workingHoursForEachDayOfWeek);
    }

    private static String parseTypeOfDayAttribute(XmlPullParser parser) throws Exception {
        final String ATTRIBUTE_TYPE_OF_DAY = StringConstants.PartnersXmlConstants.Attributes.typeOfDay;
        XmlParserUtils.requireAttributes(parser, ATTRIBUTE_TYPE_OF_DAY);
        Map<String, String> attributes = XmlParserUtils.parseAttributes(parser);
        return attributes.get(ATTRIBUTE_TYPE_OF_DAY);
    }

    private static WorkingHours parseWorkingHours(XmlPullParser parser,
                    String dayOfWeekTagName, String typeOfDay) throws Exception {
        String workingHoursText = XmlParserUtils.parseTextByTag(parser, dayOfWeekTagName);
        WorkingHoursParser workingHoursParser = new WorkingHoursParser();
        return workingHoursParser.parse(typeOfDay, workingHoursText);
    }
}
