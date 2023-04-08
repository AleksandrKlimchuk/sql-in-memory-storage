package com.digdes.school.task.storage;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public class TaskTableColumnsDescriptor implements TableColumnsDescriptor {

    private static final String ID_COLUMN_NAME = "id";
    private static final String LASTNAME_COLUMN_NAME= "lastname";
    private static final String AGE_COLUMN_NAME = "age";
    private static final String COST_COLUMN_NAME = "cost";
    private static final String ACTIVE_COLUMN_NAME = "active";
    private static final String TRUE_VALUE = "true";
    private static final String FALSE_VALUE = "false";

    private final Map<String, Function<String, Object>> converters = new HashMap<>();

    public TaskTableColumnsDescriptor(){
        converters.put(ID_COLUMN_NAME, Long::valueOf);
        converters.put(LASTNAME_COLUMN_NAME, this::extractString);
        converters.put(AGE_COLUMN_NAME, Long::valueOf);
        converters.put(COST_COLUMN_NAME, Double::valueOf);
        converters.put(ACTIVE_COLUMN_NAME, this::extractBoolean);
    }

    @Override
    public Function<String, Object> getConverterByColumnName(String columnName) {
        final Function<String, Object> converter = converters.get(columnName);
        if (Objects.isNull(converter)) {
            throw new RuntimeException(String.format("Invalid column name: %s", columnName));
        }
        return converter;
    }

    private String extractString(String columnValue) {
        if (checkSingeQuotesWrappingForString(columnValue)) {
            return columnValue.substring(1, columnValue.length() - 1);
        }
        throw new RuntimeException(
                String.format("Invalid string value: %s. String must be formatted: 'value'", columnValue)
        );
    }

    private boolean checkSingeQuotesWrappingForString(String str) {
        return str.indexOf('\'') == 0 && str.lastIndexOf('\'') == str.length() - 1;
    }

    private boolean extractBoolean(String columnValue) {
        if (TRUE_VALUE.equals(columnValue)) {
            return true;
        } else if (FALSE_VALUE.equals(columnValue)) {
            return false;
        } else {
            throw new RuntimeException(
                    String.format("Invalid boolean value: %s. Boolean value must be true or false", columnValue)
            );
        }
    }
}
