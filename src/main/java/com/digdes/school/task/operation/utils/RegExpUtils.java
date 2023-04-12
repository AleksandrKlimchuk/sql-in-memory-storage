package com.digdes.school.task.operation.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class RegExpUtils {

    private RegExpUtils() {
        throw new UnsupportedOperationException("Utility class can't be instantiated");
    }

    public static final String COLUMN_NAME_EXPRESSION = "'([^']+)'";
    public static final String COLUMN_VALUE_EXPRESSION = "('[^']*'|[^',=()\\s]*)";

    public static final String COLUMN_NAME_VALUE_PAIR_EXPRESSION = COLUMN_NAME_EXPRESSION +
            "\\s*=\\s*" + COLUMN_VALUE_EXPRESSION;
    public static final Pattern COLUMN_NAME_VALUE_PAIR_PATTERN = Pattern.compile(
            COLUMN_NAME_VALUE_PAIR_EXPRESSION
    );
    public static final int COLUMN_NAME_GROUP = 1;
    public static final int COLUMN_VALUE_GROUP = 2;
    public static final String INSERT_OPERATION_EXPRESSION = "^\\s*(?i:insert)\\s+(?i:values)\\s+" +
            "(\\s*" + COLUMN_NAME_VALUE_PAIR_EXPRESSION + "\\s*,)*" +
            "(\\s*" + COLUMN_NAME_VALUE_PAIR_EXPRESSION + "\\s*)$";
    public static final Pattern INSERT_OPERATION_PATTERN = Pattern.compile(INSERT_OPERATION_EXPRESSION);

    public static final String COLUMN_NAME_VALUE_PAIR_CONDITION_EXPRESSION = COLUMN_NAME_EXPRESSION +
            "\\s*(=|!=|<=|<|>=|>|like|ilike)\\s*" + COLUMN_VALUE_EXPRESSION;
    public static Pattern COLUMN_NAME_VALUE_PAIR_OF_CONDITION_PATTERN = Pattern.compile(
            COLUMN_NAME_VALUE_PAIR_CONDITION_EXPRESSION
    );
    public static final int CONDITION_COLUMN_NAME_GROUP = 1;
    public static final int CONDITION_OPERATOR_GROUP = 2;
    public static final int CONDITION_COLUMN_VALUE_GROUP = 3;

    public static final String WHERE_OPERATOR_EXPRESSION = "(\\s+(?i:where)\\s+(.+))?";
    public static final String SELECT_OPERATION_EXPRESSION = "^\\s*(?i:select)" + WHERE_OPERATOR_EXPRESSION + "$";
    public static final int SELECT_OPERATION_WHERE_GROUP = 1;
    public static final Pattern SELECT_OPERATION_PATTERN = Pattern.compile(SELECT_OPERATION_EXPRESSION);

    public static final String UPDATE_OPERATION_EXPRESSION = "^\\s*(?i:update)\\s+(?i:values)\\s+" +
            "((\\s*" + COLUMN_NAME_VALUE_PAIR_EXPRESSION + "\\s*,)*" +
            "(\\s*" + COLUMN_NAME_VALUE_PAIR_EXPRESSION + "\\s*))" + WHERE_OPERATOR_EXPRESSION + "$";
    public static final int UPDATE_OPERATION_VALUES_GROUP = 1;
    public static final int UPDATE_OPERATION_WHERE_GROUP = 8;
    public static final Pattern UPDATE_OPERATION_PATTERN = Pattern.compile(UPDATE_OPERATION_EXPRESSION);

    public static final String DELETE_OPERATION_EXPRESSION = "^\\s*(?i:delete)" + WHERE_OPERATOR_EXPRESSION + "$";
    public static final int DELETE_OPERATION_WHERE_GROUP = 1;
    public static final Pattern DELETE_OPERATION_PATTERN = Pattern.compile(DELETE_OPERATION_EXPRESSION);

    public static final String SHIELDING_SYMBOLS = "<([{\\^-=$!|]})?*+.>".replaceAll(".", "\\\\$0");
    public final static Pattern SHIELDING_SYMBOLS_PATTERN = Pattern.compile("[" + SHIELDING_SYMBOLS + "]");

    public static String shieldExpression(String shieldingExpression) {
        final Matcher shieldSymbolMatcher = RegExpUtils.SHIELDING_SYMBOLS_PATTERN.matcher(shieldingExpression);
        return shieldSymbolMatcher.replaceAll("\\\\$0").replaceAll("%", ".*");
    }
}
