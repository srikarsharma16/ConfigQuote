package com.configquote.configquote_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateSkuException
        extends RuntimeException {

    public DuplicateSkuException(String sku) {
        super("Product SKU already exists: " + sku);
    }
}
