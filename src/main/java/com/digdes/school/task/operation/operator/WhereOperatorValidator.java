package com.digdes.school.task.operation.operator;

import com.digdes.school.task.operation.RequestValidator;

import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class WhereOperatorValidator implements RequestValidator {

    private static final Pattern CLEARED_CONDITIONS_PATTERN = Pattern.compile(
            "^[" + WhereSQLOperator.CONDITION_REPLACE_SYMBOL + WhereSQLOperator.AND_REPLACE_SYMBOL + WhereSQLOperator.OR_REPLACE_SYMBOL +
                    WhereSQLOperator.OPENING_PARENTHESES + WhereSQLOperator.CLOSING_PARENTHESES + "]+$"
    );

    @FunctionalInterface
    private interface Action {
        void execute();
    }

    private record Validator(
            Predicate<Character> isNextSymbolAvailable,
            String nextSymbolUnavailableErrorMessage,
            Action action
    ) {
    }

    private int parenthesesNestingCount = 0;
    private int conditionsCount = 0;
    private int combinersCount = 0;

    private final Map<Character, Validator> validatorMap = Map.of(
            WhereSQLOperator.OPENING_PARENTHESES, new Validator(
                    nextSymbol -> nextSymbol == WhereSQLOperator.CONDITION_REPLACE_SYMBOL || nextSymbol == WhereSQLOperator.OPENING_PARENTHESES,
                    "Unexpected character after opening parentheses",
                    () -> parenthesesNestingCount++
            ),
            WhereSQLOperator.CLOSING_PARENTHESES, new Validator(
                    nextSymbol -> nextSymbol == WhereSQLOperator.AND_REPLACE_SYMBOL ||
                            nextSymbol == WhereSQLOperator.OR_REPLACE_SYMBOL || nextSymbol == WhereSQLOperator.CLOSING_PARENTHESES,
                    "Unexpected character after closing parentheses",
                    () -> parenthesesNestingCount--
            ),
            WhereSQLOperator.AND_REPLACE_SYMBOL, new Validator(
                    nextSymbol -> nextSymbol == WhereSQLOperator.CONDITION_REPLACE_SYMBOL || nextSymbol == WhereSQLOperator.OPENING_PARENTHESES,
                    "Unexpected symbol after AND",
                    () -> combinersCount++
            ),
            WhereSQLOperator.OR_REPLACE_SYMBOL, new Validator(
                    nextSymbol -> nextSymbol == WhereSQLOperator.CONDITION_REPLACE_SYMBOL || nextSymbol == WhereSQLOperator.OPENING_PARENTHESES,
                    "Unexpected symbol after OR",
                    () -> combinersCount++
            ),
            WhereSQLOperator.CONDITION_REPLACE_SYMBOL, new Validator(
                    nextSymbol -> nextSymbol == WhereSQLOperator.AND_REPLACE_SYMBOL
                            || nextSymbol == WhereSQLOperator.OR_REPLACE_SYMBOL || nextSymbol == WhereSQLOperator.CLOSING_PARENTHESES,
                    "Unexpected symbol after condition",
                    () -> conditionsCount++
            )
    );

    @Override
    public void validate(String clearedConditions) throws Exception {
        reset();
        if (clearedConditions.isEmpty()) {
            throw new Exception("After 'WHERE' should be at least 1 condition");
        }
        if (!CLEARED_CONDITIONS_PATTERN.matcher(clearedConditions).matches()) {
            throw new Exception("Conditions has invalid structure");
        }
        final char[] conditionsSymbols = clearedConditions.toCharArray();
        if (conditionsSymbols[0] == WhereSQLOperator.CLOSING_PARENTHESES ||
                conditionsSymbols[0] == WhereSQLOperator.AND_REPLACE_SYMBOL ||
                conditionsSymbols[0] == WhereSQLOperator.OR_REPLACE_SYMBOL
        ) {
            throw new Exception("Unexpected symbols after 'WHERE'. " +
                    "Conditions should starts with condition description or ("
            );
        }
        Validator validator = validatorMap.get(conditionsSymbols[0]);
        for (int i = 0; i < conditionsSymbols.length - 1; i++) {
            char nextSymbol = conditionsSymbols[i + 1];
            if (!validator.isNextSymbolAvailable.test(nextSymbol)) {
                throw new Exception(validator.nextSymbolUnavailableErrorMessage);
            }
            validator.action.execute();
            validator = validatorMap.get(nextSymbol);
        }
        validator.action.execute();
        if (conditionsCount - 1 != combinersCount) {
            throw new Exception("Invalid structure of conditions. " +
                    "Each condition must be connected to the following by AND or OR"
            );
        }
        if (parenthesesNestingCount != 0) {
            throw new Exception("Invalid structure of conditions. Fix count of parentheses");
        }
    }

    private void reset() {
        conditionsCount = 0;
        combinersCount = 0;
        parenthesesNestingCount = 0;
    }
}
