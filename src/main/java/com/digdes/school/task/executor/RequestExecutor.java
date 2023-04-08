package com.digdes.school.task.executor;

import java.util.List;
import java.util.Map;

public interface RequestExecutor {

     List<Map<String, Object>> executeRequest(String request) throws Exception;
}
