package com.digdes.school.task.operation;

import jdk.dynalink.Operation;

public interface SQLOperationFactory {

    SQLOperation createByRequest(String request) throws Exception;
}
