package com.digdes.school;

import com.digdes.school.task.executor.CRUDRequestExecutor;
import com.digdes.school.task.executor.RequestExecutor;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class JavaSchoolStarter {

    private final RequestExecutor executor = new CRUDRequestExecutor();

    public JavaSchoolStarter() {

    }

    public List<Map<String, Object>> execute(String request) throws Exception {
        request = Objects.requireNonNull(request, "Request is null").trim();
        return executor.executeRequest(request);
    }
}
