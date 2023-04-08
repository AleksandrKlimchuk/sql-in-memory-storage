package com.digdes.school.task.storage;

import java.util.function.Function;

public interface TableColumnsDescriptor {

    Function<String, Object> getConverterByColumnName(String columnName);
}
