package com.digdes.school.task.operation.operator.conditon;

public class LessOrEqualCondition implements Condition {

    private static final String LESS_OR_EQUALS_CONDITION_OPERATOR = "<=";

    @Override
    public boolean checkCondition(Object currentValue, Object convertedParamValue) {
        if (currentValue instanceof Long) {
            return ((Long) currentValue).compareTo((Long) convertedParamValue) <= 0;
        }
        return ((Double) currentValue).compareTo((Double) convertedParamValue) <= 0;
    }

    @Override
    public String getConditionOperator() {
        return LESS_OR_EQUALS_CONDITION_OPERATOR;
    }
}
