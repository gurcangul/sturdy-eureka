package com.example.employeemanagement.dto.request;

import com.example.employeemanagement.entity.EmployeeStatus;
import lombok.Data;

@Data
public class EmployeeSearchRequest {

    private String firstName;
    private String lastName;
    private String email;
    private String department;
    private String position;
    private EmployeeStatus status;
    private String employeeNumber;

    private int page = 0;
    private int size = 20;
    private String sortBy = "id";
    private String sortDirection = "ASC";
}
