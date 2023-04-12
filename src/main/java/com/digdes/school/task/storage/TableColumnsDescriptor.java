package com.digdes.school.task.storage;

import com.digdes.school.task.operation.operator.conditon.Condition;

import java.util.function.Function;

public interface TableColumnsDescriptor {

    Function<String, Object> getConverterByColumnName(String columnName);
    boolean isConditionAvailableForColumn(String columnName, Class<? extends Condition> conditionType);
}
