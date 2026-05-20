package com.herbertmonteiro.usermanagementapi.exeception;

public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}
