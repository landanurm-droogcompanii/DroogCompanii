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
import ru.droogcompanii.application.data.data_structure.working_hours.Time;
import ru.droogcompanii.application.data.data_structure.working_hours.WeekWorkingHours;
import ru.droogcompanii.application.data.data_structure.working_hours.WorkingHours;
import ru.droogcompanii.application.data.data_structure.working_hours.WorkingHoursForEachDayOfWeek;
import ru.droogcompanii.application.data.data_structure.working_hours.working_hours_impl.WorkingHoursBuilder;

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
        WorkingHoursBuilder workingHoursBuilder = new WorkingHoursBuilder();
        workingHoursOfPartnerPoint.onMonday = workingHoursBuilder.onBusinessDay(new Time(9, 0), new Time(18, 0));
        workingHoursOfPartnerPoint.onTuesday = workingHoursBuilder.onBusinessDay(new Time(10, 0), new Time(19, 0));
        workingHoursOfPartnerPoint.onWednesday = workingHoursBuilder.onBusinessDay(new Time(9, 0), new Time(19, 0));
        workingHoursOfPartnerPoint.onThursday = workingHoursBuilder.onBusinessDay(new Time(8, 0), new Time(17, 0));
        workingHoursOfPartnerPoint.onFriday = workingHoursBuilder.onBusinessDay(new Time(10, 0), new Time(17, 0));
        workingHoursOfPartnerPoint.onSaturday = workingHoursBuilder.onHoliday("Holiday");
        workingHoursOfPartnerPoint.onSunday = workingHoursBuilder.onHoliday("Holiday");
        weekWorkingHoursOfPartnerPoint = new WeekWorkingHours(workingHoursOfPartnerPoint);

        xml = stringToInputStream(prepareXml());
    }

    private InputStream stringToInputStream(String str) {
        try {
            return new ByteArrayInputStream(str.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            return new ByteArrayInputStream(str.getBytes());
        }
    }

    private String prepareXml() {
        StringBuilder xmlBuilder = new StringBuilder();

        xmlBuilder
            .append(openArrayTag(XmlTags.partnerCategories) + "\n")
            .append("\t" + openTag(XmlTags.partnerCategory) + "\n")
            .append("\t\t" + openTag(XmlTags.title) + titleOfPartnerCategory + closeTag(XmlTags.title) + "\n")
            .append("\t\t" + openArrayTag(XmlTags.partners) + "\n")
            .append("\t\t\t" + openTag(XmlTags.partner) + "\n")
            .append("\t\t\t\t" + openTag(XmlTags.id + TYPE_INTEGER) + idOfPartner + closeTag(XmlTags.id) + "\n")
            .append("\t\t\t\t" + openTag(XmlTags.title) + titleOfPartner + closeTag(XmlTags.title) + "\n")
            .append("\t\t\t\t" + openTag(XmlTags.fullTitle) + fullTitleOfPartner + closeTag(XmlTags.fullTitle) + "\n")
            .append("\t\t\t\t" + openTag(XmlTags.saleType) + saleTypeOfPartner + closeTag(XmlTags.saleType) + "\n")
            .append("\t\t\t\t" + openArrayTag(XmlTags.partnerPoints) + "\n")
            .append("\t\t\t\t\t" + openTag(XmlTags.partnerPoint) + "\n")
            .append("\t\t\t\t\t\t" + openTag(XmlTags.title) + titleOfPartnerPoint + closeTag(XmlTags.title) + "\n")
            .append("\t\t\t\t\t\t" + openTag(XmlTags.address) + addressOfPartnerPoint + closeTag(XmlTags.address) + "\n")
            .append("\t\t\t\t\t\t" + openTag(XmlTags.longitude + TYPE_DECIMAL) + longitudeOfPartnerPoint + closeTag(XmlTags.longitude) + "\n")
            .append("\t\t\t\t\t\t" + openTag(XmlTags.latitude + TYPE_DECIMAL) + latitudeOfPartnerPoint + closeTag(XmlTags.latitude) + "\n")
            .append("\t\t\t\t\t\t" + openTag(XmlTags.paymentMethods) + paymentMethodsOfPartnerPoint + closeTag(XmlTags.paymentMethods) + "\n")
            .append(preparePhonesXml("\t\t\t\t\t\t", phonesOfPartnerPoint) + "\n")
            .append(prepareWorkingHoursXml("\t\t\t\t\t\t", workingHoursOfPartnerPoint) + "\n")
            .append("\t\t\t\t\t" + closeTag(XmlTags.partnerPoint) + "\n")
            .append("\t\t\t\t" + closeTag(XmlTags.partnerPoints) + "\n")
            .append("\t\t\t" + closeTag(XmlTags.partner) + "\n")
            .append("\t\t" + closeTag(XmlTags.partners) + "\n")
            .append("\t" + closeTag(XmlTags.partnerCategory) + "\n")
            .append(closeTag(XmlTags.partnerCategories));

        return xmlBuilder.toString();
    }

    private String preparePhonesXml(String indent, List<String> phones) {
        StringBuilder phonesBuilder = new StringBuilder();
        phonesBuilder.append(indent).append(openArrayTag(XmlTags.phones) + "\n");
        for (String phone : phones) {
            phonesBuilder.append(indent).append("\t").append(prepareTextWithinTags(XmlTags.phone, phone)).append("\n");
        }
        phonesBuilder.append(indent).append(closeTag(XmlTags.phones));
        return phonesBuilder.toString();
    }

    private String prepareTextWithinTags(String tagName, String value) {
        return openTag(tagName) + value + closeTag(tagName);
    }

    private String prepareWorkingHoursXml(String indent, WorkingHoursForEachDayOfWeek workingHours) {
        StringBuilder workingHoursBuilder = new StringBuilder();

        workingHoursBuilder.append(indent).append(openArrayTag(XmlTags.workinghours) + "\n");

        appendWorkingHoursOnDay(workingHoursBuilder, indent + "\t", XmlTags.DayOfWeek.monday, workingHours.onMonday);
        appendWorkingHoursOnDay(workingHoursBuilder, indent + "\t", XmlTags.DayOfWeek.tuesday, workingHours.onTuesday);
        appendWorkingHoursOnDay(workingHoursBuilder, indent + "\t", XmlTags.DayOfWeek.wednesday, workingHours.onWednesday);
        appendWorkingHoursOnDay(workingHoursBuilder, indent + "\t", XmlTags.DayOfWeek.thursday, workingHours.onThursday);
        appendWorkingHoursOnDay(workingHoursBuilder, indent + "\t", XmlTags.DayOfWeek.friday, workingHours.onFriday);
        appendWorkingHoursOnDay(workingHoursBuilder, indent + "\t", XmlTags.DayOfWeek.saturday, workingHours.onSaturday);
        appendWorkingHoursOnDay(workingHoursBuilder, indent + "\t", XmlTags.DayOfWeek.sunday, workingHours.onSunday);

        workingHoursBuilder.append(indent).append(closeTag(XmlTags.workinghours));

        return workingHoursBuilder.toString();
    }

    private void appendWorkingHoursOnDay(StringBuilder workingHoursBuilder,
                           String indent, String tagOfDay, WorkingHours workingHoursOnDay) {
        String workingHoursOnDayWithinTags = prepareTextWithinTags(tagOfDay, workingHoursOnDay.toString());
        workingHoursBuilder.append(indent).append(workingHoursOnDayWithinTags).append("\n");
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
