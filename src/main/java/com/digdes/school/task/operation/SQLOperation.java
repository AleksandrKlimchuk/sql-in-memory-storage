package com.digdes.school.task.operation;

import com.digdes.school.task.storage.RowColumnStorage;

import java.util.List;
import java.util.Map;

public interface SQLOperation {

    List<Map<String, Object>> execute(String sqlRequest, RowColumnStorage storage) throws Exception;

    String getName();
}
