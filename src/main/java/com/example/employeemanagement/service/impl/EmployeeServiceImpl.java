package com.example.employeemanagement.service.impl;

import com.example.employeemanagement.dto.request.EmployeeCreateRequest;
import com.example.employeemanagement.dto.request.EmployeeSearchRequest;
import com.example.employeemanagement.dto.request.EmployeeUpdateRequest;
import com.example.employeemanagement.dto.response.EmployeeDto;
import com.example.employeemanagement.dto.response.PageResponse;
import com.example.employeemanagement.entity.Employee;
import com.example.employeemanagement.exception.DuplicateEmployeeException;
import com.example.employeemanagement.exception.EmployeeNotFoundException;
import com.example.employeemanagement.mapper.EmployeeMapper;
import com.example.employeemanagement.repository.EmployeeRepository;
import com.example.employeemanagement.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    @Override
    @Transactional
    public EmployeeDto create(EmployeeCreateRequest request) {
        if (employeeRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmployeeException("e-posta", request.getEmail());
        }

        Employee employee = employeeMapper.toEntity(request);
        employee.setEmployeeNumber(generateEmployeeNumber());

        Employee saved = employeeRepository.save(employee);
        log.info("Yeni çalışan oluşturuldu: {}", saved.getEmployeeNumber());
        return employeeMapper.toDto(saved);
    }

    @Override
    public EmployeeDto getById(Long id) {
        return employeeRepository.findById(id)
                .map(employeeMapper::toDto)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
    }

    @Override
    public EmployeeDto getByEmployeeNumber(String employeeNumber) {
        return employeeRepository.findByEmployeeNumber(employeeNumber)
                .map(employeeMapper::toDto)
                .orElseThrow(() -> new EmployeeNotFoundException(employeeNumber));
    }

    @Override
    @Transactional
    public EmployeeDto update(Long id, EmployeeUpdateRequest request) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));

        if (request.getEmail() != null && !request.getEmail().equals(employee.getEmail())) {
            if (employeeRepository.existsByEmail(request.getEmail())) {
                throw new DuplicateEmployeeException("e-posta", request.getEmail());
            }
        }

        employeeMapper.updateEntity(request, employee);
        Employee updated = employeeRepository.save(employee);
        log.info("Çalışan güncellendi: {}", updated.getEmployeeNumber());
        return employeeMapper.toDto(updated);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new EmployeeNotFoundException(id);
        }
        employeeRepository.deleteById(id);
        log.info("Çalışan silindi: id={}", id);
    }

    @Override
    public PageResponse<EmployeeDto> list(int page, int size, String sortBy, String sortDirection) {
        Sort sort = Sort.Direction.DESC.name().equalsIgnoreCase(sortDirection)
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Employee> result = employeeRepository.findAll(pageable);
        return toPageResponse(result);
    }

    @Override
    public PageResponse<EmployeeDto> search(EmployeeSearchRequest request) {
        Sort sort = Sort.Direction.DESC.name().equalsIgnoreCase(request.getSortDirection())
                ? Sort.by(request.getSortBy()).descending()
                : Sort.by(request.getSortBy()).ascending();
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);

        Page<Employee> result = employeeRepository.search(
                request.getFirstName(),
                request.getLastName(),
                request.getDepartment(),
                request.getStatus(),
                pageable);
        return toPageResponse(result);
    }

    private PageResponse<EmployeeDto> toPageResponse(Page<Employee> page) {
        return PageResponse.<EmployeeDto>builder()
                .content(page.getContent().stream().map(employeeMapper::toDto).toList())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .build();
    }

    private String generateEmployeeNumber() {
        return "EMP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
