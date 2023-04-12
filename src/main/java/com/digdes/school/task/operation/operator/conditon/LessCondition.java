package com.digdes.school.task.operation.operator.conditon;

public class LessCondition implements Condition {

    private static final String LESS_CONDITION_OPERATOR = "<";

    @Override
    public String getConditionOperator() {
        return LESS_CONDITION_OPERATOR;
    }

    @Override
    public boolean checkCondition(Object columnValue, Object paramValue) {
        if (columnValue instanceof Long) {
            return ((Long) columnValue).compareTo((Long) paramValue) < 0;
        }
        return ((Double) columnValue).compareTo((Double) paramValue) < 0;
    }
}
