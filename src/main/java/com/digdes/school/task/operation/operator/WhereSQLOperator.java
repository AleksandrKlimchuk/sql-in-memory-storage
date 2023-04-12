package com.digdes.school.task.operation.operator;

import com.digdes.school.task.operation.RequestValidator;
import com.digdes.school.task.operation.operator.conditon.Condition;
import com.digdes.school.task.operation.operator.conditon.ConditionFactory;
import com.digdes.school.task.operation.operator.conditon.RequestConditionFactory;
import com.digdes.school.task.storage.RowColumnStorage;
import com.digdes.school.task.operation.utils.RegExpUtils;

import java.util.*;
import java.util.function.BiFunction;
import java.util.regex.Matcher;

public class WhereSQLOperator implements SQLOperator {

    static final char CONDITION_REPLACE_SYMBOL = '_';
    static final char AND_REPLACE_SYMBOL = '&';
    static final char OR_REPLACE_SYMBOL = '|';
    static final char OPENING_PARENTHESES = '(';
    static final char CLOSING_PARENTHESES = ')';

    private static final String AND = "and";
    private static final String OR = "or";

    private record ConditionParams(
            String columnName, String operator, String paramValue
    ) {
    }

    private final Map<Character, BiFunction<Set<Integer>, Set<Integer>, Set<Integer>>> combiners = Map.of(
            AND_REPLACE_SYMBOL, (indices1, indices2) -> {
                indices1.retainAll(indices2);
                return indices1;
            },
            OR_REPLACE_SYMBOL, (indices1, indices2) -> {
                indices1.addAll(indices2);
                return indices1;
            }
    );

    private final RequestValidator validator = new WhereOperatorValidator();
    private final ConditionFactory conditionFactory = new RequestConditionFactory();


    @Override
    public Set<Integer> execute(String conditions, RowColumnStorage storage) throws Exception {
        final Queue<ConditionParams> extractedConditions = extractConditions(conditions);
        final String clearedConditionsString = replaceAllConditionsBySpecialSymbol(conditions);
        validator.validate(clearedConditionsString);
        return combineConditionsByRPN(clearedConditionsString, storage, extractedConditions);
    }

    private Set<Integer> combineConditionsByRPN(
            String clearedConditionsString, RowColumnStorage storage, Queue<ConditionParams> extractedConditions
    ) throws Exception {
        final Deque<Character> combinersStack = new ArrayDeque<>();
        final Deque<Set<Integer>> valuesStack = new ArrayDeque<>();
        final char[] conditionsSymbols = clearedConditionsString.toCharArray();
        for (char symbol : conditionsSymbols) {
            switch (symbol) {
                case OPENING_PARENTHESES -> combinersStack.push(OPENING_PARENTHESES);
                case CONDITION_REPLACE_SYMBOL ->
                        valuesStack.push(computeConditionExpression(extractedConditions.remove(), storage));
                case AND_REPLACE_SYMBOL -> {
                    combineUntilOpeningParenthesis(combinersStack, valuesStack);
                    combinersStack.push(AND_REPLACE_SYMBOL);
                }
                case OR_REPLACE_SYMBOL -> {
                    combineUntilOpeningParenthesis(combinersStack, valuesStack);
                    combinersStack.push(OR_REPLACE_SYMBOL);
                }
                case CLOSING_PARENTHESES -> {
                    combineUntilOpeningParenthesis(combinersStack, valuesStack);
                    combinersStack.pop();
                }
            }
        }
        while (!combinersStack.isEmpty()) {
            combineNext(combinersStack, valuesStack);
        }
        return valuesStack.pop();
    }

    private void combineUntilOpeningParenthesis(
            Deque<Character> combinersStack, Deque<Set<Integer>> valuesStack
    ) {
        while (!combinersStack.isEmpty() && combinersStack.peek() != OPENING_PARENTHESES) {
            combineNext(combinersStack, valuesStack);
        }
    }

    private void combineNext(Deque<Character> combinersStack, Deque<Set<Integer>> valuesStack) {
        final BiFunction<Set<Integer>, Set<Integer>, Set<Integer>> combiner = combiners.get(combinersStack.pop());
        final Set<Integer> indices2 = valuesStack.pop();
        final Set<Integer> indices1 = valuesStack.pop();
        final Set<Integer> combineIndicesResult = combiner.apply(indices1, indices2);
        valuesStack.push(combineIndicesResult);
    }


    private Set<Integer> computeConditionExpression(
            ConditionParams conditionParams, RowColumnStorage storage
    ) throws Exception {
        final Condition conditionExecutor = conditionFactory.createCondition(conditionParams.operator());
        return conditionExecutor.execute(storage, conditionParams.columnName(), conditionParams.paramValue());
    }

    private String replaceAllConditionsBySpecialSymbol(String conditions) {
        String clearedConditions = conditions.replaceAll(
                RegExpUtils.COLUMN_NAME_VALUE_PAIR_CONDITION_EXPRESSION, String.valueOf(CONDITION_REPLACE_SYMBOL)
        );
        clearedConditions = clearedConditions.replaceAll("(?i:" + AND + ")", String.valueOf(AND_REPLACE_SYMBOL));
        clearedConditions = clearedConditions.replaceAll("(?i:" + OR + ")", String.valueOf(OR_REPLACE_SYMBOL));
        clearedConditions = clearedConditions.replaceAll("\\s", "");
        return clearedConditions;
    }

    private Queue<ConditionParams> extractConditions(String conditions) {
        final Matcher conditionsMatcher = RegExpUtils.COLUMN_NAME_VALUE_PAIR_OF_CONDITION_PATTERN.matcher(conditions);
        final Queue<ConditionParams> extractedConditions = new ArrayDeque<>();
        while (conditionsMatcher.find()) {
            extractedConditions.add(new ConditionParams(
                    conditionsMatcher.group(RegExpUtils.CONDITION_COLUMN_NAME_GROUP).toLowerCase(),
                    conditionsMatcher.group(RegExpUtils.CONDITION_OPERATOR_GROUP),
                    conditionsMatcher.group(RegExpUtils.CONDITION_COLUMN_VALUE_GROUP)
            ));
        }
        return extractedConditions;
    }
}
