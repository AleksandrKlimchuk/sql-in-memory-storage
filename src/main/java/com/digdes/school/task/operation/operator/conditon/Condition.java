package com.digdes.school.task.operation.operator.conditon;

import com.digdes.school.task.storage.RowColumnStorage;
import com.digdes.school.task.storage.TableColumnsDescriptor;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

public interface Condition {

    default Set<Integer> execute(RowColumnStorage table, String columnName, String paramValue) throws Exception {
        final TableColumnsDescriptor columnsDescriptor = table.getColumnsDescriptor();
        if (!columnsDescriptor.isConditionAvailableForColumn(columnName, this.getClass())) {
            throw new Exception(String.format(
                    "Invalid condition. %s column does not support %s operation", columnName, getConditionOperator()
            ));
        }
        final List<Object> columnValues = table.selectValuesByColumnName(columnName);
        final Function<String, Object> converter = columnsDescriptor.getConverterByColumnName(columnName);
        final Object convertedParamValue = converter.apply(paramValue);
        final Set<Integer> matchedRowIndices = new HashSet<>();
        for (int i = 0; i < columnValues.size(); i++) {
            final Object currentValue = columnValues.get(i);
            if (Objects.isNull(currentValue) && isNullUnavailable()) {
                continue;
            }
            if (checkCondition(currentValue, convertedParamValue)) {
                matchedRowIndices.add(i);
            }
        }
        return matchedRowIndices;
    }

    default boolean isNullUnavailable() {
        return true;
    }

    boolean checkCondition(Object currentValue, Object convertedParamValue);

    String getConditionOperator();
}
