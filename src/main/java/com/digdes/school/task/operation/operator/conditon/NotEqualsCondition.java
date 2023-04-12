package com.digdes.school.task.operation.operator.conditon;

public class NotEqualsCondition implements Condition {

    private static final String NOT_EQUALS_CONDITION_OPERATOR = "!=";

    @Override
    public boolean checkCondition(Object currentValue, Object convertedParamValue) {
        return !convertedParamValue.equals(currentValue);
    }

    @Override
    public boolean isNullUnavailable() {
        return false;
    }

    @Override
    public String getConditionOperator() {
        return NOT_EQUALS_CONDITION_OPERATOR;
    }
}
