package com.digdes.school.task.operation;

import java.util.regex.Pattern;

public interface RequestValidator {

    String COLUMN_NAME_VALUE_PAIR_REGEX = "'([^']+)'\\s*=\\s*('[^']*'|[^',=\\s]*)";
    Pattern COLUMN_NAME_VALUE_PAIR_PATTERN = Pattern.compile(COLUMN_NAME_VALUE_PAIR_REGEX);

    void validate(String request) throws Exception;
}
