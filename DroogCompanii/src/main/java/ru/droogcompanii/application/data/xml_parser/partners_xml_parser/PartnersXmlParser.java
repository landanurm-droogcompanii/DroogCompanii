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
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerCategoryImpl;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerImpl;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPointImpl;
import ru.droogcompanii.application.data.working_hours.WeekWorkingHours;
import ru.droogcompanii.application.data.working_hours.WorkingHours;
import ru.droogcompanii.application.data.working_hours.WorkingHoursForEachDayOfWeek;
import ru.droogcompanii.application.data.working_hours.parser.WorkingHoursParser;
import ru.droogcompanii.application.data.xml_parser.XmlParserUtils;
import ru.droogcompanii.application.util.constants.StringConstants;

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

        PartnerCategoryImpl partnerCategory = new PartnerCategoryImpl();
        partnerCategory.id = partnerCategoryId;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String tag = parser.getName();
            if (tag.equals(TAGS.title)) {
                partnerCategory.title = XmlParserUtils.parseTextByTag(parser, tag);
            } else if (tag.equals(TAGS.partners)) {
                parsePartners(parser, partnerCategoryId);
            } else {
                XmlParserUtils.skip(parser);
            }
        }
        return partnerCategory;
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

        PartnerImpl partner = new PartnerImpl();

        partner.categoryId = partnerCategoryId;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String tag = parser.getName();
            if (tag.equals(TAGS.id)) {
                partner.id = XmlParserUtils.parseIntegerByTag(parser, tag);
            } else if (tag.equals(TAGS.title)) {
                partner.title = XmlParserUtils.parseTextByTag(parser, tag);
            } else if (tag.equals(TAGS.fullTitle)) {
                partner.fullTitle = XmlParserUtils.parseTextByTag(parser, tag);
            } else if (tag.equals(TAGS.imageUrl)) {
                partner.imageUrl = XmlParserUtils.parseTextByTag(parser, tag);
            } else if (tag.equals(TAGS.description)) {
                partner.description = XmlParserUtils.parseTextByTag(parser, tag);
            } else if (tag.equals(TAGS.webSites)) {
                partner.webSites = parseWebSites(parser);
            } else if (tag.equals(TAGS.emails)) {
                partner.emails = parseEmails(parser);
            } else if (tag.equals(TAGS.discountType)) {
                partner.discountType = XmlParserUtils.parseTextByTag(parser, tag);
            } else if (tag.equals(TAGS.discountSize)) {
                partner.discountSize = XmlParserUtils.parseIntegerByTag(parser, tag);
            } else if (tag.equals(TAGS.partnerPoints)) {
                parsePartnerPoints(parser, partner.getId());
            } else {
                XmlParserUtils.skip(parser);
            }
        }
        return partner;
    }

    private List<String> parseEmails(XmlPullParser parser) throws Exception {
        return parseArrayOfTextFields(parser, TAGS.emails, TAGS.email);
    }

    private static List<String> parseWebSites(XmlPullParser parser) throws Exception {
        return parseArrayOfTextFields(parser, TAGS.webSites, TAGS.webSite);
    }

    private static List<String> parseArrayOfTextFields(
            XmlPullParser parser, String arrayTag, String elementTag) throws Exception {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, arrayTag);

        List<String> elements = new ArrayList<String>();
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String tag = parser.getName();
            if (tag.equals(elementTag)) {
                elements.add(XmlParserUtils.parseTextByTag(parser, elementTag));
            } else {
                XmlParserUtils.skip(parser);
            }
        }
        return elements;
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

    private PartnerPointImpl parsePartnerPoint(XmlPullParser parser, int partnerId) throws Exception {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, TAGS.partnerPoint);

        PartnerPointImpl partnerPoint = new PartnerPointImpl();

        partnerPoint.partnerId = partnerId;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String tag = parser.getName();
            if (tag.equals(TAGS.id)) {
                partnerPoint.id = XmlParserUtils.parseIntegerByTag(parser, tag);
            } else if (tag.equals(TAGS.title)) {
                partnerPoint.title = XmlParserUtils.parseTextByTag(parser, tag);
            } else if (tag.equals(TAGS.address)) {
                partnerPoint.address = XmlParserUtils.parseTextByTag(parser, tag);
            } else if (tag.equals(TAGS.longitude)) {
                partnerPoint.longitude = XmlParserUtils.parseDoubleByTag(parser, tag);
            } else if (tag.equals(TAGS.latitude)) {
                partnerPoint.latitude = XmlParserUtils.parseDoubleByTag(parser, tag);
            } else if (tag.equals(TAGS.phones)) {
                partnerPoint.phones = parsePhones(parser);
            } else if (tag.equals(TAGS.paymentMethods)) {
                partnerPoint.paymentMethods = XmlParserUtils.parseTextByTag(parser, tag);
            } else if (tag.equals(TAGS.workinghours)) {
                partnerPoint.workingHours = parseWeekWorkingHours(parser);
            } else {
                XmlParserUtils.skip(parser);
            }
        }
        return partnerPoint;
    }

    private static List<String> parsePhones(XmlPullParser parser) throws Exception {
        return parseArrayOfTextFields(parser, TAGS.phones, TAGS.phone);
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
