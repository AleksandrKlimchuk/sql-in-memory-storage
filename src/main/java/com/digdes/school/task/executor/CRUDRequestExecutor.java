package com.digdes.school.task.executor;

import com.digdes.school.task.storage.Storage;
import com.digdes.school.task.storage.TableStorage;

import java.util.List;
import java.util.Map;

public class CRUDRequestExecutor implements RequestExecutor<List<Map<String, Object>>> {

    private final Storage<List<Map<String, Object>>> storage = new TableStorage();

    @Override
    public List<Map<String, Object>> executeRequest(String request) {
        return null;
    }
}
