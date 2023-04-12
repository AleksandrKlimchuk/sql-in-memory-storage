package com.digdes.school.task.operation.insert;

import com.digdes.school.task.operation.RequestValidator;
import com.digdes.school.task.utils.RegExpUtils;

import java.util.regex.Matcher;

public class InsertRequestValidator implements RequestValidator {

    @Override
    public void validate(String request) throws Exception {
        final Matcher validationMatcher = RegExpUtils.INSERT_OPERATION_PATTERN.matcher(request);
        if (!validationMatcher.matches()) {
            throw new Exception(String.format("Invalid insert request: %s. Insert request must be formatted as" +
                    " insert values 'str'='value1', ..., 'non-str'=value2", request
            ));
        }
    }
}
