package com.dims.marketplace.exceptions;

import org.springframework.http.HttpStatus;

public class DataNotFoundException extends BaseException {
    public DataNotFoundException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
