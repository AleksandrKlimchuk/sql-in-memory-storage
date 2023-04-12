package com.digdes.school.task.operation.operator;

import com.digdes.school.task.storage.RowColumnStorage;

import java.util.Set;

public interface SQLOperator {

    Set<Integer> execute(String conditions, RowColumnStorage storage) throws Exception;
}
