package com.digdes.school.task.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        data.add(row);
    }
}
