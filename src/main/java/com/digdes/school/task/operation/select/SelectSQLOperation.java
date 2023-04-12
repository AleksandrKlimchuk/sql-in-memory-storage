package com.digdes.school.task.operation.select;

import com.digdes.school.task.operation.SQLOperation;
import com.digdes.school.task.operation.utils.RegExpUtils;
import com.digdes.school.task.operation.utils.RowUtils;
import com.digdes.school.task.storage.RowColumnStorage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
        final Set<Integer> indicesOfSelectedRows = RowUtils.extractIndicesByConditionsOrAll(
                storage, matcher, RegExpUtils.SELECT_OPERATION_WHERE_GROUP
        );
        return indicesOfSelectedRows.stream()
                .map(allRows::get)
                .map(HashMap::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getName() {
        return SQL_OPERATION_NAME;
    }
}
