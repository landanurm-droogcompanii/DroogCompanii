package ru.droogcompanii.application.test.data.hierarchy_of_partners;

import junit.framework.TestCase;

import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;

/**
 * Created by ls on 08.01.14.
 */
public class TestPartner extends TestCase {

    private static final int id = 123;
    private static final String title = "title";
    private static final String fullTitle = "This is full title";
    private static final String saleType = "Продажа";
    private static final int categoryId = 456;

    private Partner partner;


    @Override
    public void setUp() throws Exception {
        super.setUp();

        partner = new Partner(id, title, fullTitle, saleType, categoryId);
    }


    public void testConstructor() {
        assertEquals(id, partner.id);
        assertEquals(title, partner.title);
        assertEquals(fullTitle, partner.fullTitle);
        assertEquals(saleType, partner.saleType);
        assertEquals(categoryId, partner.categoryId);
    }


    public void testEquals() {
        Object one = new Partner(id, title, fullTitle, saleType, categoryId);
        Object two = new Partner(id, title, fullTitle, saleType, categoryId);
        assertEquals(one, two);
    }


    public void testEqualsToItself() {
        assertEquals(partner, partner);
    }


    public void testEqualsWithDifferentCategoryId() {
        Object one = new Partner(id, title, fullTitle, saleType, categoryId);
        Object two = new Partner(id, title, fullTitle, saleType, categoryId + 1);
        assertEquals(one, two);
    }


    public void testNotEqualsWithDifferent_Id() {
        Object one = new Partner(id, title, fullTitle, saleType, categoryId);
        Object two = new Partner(id + 1, title, fullTitle, saleType, categoryId);
        assertFalse(one.equals(two));
    }


    public void testNotEqualsWithDifferent_Title() {
        Object one = new Partner(id, title, fullTitle, saleType, categoryId);
        Object two = new Partner(id, title + "a", fullTitle, saleType, categoryId);
        assertFalse(one.equals(two));
    }


    public void testNotEqualsWithDifferent_FullTitle() {
        Object one = new Partner(id, title, fullTitle, saleType, categoryId);
        Object two = new Partner(id, title, fullTitle + "a", saleType, categoryId);
        assertFalse(one.equals(two));
    }


    public void testNotEqualsWithDifferent_SaleType() {
        Object one = new Partner(id, title, fullTitle, saleType, categoryId);
        Object two = new Partner(id, title, fullTitle, saleType + "b", categoryId);
        assertFalse(one.equals(two));
    }


    public void testNotEqualsToNull() {
        assertFalse(partner.equals(null));
    }


    public void testHashCodesAreEqual_OfEqualObjects() {
        Partner copy = new Partner(id, title, fullTitle, saleType, categoryId);
        assertEquals(partner.hashCode(), copy.hashCode());
    }


    public void testIdAffectsHashCode() {
        int idOfOne = id;
        int idOfTwo = idOfOne + 1;
        Partner one = new Partner(idOfOne, title, fullTitle, saleType, categoryId);
        Partner two = new Partner(idOfTwo, title, fullTitle, saleType, categoryId);
        assertTrue(one.hashCode() != two.hashCode());
    }
}
