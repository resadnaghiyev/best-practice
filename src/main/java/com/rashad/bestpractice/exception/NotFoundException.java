package com.rashad.bestpractice.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class NotFoundException extends RuntimeException {

    private final String code;

    private final String message;

    public NotFoundException(String code, String message) {
        super(code);
        this.code = code;
        this.message = message;
    }
}
