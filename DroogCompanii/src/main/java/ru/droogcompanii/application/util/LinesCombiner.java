package ru.droogcompanii.application.util;

import java.util.List;

/**
 * Created by ls on 23.12.13.
 */
public class LinesCombiner {
    public static String combine(List<String> lines) {
        return combine(lines, "\n");
    }

    public static String combine(List<String> lines, String separator) {
        StringBuilder result = new StringBuilder();
        for (int lineNumber = 0; lineNumber < lines.size(); ++lineNumber) {
            if (lineNumber > 0) {
                result.append(separator);
            }
            result.append(lines.get(lineNumber));
        }
        return result.toString();
    }
}
