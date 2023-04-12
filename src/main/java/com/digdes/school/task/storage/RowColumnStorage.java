package com.digdes.school.task.storage;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface RowColumnStorage {

    TableColumnsDescriptor getColumnsDescriptor();

    void insert(Map<String, Object> row);
    List<Map<String, Object>> selectAllRows();
    List<Object> selectValuesByColumnName(String columnName);
    void updateValuesByIndices(Set<Integer> indices, Map<String, Object> values);
    void deleteByIndices(Set<Integer> indicesOfDeleteRows);
}
