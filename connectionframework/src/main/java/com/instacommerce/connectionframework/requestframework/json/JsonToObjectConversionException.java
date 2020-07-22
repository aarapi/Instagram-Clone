package com.instacommerce.connectionframework.requestframework.json;

public class JsonToObjectConversionException extends RuntimeException {

    private static final long serialVersionUID = 6230071353071098596L;

    public JsonToObjectConversionException() {
    }

    public JsonToObjectConversionException(String detailMessage) {
        super(detailMessage);
    }

    public JsonToObjectConversionException(Throwable throwable) {
        super(throwable);
    }

    public JsonToObjectConversionException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }
}
