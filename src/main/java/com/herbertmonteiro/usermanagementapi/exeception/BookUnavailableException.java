package com.herbertmonteiro.usermanagementapi.exeception;

public class BookUnavailableException extends RuntimeException {
    public BookUnavailableException(String message) {
        super(message);
    }
}
