package com.digdes.school.task.operation.select;

import com.digdes.school.task.operation.SQLOperation;
import com.digdes.school.task.operation.operator.SQLOperator;
import com.digdes.school.task.operation.operator.WhereSQLOperator;
import com.digdes.school.task.storage.RowColumnStorage;
import com.digdes.school.task.operation.utils.RegExpUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

public class SelectSQLOperation implements SQLOperation {

    private static final String SQL_OPERATION_NAME = "select";

    @Override
    public List<Map<String, Object>> execute(String sqlRequest, RowColumnStorage storage) throws Exception {
        final Matcher matcher = RegExpUtils.SELECT_OPERATION_PATTERN.matcher(sqlRequest);
        if (!matcher.matches()) {
            throw new Exception(String.format("Invalid select request: %s. Select request must be formatted as " +
                    "select or i.e select where condition1 and ((condition2 or condition3) and condition4)", sqlRequest
            ));
        }
        final List<Map<String, Object>> allRows = storage.selectAllRows();
        if (Objects.isNull(matcher.group(RegExpUtils.SELECT_OPERATION_WHERE_GROUP))) {
            return allRows.stream()
                    .map(HashMap::new)
                    .collect(Collectors.toList());
        }
        final SQLOperator whereOperator = new WhereSQLOperator();
        final String group = matcher.group(RegExpUtils.SELECT_OPERATION_WHERE_CONDITIONS_GROUP);
        final Set<Integer> selectedIndices = whereOperator.execute(
                group, storage
        );
        return selectedIndices.stream()
                .map(allRows::get)
                .map(HashMap::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getName() {
        return SQL_OPERATION_NAME;
    }
}
