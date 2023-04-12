package com.digdes.school.task.operation.operator.conditon;

import com.digdes.school.task.utils.RegExpUtils;

import java.util.Objects;
import java.util.regex.Pattern;

public class LikeCondition implements Condition {

    private static final String LIKE_CONDITION_OPERATOR = "like";

    @Override
    public boolean checkCondition(Object currentValue, Object convertedParamValue) {
        final String columnValue = (String) currentValue;
        final String patternExpression = RegExpUtils.shieldExpression((String) convertedParamValue);
        return !Objects.isNull(currentValue) && Pattern.matches(wrapSearchExpression(patternExpression), columnValue);
    }

    @Override
    public String getConditionOperator() {
        return LIKE_CONDITION_OPERATOR;
    }

    private String wrapSearchExpression(String searchExpression) {
        return "^" + searchExpression + "$";
    }
}
