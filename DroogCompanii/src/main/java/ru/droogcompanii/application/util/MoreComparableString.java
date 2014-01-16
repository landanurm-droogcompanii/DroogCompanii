package ru.droogcompanii.application.util;

/**
 * Created by ls on 16.01.14.
 */
public class MoreComparableString {
    private final String str;

    public MoreComparableString(String str) {
        this.str = str;
    }

    public boolean containsIgnoreCase(String other) {
        return str.toLowerCase().contains(other.toLowerCase());
    }
}
