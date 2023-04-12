package com.digdes.school.task.operation.delete;

import com.digdes.school.task.operation.SQLOperation;
import com.digdes.school.task.operation.operator.SQLOperator;
import com.digdes.school.task.operation.operator.WhereSQLOperator;
import com.digdes.school.task.operation.utils.RegExpUtils;
import com.digdes.school.task.storage.RowColumnStorage;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DeleteSqlOperation implements SQLOperation {

    private static final String SQL_OPERATION_NAME = "delete";

    @Override
    public List<Map<String, Object>> execute(String sqlRequest, RowColumnStorage storage) throws Exception {
        final Matcher matcher = RegExpUtils.DELETE_OPERATION_PATTERN.matcher(sqlRequest);
        if (!matcher.matches()) {
            throw new Exception("Delete request has invalid structure");
        }
        final Set<Integer> indicesOfDeleteRows;
        final List<Map<String, Object>> allRows = storage.selectAllRows();
        if (Objects.isNull(matcher.group(RegExpUtils.DELETE_OPERATION_WHERE_GROUP))) {
            indicesOfDeleteRows = IntStream.range(0, allRows.size()).boxed().collect(Collectors.toSet());
        } else {
            final String whereConditions = matcher.group(RegExpUtils.DELETE_OPERATION_WHERE_CONDITIONS_GROUP);
            final SQLOperator whereOperator = new WhereSQLOperator();
            indicesOfDeleteRows = whereOperator.execute(whereConditions, storage);
        }
        final List<Map<String, Object>> deletedRows = indicesOfDeleteRows.stream()
                .map(allRows::get)
                .collect(Collectors.toList());
        storage.deleteByIndices(indicesOfDeleteRows);
        return deletedRows;
    }

    @Override
    public String getName() {
        return SQL_OPERATION_NAME;
    }
}
