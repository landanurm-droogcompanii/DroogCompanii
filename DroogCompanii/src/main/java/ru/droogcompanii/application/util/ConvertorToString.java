package ru.droogcompanii.application.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leonid on 09.02.14.
 */
public class ConvertorToString {
    private static final String SEPARATOR_BETWEEN_FIELD_NAME_AND_VALUE = "=";
    private static final String SEPARATOR_BETWEEN_FIELDS = ", ";
    private static final String OPEN_FIELDS = "( ";
    private static final String CLOSE_FIELDS = " )";

    private final String className;
    private String[] fieldNames;
    private Object[] fieldValues;

    public static ConvertorToString buildFor(Object someObject) {
        return new ConvertorToString(someObject.getClass().getName());
    }

    private ConvertorToString(String className) {
        this.className = className;
    }

    public ConvertorToString withFieldNames(String... fieldNames) {
        this.fieldNames = fieldNames;
        return this;
    }

    public ConvertorToString withFieldValues(Object... fields) {
        this.fieldValues = fields;
        return this;
    }

    private class Field {
        private final int index;

        public Field(int index) {
            this.index = index;
        }

        public String toString() {
            checkIndex(index);
            return fieldNames[index] + SEPARATOR_BETWEEN_FIELD_NAME_AND_VALUE + fieldValues[index];
        }

        private void checkIndex(int index) {
            if (index < 0 || index >= getCountOfFields()) {
                throw new IllegalArgumentException("Index should be inside a range: " +
                        "[0," + getCountOfFields() + "];  " + "but index was: " + index);
            }
        }
    }

    public String toString() {
        checkState();
        return className
               + OPEN_FIELDS +
                StringsCombiner.combine(getFields(), SEPARATOR_BETWEEN_FIELDS)
               + CLOSE_FIELDS;
    }

    private void checkState() {
        if (fieldNames == null || fieldValues == null || fieldNames.length != fieldValues.length) {
            throw new IllegalStateException("count of fieldNames should be equal to count of fieldValues");
        }
    }

    private List<Field> getFields() {
        List<Field> fields = new ArrayList<Field>(fieldNames.length);
        int countOfFields = getCountOfFields();
        for (int i = 0; i < countOfFields; ++i) {
            fields.add(new Field(i));
        }
        return fields;
    }

    private int getCountOfFields() {
        return fieldNames.length;
    }
}
