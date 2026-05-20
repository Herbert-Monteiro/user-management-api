package com.herbertmonteiro.usermanagementapi.exeception;

public class InvalidDataException extends RuntimeException {
    public InvalidDataException(String message) {
        super(message);
    }
}
