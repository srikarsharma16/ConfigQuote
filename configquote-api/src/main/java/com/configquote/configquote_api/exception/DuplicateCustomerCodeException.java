package com.configquote.configquote_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateCustomerCodeException
        extends RuntimeException {

    public DuplicateCustomerCodeException(String code) {
        super("Customer code already exists: " + code);
    }
}
