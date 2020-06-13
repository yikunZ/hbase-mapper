package org.zyk.data.util.exception;

public class DataConvertException extends RuntimeException {
    public DataConvertException(String message) {
        super(message);
    }

    public DataConvertException(Exception exception) {
        super(exception);
    }

    public DataConvertException(String message, Exception exception) {
        super(message, exception);
    }
}
