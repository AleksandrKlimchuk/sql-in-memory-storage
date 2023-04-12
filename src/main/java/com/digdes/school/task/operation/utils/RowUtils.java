package com.digdes.school.task.operation.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

public class RowUtils {

    public static final String NULL_VALUE = "null";
    public static final Object NULL_OBJECT = new Object();

    private RowUtils() {
        throw new UnsupportedOperationException("Utility class can't be instantiated");
    }

    public static Map<String, String> extractColumnNameAndValuePairs(String nameValuePairs) {
        final Matcher pairsMatcher = RegExpUtils.COLUMN_NAME_VALUE_PAIR_PATTERN.matcher(nameValuePairs);
        final Map<String, String> columnNameAndValuePairs = new HashMap<>();
        while (pairsMatcher.find()) {
            columnNameAndValuePairs.put(
                    pairsMatcher.group(RegExpUtils.COLUMN_NAME_GROUP).toLowerCase(),
                    pairsMatcher.group(RegExpUtils.COLUMN_VALUE_GROUP)
            );
        }
        return columnNameAndValuePairs;
    }
}
