package ru.droogcompanii.application.data.data_structure;

import junit.framework.TestCase;

/**
 * Created by ls on 08.01.14.
 */
public class TestPartnerCategory extends TestCase {

    private static final int id = 12;
    private static final String title = "Category Title";

    private PartnerCategory partnerCategory;


    @Override
    public void setUp() throws Exception {
        super.setUp();

        partnerCategory = new PartnerCategory(id, title);
    }


    public void testConstructor() {
        assertEquals(id, partnerCategory.id);
        assertEquals(title, partnerCategory.title);
    }


    public void testEquals() {
        PartnerCategory one = new PartnerCategory(id, title);
        PartnerCategory two = new PartnerCategory(id, title);
        assertEquals(one, two);
    }


    public void testEqualsToItself() {
        assertEquals(partnerCategory, partnerCategory);
    }


    public void testNotEqualsWithDifferent_Id() {
        Object one = new PartnerCategory(id, title);
        Object two = new PartnerCategory(id + 1, title);
        assertFalse(one.equals(two));
    }


    public void testNotEqualsWithDifferent_Title() {
        Object one = new PartnerCategory(id, title);
        Object two = new PartnerCategory(id, title + "a");
        assertFalse(one.equals(two));
    }


    public void testNotEqualsToNull() {
        assertFalse(partnerCategory.equals(null));
    }


    public void testHashCodesAreEqual_OfEqualObjects() {
        Object one = new PartnerCategory(id, title);
        Object two = new PartnerCategory(id, title);
        assertEquals(one.hashCode(), two.hashCode());
    }
}
