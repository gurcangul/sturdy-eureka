package com.example.employeemanagement.service;

import com.example.employeemanagement.dto.request.EmployeeCreateRequest;
import com.example.employeemanagement.dto.request.EmployeeSearchRequest;
import com.example.employeemanagement.dto.request.EmployeeUpdateRequest;
import com.example.employeemanagement.dto.response.EmployeeDto;
import com.example.employeemanagement.dto.response.PageResponse;

public interface EmployeeService {

    EmployeeDto create(EmployeeCreateRequest request);

    EmployeeDto getById(Long id);

    EmployeeDto getByEmployeeNumber(String employeeNumber);

    EmployeeDto update(Long id, EmployeeUpdateRequest request);

    void delete(Long id);

    PageResponse<EmployeeDto> list(int page, int size, String sortBy, String sortDirection);

    PageResponse<EmployeeDto> search(EmployeeSearchRequest request);
}
