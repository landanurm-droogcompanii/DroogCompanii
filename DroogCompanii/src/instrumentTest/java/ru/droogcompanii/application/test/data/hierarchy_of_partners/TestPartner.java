package ru.droogcompanii.application.test.data.hierarchy_of_partners;

import junit.framework.TestCase;

import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.test.TestingUtils;

/**
 * Created by ls on 08.01.14.
 */
public class TestPartner extends TestCase {

    private static final int id = 123;
    private static final String title = "title";
    private static final String fullTitle = "This is full title";
    private static final String discountType = "Скидка";
    private static final int discount = 4;
    private static final int categoryId = 456;

    private Partner partner;


    @Override
    public void setUp() throws Exception {
        super.setUp();

        partner = new Partner(id, title, fullTitle, discountType, discount, categoryId);
    }

    public void testIsSerializable() {
        assertEquals(partner, TestingUtils.serializeAndDeserialize(partner));
    }

    public void testConstructor() {
        assertEquals(id, partner.id);
        assertEquals(title, partner.title);
        assertEquals(fullTitle, partner.fullTitle);
        assertEquals(discountType, partner.discountType);
        assertEquals(discount, partner.discount);
        assertEquals(categoryId, partner.categoryId);
    }

    public void testEquals() {
        Object one = new Partner(id, title, fullTitle, discountType, discount, categoryId);
        Object two = new Partner(id, title, fullTitle, discountType, discount, categoryId);
        assertEquals(one, two);
    }


    public void testEqualsToItself() {
        assertEquals(partner, partner);
    }


    public void testEqualsWithDifferent_CategoryId() {
        Object one = new Partner(id, title, fullTitle, discountType, discount, categoryId);
        Object two = new Partner(id, title, fullTitle, discountType, discount, categoryId + 1);
        assertEquals(one, two);
    }


    public void testNotEqualsWithDifferent_Id() {
        Object one = new Partner(id, title, fullTitle, discountType, discount, categoryId);
        Object two = new Partner(id + 1, title, fullTitle, discountType, discount, categoryId);
        assertFalse(one.equals(two));
    }


    public void testNotEqualsWithDifferent_Title() {
        Object one = new Partner(id, title, fullTitle, discountType, discount, categoryId);
        Object two = new Partner(id, title + "a", fullTitle, discountType, discount, categoryId);
        assertFalse(one.equals(two));
    }


    public void testNotEqualsWithDifferent_FullTitle() {
        Object one = new Partner(id, title, fullTitle, discountType, discount, categoryId);
        Object two = new Partner(id, title, fullTitle + "a", discountType, discount, categoryId);
        assertFalse(one.equals(two));
    }


    public void testNotEqualsWithDifferent_DiscountType() {
        Object one = new Partner(id, title, fullTitle, discountType, discount, categoryId);
        Object two = new Partner(id, title, fullTitle, discountType + "b", discount, categoryId);
        assertFalse(one.equals(two));
    }


    public void testNotEqualsWithDifferent_Discount() {
        Object one = new Partner(id, title, fullTitle, discountType, discount, categoryId);
        Object two = new Partner(id, title, fullTitle, discountType, discount + 1, categoryId);
        assertFalse(one.equals(two));
    }


    public void testNotEqualsToNull() {
        assertFalse(partner.equals(null));
    }


    public void testHashCodesAreEqualOfEqualObjects() {
        Partner copy = new Partner(id, title, fullTitle, discountType, discount, categoryId);
        assertEquals(partner.hashCode(), copy.hashCode());
    }


    public void testIdAffectsHashCode() {
        int idOfOne = id;
        int idOfTwo = idOfOne + 1;
        Partner one = new Partner(idOfOne, title, fullTitle, discountType, discount, categoryId);
        Partner two = new Partner(idOfTwo, title, fullTitle, discountType, discount, categoryId);
        assertTrue(one.hashCode() != two.hashCode());
    }
}
