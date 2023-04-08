package com.digdes.school.task.executor;

import com.digdes.school.task.operation.SQLOperation;
import com.digdes.school.task.operation.SQLOperationFactory;
import com.digdes.school.task.operation.CRUDSQLOperationFactory;
import com.digdes.school.task.storage.RowColumnStorage;
import com.digdes.school.task.storage.TaskStorage;

import java.util.List;
import java.util.Map;

public class CRUDRequestExecutor implements RequestExecutor {

    private final RowColumnStorage storage = new TaskStorage();
    private final SQLOperationFactory sqlOperationFactory = new CRUDSQLOperationFactory();

    @Override
    public List<Map<String, Object>> executeRequest(String request) throws Exception {
        final SQLOperation sqlOperation = sqlOperationFactory.createByRequest(request);
        return sqlOperation.execute(request, storage);
    }
}
