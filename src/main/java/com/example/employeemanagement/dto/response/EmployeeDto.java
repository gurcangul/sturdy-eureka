package com.example.employeemanagement.dto.response;

import com.example.employeemanagement.entity.EmployeeStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class EmployeeDto {

    private Long id;
    private String employeeNumber;
    private String firstName;
    private String lastName;
    private String fullName;
    private String email;
    private String phone;
    private String department;
    private String position;
    private BigDecimal salary;
    private LocalDate startDate;
    private EmployeeStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
