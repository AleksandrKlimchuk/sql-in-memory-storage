package com.digdes.school.task.operation.operator.conditon;

import com.digdes.school.task.utils.RegExpUtils;

import java.util.Objects;
import java.util.regex.Pattern;

public class IlikeCondition implements Condition {

    private static final String ILIKE_CONDITION_OPERATOR = "ilike";

    @Override
    public boolean checkCondition(Object currentValue, Object convertedParamValue) {
        final String columnValue = (String) currentValue;
        final String patternExpression = RegExpUtils.shieldExpression((String) convertedParamValue);
        return !Objects.isNull(currentValue) && Pattern.matches(wrapSearchExpression(patternExpression), columnValue);
    }

    @Override
    public String getConditionOperator() {
        return ILIKE_CONDITION_OPERATOR;
    }

    private String wrapSearchExpression(String searchExpression) {
        return "^(?i:" + searchExpression + ")$";
    }


}
