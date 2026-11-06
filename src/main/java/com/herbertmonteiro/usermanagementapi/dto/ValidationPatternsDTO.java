package com.herbertmonteiro.usermanagementapi.dto;

public final class ValidationPatternsDTO {

    private ValidationPatternsDTO() {}

    public static final String EMAIL =
            "^[A-Za-z0-9._%+-]+@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*\\.[A-Za-z]{2,}$";
}
