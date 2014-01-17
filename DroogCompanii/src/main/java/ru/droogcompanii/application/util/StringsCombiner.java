package ru.droogcompanii.application.util;

import java.util.Collection;

/**
 * Created by ls on 23.12.13.
 */
public class StringsCombiner {

    public static String combine(Collection<?> lines) {
        return combine(lines, "\n");
    }

    public static String combine(Collection<?> lines, String separator) {
        return new StringsCombiner(lines, separator).combine();
    }

    private final Collection<?> lines;
    private final String separator;
    private StringBuilder builder;

    private StringsCombiner(Collection<?> lines, String separator) {
        this.lines = lines;
        this.separator = separator;
    }

    private String combine() {
        builder = new StringBuilder();
        boolean firstLine = true;
        for (Object line : lines) {
            if (firstLine) {
                firstLine = false;
            } else {
                builder.append(separator);
            }
            builder.append(line.toString());
        }
        return builder.toString();
    }

}
