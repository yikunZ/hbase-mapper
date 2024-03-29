package org.zyk.data.hbase.mapper.exception;

public class MapperException extends RuntimeException {
    public MapperException(String message) {
        super(message);
    }

    public MapperException(Exception exception) {
        super(exception);
    }

    public MapperException(String message, Exception exception) {
        super(message, exception);
    }
}
