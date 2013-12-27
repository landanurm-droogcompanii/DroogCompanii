package ru.droogcompanii.application.util;

import java.util.List;

/**
 * Created by ls on 23.12.13.
 */
public class StringsCombiner {

    public static String combine(List<String> lines) {
        return combine(lines, "\n");
    }

    public static String combine(List<String> lines, String separator) {
        return new StringsCombiner(lines, separator).combine();
    }

    private final List<String> lines;
    private final String separator;
    private int lineNumber;
    private StringBuilder builder;

    private StringsCombiner(List<String> lines, String separator) {
        this.lines = lines;
        this.separator = separator;
    }

    private String combine() {
        builder = new StringBuilder();
        for (lineNumber = 0; lineNumber < lines.size(); ++lineNumber) {
            appendSeparatorIfNotFirstLine();
            appendLine();
        }
        return builder.toString();
    }

    private void appendSeparatorIfNotFirstLine() {
        if (lineNumber > 0) {
            builder.append(separator);
        }
    }

    private void appendLine() {
        String line = lines.get(lineNumber);
        builder.append(line);
    }

}
