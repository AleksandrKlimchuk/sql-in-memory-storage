package com.digdes.school.task.operation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InsertRequestValidator implements RequestValidator {

    private static final String INSERT_OPERATION_REGEX = "^\\s*(?i:insert)\\s+(?i:values)\\s+" +
            "(\\s*"+ COLUMN_NAME_VALUE_PAIR_REGEX + "\\s*,)*(\\s*" + COLUMN_NAME_VALUE_PAIR_REGEX + "\\s*)$";

    private static final Pattern VALIDATION_PATTERN = Pattern.compile(INSERT_OPERATION_REGEX);

    @Override
    public void validate(String request) throws Exception {
        final Matcher validationMatcher = VALIDATION_PATTERN.matcher(request);
        if (!validationMatcher.matches()) {
            throw new Exception(String.format("Invalid insert request: %s. Insert request must be formatted as" +
                    " insert values 'str'='value1', ..., 'non-str'=value2", request
            ));
        }
    }
}
