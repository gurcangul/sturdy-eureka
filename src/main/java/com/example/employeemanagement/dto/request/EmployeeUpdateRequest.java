package com.example.employeemanagement.dto.request;

import com.example.employeemanagement.entity.EmployeeStatus;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class EmployeeUpdateRequest {

    @Size(max = 100)
    private String firstName;

    @Size(max = 100)
    private String lastName;

    @Email(message = "Geçerli bir e-posta adresi giriniz")
    @Size(max = 150)
    private String email;

    @Size(max = 20)
    private String phone;

    @Size(max = 100)
    private String department;

    @Size(max = 100)
    private String position;

    private BigDecimal salary;

    private LocalDate startDate;

    private EmployeeStatus status;
}
