package com.digdes.school.task.operation;

import com.digdes.school.task.storage.RowColumnStorage;
import com.digdes.school.task.storage.TableColumnsDescriptor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

public class InsertSQLOperation implements SQLOperation {

    private static final String OPERATION_NAME = "insert";
    private static final String NULL_VALUE = "null";

    private final RequestValidator validator = new InsertRequestValidator();

    @Override
    public List<Map<String, Object>> execute(String sqlRequest, RowColumnStorage storage) throws Exception {
        validator.validate(sqlRequest);
        final Map<String, String> columnNameAndValuePairs = extractColumnNameAndValuePairs(sqlRequest);
        // Reallocate new map, maybe columnNameAndValuePairs should be Map<String, Object>;
        final Map<String, Object> rowToInsert = columnNameAndValuePairs.entrySet().stream()
                .filter(pair -> !NULL_VALUE.equalsIgnoreCase(pair.getValue()))
                .map(pair -> createColumnNameAndValueEntry(pair, storage.getColumnsDescriptor()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        if (rowToInsert.isEmpty()) {
            throw new Exception("Empty row can't be inserted. At least 1 pair name and value must be presented");
        }
        storage.insert(rowToInsert);
        return List.of(new HashMap<>(rowToInsert));
    }

    @Override
    public String getName() {
        return OPERATION_NAME;
    }

    private Map<String, String> extractColumnNameAndValuePairs(String sqlRequest) {
        final Matcher pairsMatcher = RequestValidator.COLUMN_NAME_VALUE_PAIR_PATTERN.matcher(sqlRequest);
        final Map<String, String> columnNameAndValuePairs = new HashMap<>();
        while (pairsMatcher.find()) {
            columnNameAndValuePairs.put(pairsMatcher.group(1).toLowerCase(), pairsMatcher.group(2));
        }
        return columnNameAndValuePairs;
    }

    private Map.Entry<String, Object> createColumnNameAndValueEntry(
            Map.Entry<String, String> columnNameAndValuePairToInsert, TableColumnsDescriptor descriptor
    ) {
        final String columnName = columnNameAndValuePairToInsert.getKey();
        final Function<String, Object> converter = descriptor.getConverterByColumnName(columnName);
        final Object columnValue = converter.apply(columnNameAndValuePairToInsert.getValue());
        return Map.entry(columnName, columnValue);
    }
}
