package ru.droogcompanii.application.data.xml_parser;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerCategory;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.data.working_hours.WeekWorkingHours;
import ru.droogcompanii.application.data.working_hours.WorkingHours;
import ru.droogcompanii.application.data.working_hours.WorkingHoursForEachDayOfWeek;
import ru.droogcompanii.application.data.working_hours.WorkingHoursParser;
import ru.droogcompanii.application.util.DroogCompaniiStringConstants.XmlConstants;

/**
 * Created by Leonid on 02.12.13.
 */
public class DroogCompaniiXmlParser {

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


    private Collection<PartnerCategory> outPartnerCategories;
    private Collection<Partner> outPartners;
    private Collection<PartnerPoint> outPartnerPoints;


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
        parser.require(XmlPullParser.START_TAG, NAMESPACE, XmlConstants.Tags.partnerCategories);

        int partnerCategoryId = 0;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String tag = parser.getName();
            if (tag.equals(XmlConstants.Tags.partnerCategory)) {
                ++partnerCategoryId;
                PartnerCategory partnerCategory = parsePartnerCategory(parser, partnerCategoryId);
                outPartnerCategories.add(partnerCategory);
            } else {
                skip(parser);
            }
        }
    }

    private PartnerCategory parsePartnerCategory(XmlPullParser parser, int partnerCategoryId) throws Exception {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, XmlConstants.Tags.partnerCategory);

        String title = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String tag = parser.getName();
            if (tag.equals(XmlConstants.Tags.title)) {
                title = parseTitle(parser);
            } else if (tag.equals(XmlConstants.Tags.partners)) {
                parsePartners(parser, partnerCategoryId);
            } else {
                skip(parser);
            }
        }
        return new PartnerCategory(partnerCategoryId, title);
    }

    private String parseTitle(XmlPullParser parser) throws Exception {
        return parseTextByTag(parser, XmlConstants.Tags.title);
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
        parser.require(XmlPullParser.START_TAG, NAMESPACE, XmlConstants.Tags.partners);

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String tag = parser.getName();
            if (tag.equals(XmlConstants.Tags.partner)) {
                Partner partner = parsePartner(parser, partnerCategoryId);
                outPartners.add(partner);
            } else {
                skip(parser);
            }
        }
    }

    private Partner parsePartner(XmlPullParser parser, int partnerCategoryId) throws Exception {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, XmlConstants.Tags.partner);

        int id = 0;
        String title = null;
        String fullTitle = null;
        String saleType = null;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String tag = parser.getName();
            if (tag.equals(XmlConstants.Tags.id)) {
                id = parseId(parser);
            } else if (tag.equals(XmlConstants.Tags.title)) {
                title = parseTitle(parser);
            } else if (tag.equals(XmlConstants.Tags.fullTitle)) {
                fullTitle = parseFullTitle(parser);
            } else if (tag.equals(XmlConstants.Tags.saleType)) {
                saleType = parseSaleType(parser);
            } else if (tag.equals(XmlConstants.Tags.partnerPoints)) {
                parsePartnerPoints(parser, id);
            } else {
                skip(parser);
            }
        }
        return new Partner(id, title, fullTitle, saleType, partnerCategoryId);
    }

    private Integer parseId(XmlPullParser parser) throws Exception {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, XmlConstants.Tags.id);
        int id = parseInteger(parser);
        parser.require(XmlPullParser.END_TAG, NAMESPACE, XmlConstants.Tags.id);
        return id;
    }

    private int parseInteger(XmlPullParser parser) throws Exception {
        return Integer.valueOf(parseText(parser));
    }

    private String parseFullTitle(XmlPullParser parser) throws Exception {
        return parseTextByTag(parser, XmlConstants.Tags.fullTitle);
    }

    private String parseSaleType(XmlPullParser parser) throws Exception {
        return parseTextByTag(parser, XmlConstants.Tags.saleType);
    }

    private void parsePartnerPoints(XmlPullParser parser, int partnerId) throws Exception {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, XmlConstants.Tags.partnerPoints);

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String tag = parser.getName();
            if (tag.equals(XmlConstants.Tags.partnerPoint)) {
                outPartnerPoints.add(parsePartnerPoint(parser, partnerId));
            } else {
                skip(parser);
            }
        }
    }

    private PartnerPoint parsePartnerPoint(XmlPullParser parser, int partnerId) throws Exception {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, XmlConstants.Tags.partnerPoint);

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
            if (tag.equals(XmlConstants.Tags.title)) {
                title = parseTitle(parser);
            } else if (tag.equals(XmlConstants.Tags.address)) {
                address = parseAddress(parser);
            } else if (tag.equals(XmlConstants.Tags.longitude)) {
                longitude = parseLongitude(parser);
            } else if (tag.equals(XmlConstants.Tags.latitude)) {
                latitude = parseLatitude(parser);
            } else if (tag.equals(XmlConstants.Tags.phones)) {
                phones = parsePhones(parser);
            } else if (tag.equals(XmlConstants.Tags.paymentMethods)) {
                paymentMethods = parsePaymentMethods(parser);
            } else if (tag.equals(XmlConstants.Tags.workinghours)) {
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
        return parseTextByTag(parser, XmlConstants.Tags.address);
    }

    private double parseLongitude(XmlPullParser parser) throws Exception {
        return parseDoubleByTag(parser, XmlConstants.Tags.longitude);
    }

    private double parseLatitude(XmlPullParser parser) throws Exception {
        return parseDoubleByTag(parser, XmlConstants.Tags.latitude);
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
        parser.require(XmlPullParser.START_TAG, NAMESPACE, XmlConstants.Tags.phones);

        List<String> phones = new ArrayList<String>();
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String tag = parser.getName();
            if (tag.equals(XmlConstants.Tags.phone)) {
                phones.add(parsePhone(parser));
            } else {
                skip(parser);
            }
        }
        return phones;
    }

    private String parsePhone(XmlPullParser parser) throws Exception {
        return parseTextByTag(parser, XmlConstants.Tags.phone);
    }

    private String parsePaymentMethods(XmlPullParser parser) throws Exception {
        return parseTextByTag(parser, XmlConstants.Tags.paymentMethods);
    }

    private WeekWorkingHours parseWeekWorkingHours(XmlPullParser parser) throws Exception {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, XmlConstants.Tags.workinghours);

        WorkingHoursForEachDayOfWeek workingHoursForEachDayOfWeek = new WorkingHoursForEachDayOfWeek();

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String dayOfWeekTagName = parser.getName();
            String typeOfDay = parseTypeOfDayAttribute(parser);

            if (dayOfWeekTagName.equals(XmlConstants.DayOfWeek.monday)) {
                workingHoursForEachDayOfWeek.onMonday = parseWorkingHours(parser, dayOfWeekTagName, typeOfDay);

            } else if (dayOfWeekTagName.equals(XmlConstants.DayOfWeek.tuesday)) {
                workingHoursForEachDayOfWeek.onTuesday = parseWorkingHours(parser, dayOfWeekTagName, typeOfDay);

            } else if (dayOfWeekTagName.equals(XmlConstants.DayOfWeek.wednesday)) {
                workingHoursForEachDayOfWeek.onWednesday = parseWorkingHours(parser, dayOfWeekTagName, typeOfDay);

            } else if (dayOfWeekTagName.equals(XmlConstants.DayOfWeek.thursday)) {
                workingHoursForEachDayOfWeek.onThursday = parseWorkingHours(parser, dayOfWeekTagName, typeOfDay);

            } else if (dayOfWeekTagName.equals(XmlConstants.DayOfWeek.friday)) {
                workingHoursForEachDayOfWeek.onFriday = parseWorkingHours(parser, dayOfWeekTagName, typeOfDay);

            } else if (dayOfWeekTagName.equals(XmlConstants.DayOfWeek.saturday)) {
                workingHoursForEachDayOfWeek.onSaturday = parseWorkingHours(parser, dayOfWeekTagName, typeOfDay);

            } else if (dayOfWeekTagName.equals(XmlConstants.DayOfWeek.sunday)) {
                workingHoursForEachDayOfWeek.onSunday = parseWorkingHours(parser, dayOfWeekTagName, typeOfDay);

            } else {
                skip(parser);
            }
        }
        return new WeekWorkingHours(workingHoursForEachDayOfWeek);
    }

    private String parseTypeOfDayAttribute(XmlPullParser parser) throws Exception {
        requireAttributes(parser, XmlConstants.Attributes.typeOfDay);
        Map<String, String> attributes = parseAttributes(parser);
        return attributes.get(XmlConstants.Attributes.typeOfDay);
    }

    private void requireAttributes(XmlPullParser parser, String... requiredAttributes) {
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

    private Map<String, String> parseAttributes(XmlPullParser parser) {
        int attributeCount = parser.getAttributeCount();
        Map<String, String> attributes = new HashMap<String, String>(attributeCount);
        for(int i = 0; i < attributeCount; i++) {
            attributes.put(parser.getAttributeName(i), parser.getAttributeValue(i));
        }
        return attributes;
    }

    private WorkingHours parseWorkingHours(XmlPullParser parser,
                    String dayOfWeekTagName, String typeOfDay) throws Exception {
        String workingHoursText = parseTextByTag(parser, dayOfWeekTagName);
        WorkingHoursParser workingHoursParser = new WorkingHoursParser();
        return workingHoursParser.parse(typeOfDay, workingHoursText);
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
