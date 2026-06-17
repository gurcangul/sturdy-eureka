package com.example.employeemanagement.exception;

public class DuplicateEmployeeException extends RuntimeException {

    public DuplicateEmployeeException(String field, String value) {
        super("Bu " + field + " zaten kullanılıyor: " + value);
    }
}
