package com.digdes.school.task.operation.update;

import com.digdes.school.task.operation.SQLOperation;
import com.digdes.school.task.operation.operator.SQLOperator;
import com.digdes.school.task.operation.operator.WhereSQLOperator;
import com.digdes.school.task.operation.utils.RegExpUtils;
import com.digdes.school.task.operation.utils.RowUtils;
import com.digdes.school.task.storage.RowColumnStorage;
import com.digdes.school.task.storage.TableColumnsDescriptor;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class UpdateSQLOperation implements SQLOperation {

    private static final String SQL_OPERATION_NAME = "update";

    private record ColumnNameValuePair(
            String columnName, Object columnValue
    ) {
    }

    @Override
    public List<Map<String, Object>> execute(String sqlRequest, RowColumnStorage storage) throws Exception {
        final List<Map<String, Object>> allRows = storage.selectAllRows();
        final Matcher matcher = RegExpUtils.UPDATE_OPERATION_PATTERN.matcher(sqlRequest);
        if (!matcher.matches()) {
            throw new Exception("Update request has invalid structure");
        }
        final String updateNewValuesString = matcher.group(RegExpUtils.UPDATE_OPERATION_VALUES_GROUP);
        final Map<String, Object> updateNewValues = createRowToUpdate(
                updateNewValuesString, storage.getColumnsDescriptor()
        );
        final Set<Integer> indicesOfRowsToUpdate;
        if (Objects.isNull(matcher.group(RegExpUtils.UPDATE_OPERATION_WHERE_GROUP))) {
            indicesOfRowsToUpdate = IntStream.range(0, allRows.size()).boxed().collect(Collectors.toSet());
        } else {
            final String whereConditions = matcher.group(RegExpUtils.UPDATE_OPERATION_WHERE_CONDITIONS_GROUP);
            final SQLOperator whereOperator = new WhereSQLOperator();
            indicesOfRowsToUpdate = whereOperator.execute(whereConditions, storage);
        }
        final List<Map<String, Object>> updatedRows = new ArrayList<>();
        for (Integer indexOfRowToUpdate : indicesOfRowsToUpdate) {
            final Map<String, Object> updatedRow = new HashMap<>(allRows.get(indexOfRowToUpdate));
            for (Map.Entry<String, Object> newColumnValue : updateNewValues.entrySet()) {
                if (newColumnValue.getValue() == RowUtils.NULL_OBJECT) {
                    updatedRow.remove(newColumnValue.getKey());
                } else {
                    updatedRow.put(newColumnValue.getKey(), newColumnValue.getValue());
                }
            }
            if (updatedRow.values().stream().allMatch(Objects::isNull)) {
                throw new Exception("Update request will make empty row");
            }
            updatedRows.add(updatedRow);
        }
        storage.updateValuesByIndices(indicesOfRowsToUpdate, updateNewValues);
        return updatedRows;
    }

    @Override
    public String getName() {
        return SQL_OPERATION_NAME;
    }

    private Map<String, Object> createRowToUpdate (
            String pairs, TableColumnsDescriptor columnsDescriptor
    ) {
        final Map<String, String> columnNameAndValuePairs = RowUtils.extractColumnNameAndValuePairs(pairs);
        return  columnNameAndValuePairs.entrySet().stream()
                .map(pair -> createColumnNameAndValueEntry(pair, columnsDescriptor))
                .collect(Collectors.toMap(ColumnNameValuePair::columnName, ColumnNameValuePair::columnValue));
    }

    private ColumnNameValuePair createColumnNameAndValueEntry(
            Map.Entry<String, String> columnNameAndValuePairToInsert, TableColumnsDescriptor descriptor
    ) {
        final String columnName = columnNameAndValuePairToInsert.getKey();
        final String columnValue = columnNameAndValuePairToInsert.getValue();
        if (columnValue.equalsIgnoreCase(RowUtils.NULL_VALUE)) {
            return new ColumnNameValuePair(columnName, RowUtils.NULL_OBJECT);
        }
        final Function<String, Object> converter = descriptor.getConverterByColumnName(columnName);
        final Object convertedColumnValue = converter.apply(columnValue);
        return new ColumnNameValuePair(columnName, convertedColumnValue);
    }
}
