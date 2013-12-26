package ru.droogcompanii.application.util;

import java.util.List;

/**
 * Created by ls on 23.12.13.
 */
public class LinesCombiner {
    public static String combine(List<String> lines) {
        StringBuilder result = new StringBuilder();
        for (int lineNumber = 0; lineNumber < lines.size(); ++lineNumber) {
            if (lineNumber > 0) {
                result.append("\n");
            }
            result.append(lines.get(lineNumber));
        }
        return result.toString();
    }
}
