package com.digdes.school.task.operation.operator.conditon;

import java.util.HashMap;
import java.util.Map;

public class RequestConditionFactory implements ConditionFactory {

    private static final Map<String, Condition> conditions = new HashMap<>();

    static {
        putCondition(new EqualsCondition());
        putCondition(new NotEqualsCondition());
        putCondition(new LikeCondition());
        putCondition(new IlikeCondition());
        putCondition(new MoreOrEqualCondition());
        putCondition(new LessOrEqualCondition());
        putCondition(new LessCondition());
        putCondition(new MoreCondition());
    }

    @Override
    public Condition createCondition(String conditionOperator) {
        return conditions.get(conditionOperator);
    }

    private static void putCondition(Condition condition) {
        conditions.put(condition.getConditionOperator(), condition);
    }
}
