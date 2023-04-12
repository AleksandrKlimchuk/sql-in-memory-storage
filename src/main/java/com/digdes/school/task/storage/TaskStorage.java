package com.digdes.school.task.storage;

import com.digdes.school.task.operation.utils.RowUtils;

import java.util.*;

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
    public List<Map<String, Object>> selectAllRows() {
        return data;
    }

    @Override
    public List<Object> selectValuesByColumnName(String columnName) {
        return data.stream()
                .map(stringObjectMap -> stringObjectMap.get(columnName))
                .toList();
    }

    @Override
    public void updateValuesByIndices(Set<Integer> rowIndicesToUpdate, Map<String, Object> newValues) {
        for (Integer rowToUpdateIndex : rowIndicesToUpdate) {
            Map<String, Object> storageRow = data.get(rowToUpdateIndex);
            for (Map.Entry<String, Object> newValue : newValues.entrySet()) {
                if (newValue.getValue() == RowUtils.NULL_OBJECT) {
                    storageRow.remove(newValue.getKey());
                } else {
                    storageRow.put(newValue.getKey(), newValue.getValue());
                }
            }
        }
    }
}
