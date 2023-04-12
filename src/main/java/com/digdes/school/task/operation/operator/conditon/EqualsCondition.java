package com.digdes.school.task.operation.operator.conditon;

public class EqualsCondition implements Condition {

    private final static String EQUALS_CONDITION_OPERATOR = "=";

    @Override
    public boolean checkCondition(Object currentValue, Object convertedParamValue) {
        return convertedParamValue.equals(currentValue);
    }

    @Override
    public String getConditionOperator() {
        return EQUALS_CONDITION_OPERATOR;
    }
}
