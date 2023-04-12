package com.digdes.school.task.operation.insert;

import com.digdes.school.task.operation.RequestValidator;
import com.digdes.school.task.operation.SQLOperation;
import com.digdes.school.task.operation.utils.RowUtils;
import com.digdes.school.task.storage.RowColumnStorage;
import com.digdes.school.task.storage.TableColumnsDescriptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class InsertSQLOperation implements SQLOperation {

    private static final String OPERATION_NAME = "insert";

    private final RequestValidator validator = new InsertRequestValidator();

    @Override
    public List<Map<String, Object>> execute(String sqlRequest, RowColumnStorage storage) throws Exception {
        validator.validate(sqlRequest);
        final Map<String, String> columnNameAndValuePairs = RowUtils.extractColumnNameAndValuePairs(sqlRequest);
        // Reallocate new map, maybe columnNameAndValuePairs should be Map<String, Object>;
        Map<String, Object> rowToInsert;
        try {
            rowToInsert = columnNameAndValuePairs.entrySet().stream()
                    .filter(pair -> !RowUtils.NULL_VALUE.equalsIgnoreCase(pair.getValue()))
                    .map(pair -> createColumnNameAndValueEntry(pair, storage.getColumnsDescriptor()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        } catch (Exception e) {
            throw new Exception(e);
        }
        if (rowToInsert.isEmpty()) {
            throw new Exception("Empty row can't be inserted. At least 1 pair name and value must be presented");
        }
        storage.insert(rowToInsert);
        List<Map<String, Object>> insertedRow = new ArrayList<>();
        insertedRow.add(rowToInsert);
        return insertedRow;
    }

    @Override
    public String getName() {
        return OPERATION_NAME;
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
