package com.digdes.school.task.operation;

import com.digdes.school.task.operation.delete.DeleteSqlOperation;
import com.digdes.school.task.operation.insert.InsertSQLOperation;
import com.digdes.school.task.operation.select.SelectSQLOperation;
import com.digdes.school.task.operation.update.UpdateSQLOperation;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CRUDSQLOperationFactory implements SQLOperationFactory {

    private final Map<String, SQLOperation> operationMap = new HashMap<>();

    public CRUDSQLOperationFactory() {
        final SQLOperation insertSQLOperation = new InsertSQLOperation();
        operationMap.put(insertSQLOperation.getName(), insertSQLOperation);
        final SQLOperation selectSQLOperation = new SelectSQLOperation();
        operationMap.put(selectSQLOperation.getName(), selectSQLOperation);
        final SQLOperation updateSqlOperation = new UpdateSQLOperation();
        operationMap.put(updateSqlOperation.getName(), updateSqlOperation);
        final SQLOperation deleteOperation = new DeleteSqlOperation();
        operationMap.put(deleteOperation.getName(), deleteOperation);
    }

    @Override
    public SQLOperation createByRequest(String request) throws Exception {
        final String command = extractCommandFromRequest(request);
        final SQLOperation operation = operationMap.get(command);
        if (Objects.isNull(operation)) {
            throw new Exception(String.format("Invalid request. SQL Command %s not valid.", command));
        }
        return operation;
    }

    private String extractCommandFromRequest(String request) {
        final int indexOfCommandEnd = request.indexOf(" ");
        final String command = indexOfCommandEnd == -1 ? request : request.substring(0, indexOfCommandEnd);
        return command.toLowerCase();
    }
}
