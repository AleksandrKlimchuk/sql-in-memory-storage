package com.digdes.school.task.operation;

public interface SQLOperationFactory {

    SQLOperation createByRequest(String request) throws Exception;
}
