package com.digdes.school.task.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TableStorage implements Storage<List<Map<String, Object>>> {

    private final List<Map<String, Object>> data = new ArrayList<>();
    // TODO: add column descriptions

    @Override
    public List<Map<String, Object>> getData() {
        return data;
    }
}
