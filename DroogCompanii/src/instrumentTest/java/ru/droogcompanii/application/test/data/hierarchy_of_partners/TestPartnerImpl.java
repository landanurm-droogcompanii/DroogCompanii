package ru.droogcompanii.application.test.data.hierarchy_of_partners;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerImpl;
import ru.droogcompanii.application.test.TestingUtils;

/**
 * Created by ls on 08.01.14.
 */
public class TestPartnerImpl extends TestCase {

    private static final int id = 123;
    private static final String title = "title";
    private static final String fullTitle = "This is full title";
    private static final String discountType = "Скидка";
    private static final int discountSize = 4;
    private static final int categoryId = 456;
    private static final String imageUrl = "this is image url";
    private static final String description = "this is description";
    private static final List<String> webSites = Arrays.asList("this is web-site");
    private static final List<String> emails = Arrays.asList("this is email");

    private PartnerImpl partner;


    @Override
    public void setUp() throws Exception {
        super.setUp();

        partner = createPartnerUsingConstants();
    }

    private static PartnerImpl createPartnerUsingConstants() {
        PartnerImpl initializedByConstants = new PartnerImpl();
        initializedByConstants.id = id;
        initializedByConstants.title = title;
        initializedByConstants.fullTitle = fullTitle;
        initializedByConstants.discountType = discountType;
        initializedByConstants.discountSize = discountSize;
        initializedByConstants.categoryId = categoryId;
        initializedByConstants.imageUrl = imageUrl;
        initializedByConstants.description = description;
        initializedByConstants.webSites = webSites;
        initializedByConstants.emails = emails;
        return initializedByConstants;
    }

    public void testIsSerializable() {
        assertEquals(partner, TestingUtils.serializeAndDeserialize(partner));
    }

    public void testAccessors() {
        assertEquals(id, partner.getId());
        assertEquals(title, partner.getTitle());
        assertEquals(fullTitle, partner.getFullTitle());
        assertEquals(discountType, partner.getDiscountType());
        assertEquals(discountSize, partner.getDiscountSize());
        assertEquals(categoryId, partner.getCategoryId());
        assertEquals(imageUrl, partner.getImageUrl());
        assertEquals(description, partner.getDescription());
        assertEquals(webSites, partner.getWebSites());
        assertEquals(emails, partner.getEmails());
    }

    public void testDefaultConstructor() {
        PartnerImpl def = new PartnerImpl();
        assertTrue(def.getId() == 0);
        assertTrue(def.getCategoryId() == 0);
        assertTrue(def.getTitle().isEmpty());
        assertTrue(def.getFullTitle().isEmpty());
        assertTrue(def.getDescription().isEmpty());
        assertTrue(def.getImageUrl().isEmpty());
        assertTrue(def.getDiscountType().isEmpty());
        assertTrue(def.getDiscountSize() == 0);
        assertTrue(def.getEmails().isEmpty());
        assertTrue(def.getWebSites().isEmpty());
    }


    public void testEquals() {
        assertEquals(createPartnerUsingConstants(), createPartnerUsingConstants());
    }


    public void testEqualsToItself() {
        assertEquals(partner, partner);
    }


    public void testEqualsWithDifferent_CategoryId() {
        PartnerImpl one = createPartnerUsingConstants();
        PartnerImpl two = createPartnerUsingConstants();
        two.categoryId += 1;
        assertEquals(one, two);
    }


    public void testNotEqualsWithDifferent_Id() {
        PartnerImpl one = createPartnerUsingConstants();
        PartnerImpl two = createPartnerUsingConstants();
        two.id += 1;
        assertFalse(one.equals(two));
    }


    public void testNotEqualsWithDifferent_Title() {
        PartnerImpl one = createPartnerUsingConstants();
        PartnerImpl two = createPartnerUsingConstants();
        two.title = two.title + "abc";
        assertFalse(one.equals(two));
    }


    public void testNotEqualsWithDifferent_FullTitle() {
        PartnerImpl one = createPartnerUsingConstants();
        PartnerImpl two = createPartnerUsingConstants();
        two.fullTitle = two.fullTitle + "abc";
        assertFalse(one.equals(two));
    }


    public void testNotEqualsWithDifferent_DiscountType() {
        PartnerImpl one = createPartnerUsingConstants();
        PartnerImpl two = createPartnerUsingConstants();
        two.discountType = two.discountType + "abc";
        assertFalse(one.equals(two));
    }


    public void testNotEqualsWithDifferent_DiscountSize() {
        PartnerImpl one = createPartnerUsingConstants();
        PartnerImpl two = createPartnerUsingConstants();
        two.discountSize += 3;
        assertFalse(one.equals(two));
    }


    public void testNotEqualsWithDifferent_ImageUrl() {
        PartnerImpl one = createPartnerUsingConstants();
        PartnerImpl two = createPartnerUsingConstants();
        two.imageUrl = two.imageUrl + "abc";
        assertFalse(one.equals(two));
    }

    public void testNotEqualsWithDifferent_Description() {
        PartnerImpl one = createPartnerUsingConstants();
        PartnerImpl two = createPartnerUsingConstants();
        two.description = two.description + "abc";
        assertFalse(one.equals(two));
    }

    public void testNotEqualsWithDifferent_WebSites() {
        PartnerImpl one = createPartnerUsingConstants();
        PartnerImpl two = createPartnerUsingConstants();
        two.webSites = new ArrayList<String>(two.webSites);
        two.webSites.add("other web-site");
        assertFalse(one.equals(two));
    }

    public void testNotEqualsWithDifferent_Emails() {
        PartnerImpl one = createPartnerUsingConstants();
        PartnerImpl two = createPartnerUsingConstants();
        two.emails = new ArrayList<String>(two.emails);
        two.emails.add("other email");
        assertFalse(one.equals(two));
    }


    public void testNotEqualsToNull() {
        assertFalse(partner.equals(null));
    }


    public void testEqualsDoesNotThrowExceptionIfSomeOfFieldsIsNull() {
        PartnerImpl one = new PartnerImpl();
        one.title = "one";
        PartnerImpl two = new PartnerImpl();
        two.fullTitle = "full title of two";
        one.equals(two);
    }

    public void testHashCodesAreEqualOfEqualObjects() {
        PartnerImpl copy = createPartnerUsingConstants();
        assertEquals(partner.hashCode(), copy.hashCode());
    }

    public void testHashCodeDoesNotThrowExceptionIfSomeOfFieldsIsNull() {
        new PartnerImpl().hashCode();
    }

    public void testIdAffectsHashCode() {
        PartnerImpl one = createPartnerUsingConstants();
        PartnerImpl two = createPartnerUsingConstants();
        two.id += 1;
        assertTrue(one.hashCode() != two.hashCode());
    }

    public void testToStringReturnsNotNullAndDoesNotThrowExceptionEvenIfSomeOfFieldsIsNull() {
        PartnerImpl partner = new PartnerImpl();
        partner.fullTitle = "Full title";
        String toStringResult = partner.toString();
        assertNotNull(toStringResult);
        assertFalse(toStringResult.isEmpty());
    }
}
