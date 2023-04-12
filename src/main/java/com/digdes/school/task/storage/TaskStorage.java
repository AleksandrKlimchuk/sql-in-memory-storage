package com.digdes.school.task.storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TaskStorage implements RowColumnStorage {

    private final List<Map<String, Object>> data = new ArrayList<>();
    private final TableColumnsDescriptor columnsDescriptor = new TaskTableColumnsDescriptor();

    @Override
    public List<Map<String, Object>> getData() {
        return data;
    }

    @Override
    public TableColumnsDescriptor getColumnsDescriptor() {
        return columnsDescriptor;
    }

    @Override
    public void insert(Map<String, Object> row) {
        data.add(new HashMap<>(row));
    }

    @Override
    public List<Map<String, Object>> select() {
        return makeCopy();
    }

    @Override
    public List<Object> selectValuesByColumnName(String columnName) {
        return data.stream()
                .map(stringObjectMap -> stringObjectMap.get(columnName))
                .toList();
    }

    private List<Map<String, Object>> makeCopy() {
        return data.stream()
                .map(HashMap::new)
                .collect(Collectors.toList());
    }
}
