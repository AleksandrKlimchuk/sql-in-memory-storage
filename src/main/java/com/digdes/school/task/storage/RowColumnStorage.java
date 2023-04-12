package com.digdes.school.task.storage;

import java.util.List;
import java.util.Map;

public interface RowColumnStorage extends Storage<List<Map<String, Object>>>{

    TableColumnsDescriptor getColumnsDescriptor();

    void insert(Map<String, Object> row);
    List<Map<String, Object>> select();
    List<Object> selectValuesByColumnName(String columnName);
}
