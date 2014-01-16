package ru.droogcompanii.application.data.xml_parser;

import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import ru.droogcompanii.application.data.data_structure.Partner;
import ru.droogcompanii.application.data.data_structure.PartnerCategory;
import ru.droogcompanii.application.data.data_structure.PartnerPoint;
import ru.droogcompanii.application.data.data_structure.working_hours.WeekWorkingHours;
import ru.droogcompanii.application.data.data_structure.working_hours.WorkingHours;
import ru.droogcompanii.application.data.data_structure.working_hours.WorkingHoursForEachDayOfWeek;
import ru.droogcompanii.application.data.data_structure.working_hours.time.Time;
import ru.droogcompanii.application.data.data_structure.working_hours.working_hours_impl.DayAndNightWorkingHours;
import ru.droogcompanii.application.data.data_structure.working_hours.working_hours_impl.WorkingHoursOnBusinessDay;
import ru.droogcompanii.application.data.data_structure.working_hours.working_hours_impl.WorkingHoursOnHoliday;

import static ru.droogcompanii.application.util.DroogCompaniiStringConstants.XmlConstants.Attributes;
import static ru.droogcompanii.application.util.DroogCompaniiStringConstants.XmlConstants.DayOfWeek;
import static ru.droogcompanii.application.util.DroogCompaniiStringConstants.XmlConstants.Tags;
import static ru.droogcompanii.application.util.DroogCompaniiStringConstants.XmlConstants.TypesOfDay;

/**
 * Created by ls on 08.01.14.
 */
public class SimpleTestDroogCompaniiXmlParser extends TestCase {

    private static final String TYPE_ARRAY = " type=\"array\"";
    private static final String TYPE_INTEGER = " type=\"integer\"";
    private static final String TYPE_DECIMAL = " type=\"decimal\"";


    private double longitudeOfPartnerPoint;
    private double latitudeOfPartnerPoint;
    private InputStream xml;
    private int idOfPartner;
    private List<String> phonesOfPartnerPoint;
    private String addressOfPartnerPoint;
    private String fullTitleOfPartner;
    private String paymentMethodsOfPartnerPoint;
    private String saleTypeOfPartner;
    private String titleOfPartner;
    private String titleOfPartnerCategory;
    private String titleOfPartnerPoint;
    private WeekWorkingHours weekWorkingHoursOfPartnerPoint;
    private WorkingHoursForEachDayOfWeek workingHoursOfPartnerPoint;


    private PartnerCategory parsedPartnerCategory;
    private Partner parsedPartner;
    private PartnerPoint parsedPartnerPoint;


    @Override
    public void setUp() throws Exception {
        super.setUp();

        titleOfPartnerCategory = "Partner Category Title";
        idOfPartner = 12;
        titleOfPartner = "Title of Partner 1";
        fullTitleOfPartner = "Full Title of Partner 1";
        saleTypeOfPartner = "Sale Type of Partner 1";
        titleOfPartnerPoint = "Title of Partner Point of Partner 1";
        addressOfPartnerPoint = "Address of Partner Point of Partner 1";
        longitudeOfPartnerPoint = 12.214535;
        latitudeOfPartnerPoint = 32.12414;
        paymentMethodsOfPartnerPoint = "Visa, MasterCard";
        phonesOfPartnerPoint = Arrays.asList("79196763351", "79105352235");

        workingHoursOfPartnerPoint = new WorkingHoursForEachDayOfWeek();
        workingHoursOfPartnerPoint.onMonday = new WorkingHoursOnBusinessDay(new Time(9, 0), new Time(18, 0));
        workingHoursOfPartnerPoint.onTuesday = new WorkingHoursOnBusinessDay(new Time(10, 0), new Time(19, 0));
        workingHoursOfPartnerPoint.onWednesday = new WorkingHoursOnBusinessDay(new Time(9, 0), new Time(19, 0));
        workingHoursOfPartnerPoint.onThursday = new DayAndNightWorkingHours("Day Night");
        workingHoursOfPartnerPoint.onFriday = new WorkingHoursOnBusinessDay(new Time(10, 0), new Time(17, 0));
        workingHoursOfPartnerPoint.onSaturday = new WorkingHoursOnHoliday("Holiday 1");
        workingHoursOfPartnerPoint.onSunday = new WorkingHoursOnHoliday("Holiday 2");
        weekWorkingHoursOfPartnerPoint = new WeekWorkingHours(workingHoursOfPartnerPoint);

        String xmlText = prepareXml();
        xml = stringToInputStream(xmlText);
    }

    private static InputStream stringToInputStream(String str) {
        try {
            return new ByteArrayInputStream(str.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            return new ByteArrayInputStream(str.getBytes());
        }
    }

    private String prepareXml() {
        StringBuilder xmlBuilder = new StringBuilder();

        xmlBuilder
            .append(openArrayTag(Tags.partnerCategories) + "\n")
            .append("\t" + openTag(Tags.partnerCategory) + "\n")
            .append("\t\t" + openTag(Tags.title) + titleOfPartnerCategory + closeTag(Tags.title) + "\n")
            .append("\t\t" + openArrayTag(Tags.partners) + "\n")
            .append("\t\t\t" + openTag(Tags.partner) + "\n")
            .append("\t\t\t\t" + openTag(Tags.id + TYPE_INTEGER) + idOfPartner + closeTag(Tags.id) + "\n")
            .append("\t\t\t\t" + openTag(Tags.title) + titleOfPartner + closeTag(Tags.title) + "\n")
            .append("\t\t\t\t" + openTag(Tags.fullTitle) + fullTitleOfPartner + closeTag(Tags.fullTitle) + "\n")
            .append("\t\t\t\t" + openTag(Tags.saleType) + saleTypeOfPartner + closeTag(Tags.saleType) + "\n")
            .append("\t\t\t\t" + openArrayTag(Tags.partnerPoints) + "\n")
            .append("\t\t\t\t\t" + openTag(Tags.partnerPoint) + "\n")
            .append("\t\t\t\t\t\t" + openTag(Tags.title) + titleOfPartnerPoint + closeTag(Tags.title) + "\n")
            .append("\t\t\t\t\t\t" + openTag(Tags.address) + addressOfPartnerPoint + closeTag(Tags.address) + "\n")
            .append("\t\t\t\t\t\t" + openTag(Tags.longitude + TYPE_DECIMAL) + longitudeOfPartnerPoint + closeTag(Tags.longitude) + "\n")
            .append("\t\t\t\t\t\t" + openTag(Tags.latitude + TYPE_DECIMAL) + latitudeOfPartnerPoint + closeTag(Tags.latitude) + "\n")
            .append("\t\t\t\t\t\t" + openTag(Tags.paymentMethods) + paymentMethodsOfPartnerPoint + closeTag(Tags.paymentMethods) + "\n")
            .append(preparePhonesXml("\t\t\t\t\t\t", phonesOfPartnerPoint) + "\n")
            .append(prepareWorkingHoursXml("\t\t\t\t\t\t", workingHoursOfPartnerPoint) + "\n")
            .append("\t\t\t\t\t" + closeTag(Tags.partnerPoint) + "\n")
            .append("\t\t\t\t" + closeTag(Tags.partnerPoints) + "\n")
            .append("\t\t\t" + closeTag(Tags.partner) + "\n")
            .append("\t\t" + closeTag(Tags.partners) + "\n")
            .append("\t" + closeTag(Tags.partnerCategory) + "\n")
            .append(closeTag(Tags.partnerCategories));

        return xmlBuilder.toString();
    }

    private String preparePhonesXml(String indent, List<String> phones) {
        StringBuilder phonesBuilder = new StringBuilder();
        phonesBuilder.append(indent).append(openArrayTag(Tags.phones) + "\n");
        for (String phone : phones) {
            phonesBuilder.append(indent).append("\t").append(prepareTextWithTags(Tags.phone, phone)).append("\n");
        }
        phonesBuilder.append(indent).append(closeTag(Tags.phones));
        return phonesBuilder.toString();
    }

    private String prepareTextWithTags(String tagName, String value) {
        return openTag(tagName) + value + closeTag(tagName);
    }

    private String prepareWorkingHoursXml(String indent, WorkingHoursForEachDayOfWeek workingHours) {
        StringBuilder builder = new StringBuilder();

        builder.append(indent).append(openArrayTag(Tags.workinghours) + "\n");

        appendWorkingHoursOnDay(builder, indent + "\t", DayOfWeek.monday, workingHours.onMonday);
        appendWorkingHoursOnDay(builder, indent + "\t", DayOfWeek.tuesday, workingHours.onTuesday);
        appendWorkingHoursOnDay(builder, indent + "\t", DayOfWeek.wednesday, workingHours.onWednesday);
        appendWorkingHoursOnDay(builder, indent + "\t", DayOfWeek.thursday, workingHours.onThursday);
        appendWorkingHoursOnDay(builder, indent + "\t", DayOfWeek.friday, workingHours.onFriday);
        appendWorkingHoursOnDay(builder, indent + "\t", DayOfWeek.saturday, workingHours.onSaturday);
        appendWorkingHoursOnDay(builder, indent + "\t", DayOfWeek.sunday, workingHours.onSunday);

        builder.append(indent).append(closeTag(Tags.workinghours));

        return builder.toString();
    }

    private void appendWorkingHoursOnDay(StringBuilder workingHoursBuilder,
                           String indent, String tagOfDay, WorkingHours workingHoursOnDay) {
        String workingHoursOnDayWithinTags = prepareWorkingHoursRow(tagOfDay, workingHoursOnDay);
        workingHoursBuilder.append(indent).append(workingHoursOnDayWithinTags).append("\n");
    }

    private String prepareWorkingHoursRow(String tagOfDay, WorkingHours workingHours) {
        return openTag(tagOfDay + " " + dayTypeAttributeOf(workingHours)) + workingHours + closeTag(tagOfDay);
    }

    private String dayTypeAttributeOf(WorkingHours workingHours) {
        return Attributes.typeOfDay + "=" + quote(dayTypeOf(workingHours));
    }

    private static String quote(String s) {
        return "\"" + s + "\"";
    }

    private String dayTypeOf(WorkingHours workingHours) {
        if (workingHours instanceof WorkingHoursOnHoliday) {
            return TypesOfDay.holiday;
        }
        if (workingHours instanceof WorkingHoursOnBusinessDay) {
            return TypesOfDay.usualDay;
        }
        if (workingHours instanceof DayAndNightWorkingHours) {
            return TypesOfDay.dayAndNight;
        }
        throw new IllegalArgumentException("Unknown type of working hours: " + workingHours);
    }

    private String openArrayTag(String tagName) {
        return openTag(tagName + TYPE_ARRAY);
    }

    private String openTag(String tagName) {
        return "<" + tagName + ">";
    }

    private String closeTag(String tagName) {
        return "</" + tagName + ">";
    }


    public void test() throws Exception {

        DroogCompaniiXmlParser testedXmlParser = new DroogCompaniiXmlParser();
        DroogCompaniiXmlParser.ParsedData parsedData = testedXmlParser.parse(xml);

        assertEquals(1, numberOf(parsedData.partnerCategories));
        assertEquals(1, numberOf(parsedData.partners));
        assertEquals(1, numberOf(parsedData.partnerPoints));

        parsedPartnerCategory = getFirstElementOf(parsedData.partnerCategories);
        parsedPartner = getFirstElementOf(parsedData.partners);
        parsedPartnerPoint = getFirstElementOf(parsedData.partnerPoints);

        assertParsedPartnerCategoryIsCorrect();
        assertParsedPartnerIsCorrect();
        assertParsedPartnerPointIsCorrect();
    }

    private static <T> T getFirstElementOf(Collection<T> elements) {
        for (T element : elements) {
            return element;
        }
        return null;
    }

    private static <T> int numberOf(Collection<T> elements) {
        return elements.size();
    }

    private void assertParsedPartnerCategoryIsCorrect() {
        assertEquals(titleOfPartnerCategory, parsedPartnerCategory.title);
    }

    private void assertParsedPartnerIsCorrect() {
        assertEquals(idOfPartner, parsedPartner.id);
        assertEquals(titleOfPartner, parsedPartner.title);
        assertEquals(fullTitleOfPartner, parsedPartner.fullTitle);
        assertEquals(saleTypeOfPartner, parsedPartner.saleType);
        assertEquals(parsedPartnerCategory.id, parsedPartner.categoryId);
    }

    private void assertParsedPartnerPointIsCorrect() {
        assertEquals(titleOfPartnerPoint, parsedPartnerPoint.title);
        assertEquals(addressOfPartnerPoint, parsedPartnerPoint.address);
        assertEquals(paymentMethodsOfPartnerPoint, parsedPartnerPoint.paymentMethods);
        assertEquals(longitudeOfPartnerPoint, parsedPartnerPoint.longitude);
        assertEquals(latitudeOfPartnerPoint, parsedPartnerPoint.latitude);
        assertEquals(phonesOfPartnerPoint, parsedPartnerPoint.phones);
        assertEquals(weekWorkingHoursOfPartnerPoint, parsedPartnerPoint.workingHours);
        assertEquals(parsedPartner.id, parsedPartnerPoint.partnerId);
    }


    @Override
    protected void tearDown() throws Exception {
        xml.close();

        super.tearDown();
    }

}
