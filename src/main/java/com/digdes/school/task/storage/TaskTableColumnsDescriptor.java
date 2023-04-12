package com.digdes.school.task.storage;

import com.digdes.school.task.operation.operator.conditon.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

public class TaskTableColumnsDescriptor implements TableColumnsDescriptor {

    private static final String ID_COLUMN_NAME = "id";
    private static final String LASTNAME_COLUMN_NAME = "lastname";
    private static final String AGE_COLUMN_NAME = "age";
    private static final String COST_COLUMN_NAME = "cost";
    private static final String ACTIVE_COLUMN_NAME = "active";
    private static final String TRUE_VALUE = "true";
    private static final String FALSE_VALUE = "false";

    private record ColumnDescriptor(
            Function<String, Object> converter,
            Set<Class<? extends Condition>> availableConditions
    ) {
    }

    private static final Set<Class<? extends Condition>> NUMERIC_TYPE_AVAILABLE_CONDITIONS = Set.of(
            EqualsCondition.class, NotEqualsCondition.class, LessCondition.class,
            MoreCondition.class, LessOrEqualCondition.class, MoreOrEqualCondition.class
    );
    private static final Set<Class<? extends Condition>> STRING_TYPE_AVAILABLE_CONDITIONS = Set.of(
            EqualsCondition.class, NotEqualsCondition.class, LikeCondition.class, IlikeCondition.class
    );
    private static final Set<Class<? extends Condition>> BOOLEAN_TYPE_AVAILABLE_CONDITIONS = Set.of(
            EqualsCondition.class, NotEqualsCondition.class
    );

    private final Map<String, ColumnDescriptor> columnsDescriptors = new HashMap<>();

    public TaskTableColumnsDescriptor() {
        columnsDescriptors.put(
                ID_COLUMN_NAME, new ColumnDescriptor(this::extractLong, NUMERIC_TYPE_AVAILABLE_CONDITIONS)
        );
        columnsDescriptors.put(
                LASTNAME_COLUMN_NAME, new ColumnDescriptor(this::extractString, STRING_TYPE_AVAILABLE_CONDITIONS)
        );
        columnsDescriptors.put(
                AGE_COLUMN_NAME, new ColumnDescriptor(this::extractLong, NUMERIC_TYPE_AVAILABLE_CONDITIONS)
        );
        columnsDescriptors.put(
                COST_COLUMN_NAME, new ColumnDescriptor(this::extractDouble, NUMERIC_TYPE_AVAILABLE_CONDITIONS)
        );
        columnsDescriptors.put(
                ACTIVE_COLUMN_NAME, new ColumnDescriptor(this::extractBoolean, BOOLEAN_TYPE_AVAILABLE_CONDITIONS)
        );
    }

    @Override
    public Function<String, Object> getConverterByColumnName(String columnName) {
        final ColumnDescriptor columnDescriptor = getColumnDescriptorByColumnName(columnName);
        return columnDescriptor.converter();
    }

    @Override
    public boolean isConditionAvailableForColumn(String columnName, Class<? extends Condition> conditionType) {
        final ColumnDescriptor columnDescriptor = getColumnDescriptorByColumnName(columnName);
        return columnDescriptor.availableConditions().contains(conditionType);
    }

    private ColumnDescriptor getColumnDescriptorByColumnName(String columnName) {
        final ColumnDescriptor columnDescriptor = columnsDescriptors.get(columnName);
        if (Objects.isNull(columnDescriptor)) {
            throw new RuntimeException(String.format("Invalid column name: %s", columnName));
        }
        return columnDescriptor;
    }

    private Long extractLong(String columnValue) {
        try {
            return Long.valueOf(columnValue);
        } catch (Exception e) {
            throw new RuntimeException(
                    String.format("Invalid long value: %s. Long value contains only digits", columnValue), e
            );
        }
    }

    private String extractString(String columnValue) {
        if (checkSingeQuotesWrappingForString(columnValue)) {
            return columnValue.substring(1, columnValue.length() - 1);
        }
        throw new RuntimeException(
                String.format("Invalid string value: %s. String must be formatted: 'value'", columnValue)
        );
    }

    private Double extractDouble(String columnValue) {
        try {
            return Double.valueOf(columnValue);
        } catch (Exception e) {
            throw new RuntimeException(
                    String.format("Invalid double value: %s. Long value must be formatted dddd.dddd", columnValue), e
            );
        }
    }

    private boolean extractBoolean(String columnValue) {
        if (!isValueBoolean(columnValue)) {
            throw new RuntimeException(
                    String.format("Invalid boolean value: %s. Boolean value must be true or false", columnValue)
            );
        }
        return Boolean.parseBoolean(columnValue);
    }

    private boolean checkSingeQuotesWrappingForString(String str) {
        return str.indexOf('\'') == 0 && str.lastIndexOf('\'') == str.length() - 1;
    }

    private boolean isValueBoolean(String testValue) {
        return testValue.equals(TRUE_VALUE) || testValue.equals(FALSE_VALUE);
    }
}
