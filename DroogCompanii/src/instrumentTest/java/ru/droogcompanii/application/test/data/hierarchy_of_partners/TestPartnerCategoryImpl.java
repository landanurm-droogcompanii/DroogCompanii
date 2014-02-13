package ru.droogcompanii.application.test.data.hierarchy_of_partners;

import junit.framework.TestCase;

import java.io.Serializable;

import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerCategory;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerCategoryImpl;
import ru.droogcompanii.application.test.TestingUtils;

/**
 * Created by ls on 08.01.14.
 */
public class TestPartnerCategoryImpl extends TestCase {

    private static final int id = 12;
    private static final String title = "Category Title";

    private PartnerCategoryImpl partnerCategory;


    @Override
    public void setUp() throws Exception {
        super.setUp();

        partnerCategory = createPartnerCategoryBy(id, title);
    }


    public void testIsSerializable() {
        assertEquals(partnerCategory, TestingUtils.serializeAndDeserialize((Serializable) partnerCategory));
    }


    public void testAccessors() {
        assertEquals(id, partnerCategory.getId());
        assertEquals(title, partnerCategory.getTitle());
    }


    public void testDefaultConstructor() {
        PartnerCategoryImpl def = new PartnerCategoryImpl();
        assertTrue(def.getId() == 0);
        assertTrue(def.getTitle().isEmpty());
    }


    public void testEquals() {
        PartnerCategory one = createPartnerCategoryBy(id, title);
        PartnerCategory two = createPartnerCategoryBy(id, title);
        assertEquals(one, two);
    }

    private static PartnerCategoryImpl createPartnerCategoryBy(int id, String title) {
        PartnerCategoryImpl category = new PartnerCategoryImpl();
        category.id = id;
        category.title = title;
        return category;
    }


    public void testEqualsToItself() {
        assertEquals(partnerCategory, partnerCategory);
    }


    public void testNotEqualsWithDifferent_Id() {
        Object one = createPartnerCategoryBy(id, title);
        Object two = createPartnerCategoryBy(id + 1, title);
        assertFalse(one.equals(two));
    }


    public void testNotEqualsWithDifferent_Title() {
        Object one = createPartnerCategoryBy(id, title);
        Object two = createPartnerCategoryBy(id, title + "a");
        assertFalse(one.equals(two));
    }


    public void testNotEqualsToNull() {
        assertFalse(partnerCategory.equals(null));
    }


    public void testEqualsDoesNotThrowExceptionIfSomeOfFieldsIsNull() {
        PartnerCategory one = createPartnerCategoryBy(0, null);
        PartnerCategory two = createPartnerCategoryBy(0, "not null");
        one.equals(two);
        two.equals(one);
    }


    public void testHashCodesAreEqual_OfEqualObjects() {
        Object one = createPartnerCategoryBy(id, title);
        Object two = createPartnerCategoryBy(id, title);
        assertEquals(one.hashCode(), two.hashCode());
    }

    public void testHashCodeDoesNotThrowExceptionIfSomeOfFieldsIsNull() {
        createPartnerCategoryBy(0, null).hashCode();
    }

    public void testToStringDoesNotThrowExceptionIfSomeOfFieldsIsNull() {
        String toStringResult = createPartnerCategoryBy(0, null).toString();
        assertNotNull(toStringResult);
    }
}
