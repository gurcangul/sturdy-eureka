package com.example.employeemanagement.exception;

public class EmployeeNotFoundException extends RuntimeException {

    public EmployeeNotFoundException(Long id) {
        super("Çalışan bulunamadı: id=" + id);
    }

    public EmployeeNotFoundException(String employeeNumber) {
        super("Çalışan bulunamadı: employeeNumber=" + employeeNumber);
    }
}
