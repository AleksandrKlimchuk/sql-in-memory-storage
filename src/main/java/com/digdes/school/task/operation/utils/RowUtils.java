package com.digdes.school.task.operation.utils;

import com.digdes.school.task.operation.operator.SQLOperator;
import com.digdes.school.task.operation.operator.WhereSQLOperator;
import com.digdes.school.task.storage.RowColumnStorage;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    public static Set<Integer> extractIndicesByConditionsOrAll(
            RowColumnStorage storage, Matcher matcher, int whereGroupNumber
    ) throws Exception {
        final Set<Integer> satisfyingIndices;
        if (Objects.isNull(matcher.group(whereGroupNumber))) {
            satisfyingIndices = IntStream.range(0, storage.selectAllRows().size())
                    .boxed()
                    .collect(Collectors.toSet());
        } else {
            final String whereConditions = matcher.group(whereGroupNumber + 1);
            final SQLOperator whereOperator = new WhereSQLOperator();
            satisfyingIndices = whereOperator.execute(whereConditions, storage);
        }
        return satisfyingIndices;
    }

    public static void updateRowValuesOrRemoveIfNull(Map<String, Object> newValues, Map<String, Object> storageRow) {
        for (Map.Entry<String, Object> newValue : newValues.entrySet()) {
            if (newValue.getValue() == RowUtils.NULL_OBJECT) {
                storageRow.remove(newValue.getKey());
            } else {
                storageRow.put(newValue.getKey(), newValue.getValue());
            }
        }
    }
}
