package com.digdes.school.task.executor;

public interface RequestExecutor<T> {

    T executeRequest(String request);
}
