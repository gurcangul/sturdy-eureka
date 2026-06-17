package com.example.employeemanagement.service;

import com.example.employeemanagement.dto.request.EmployeeCreateRequest;
import com.example.employeemanagement.dto.response.EmployeeDto;
import com.example.employeemanagement.entity.Employee;
import com.example.employeemanagement.entity.EmployeeStatus;
import com.example.employeemanagement.exception.DuplicateEmployeeException;
import com.example.employeemanagement.exception.EmployeeNotFoundException;
import com.example.employeemanagement.mapper.EmployeeMapper;
import com.example.employeemanagement.repository.EmployeeRepository;
import com.example.employeemanagement.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeMapper employeeMapper;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private EmployeeCreateRequest createRequest;
    private Employee employee;
    private EmployeeDto employeeDto;

    @BeforeEach
    void setUp() {
        createRequest = new EmployeeCreateRequest();
        createRequest.setFirstName("Test");
        createRequest.setLastName("User");
        createRequest.setEmail("test@example.com");
        createRequest.setStatus(EmployeeStatus.ACTIVE);
        createRequest.setStartDate(LocalDate.now());

        employee = Employee.builder()
                .id(1L)
                .employeeNumber("EMP-TEST001")
                .firstName("Test")
                .lastName("User")
                .email("test@example.com")
                .status(EmployeeStatus.ACTIVE)
                .build();

        employeeDto = new EmployeeDto();
        employeeDto.setId(1L);
        employeeDto.setFirstName("Test");
        employeeDto.setLastName("User");
        employeeDto.setEmail("test@example.com");
    }

    @Test
    void create_success() {
        when(employeeRepository.existsByEmail(createRequest.getEmail())).thenReturn(false);
        when(employeeMapper.toEntity(createRequest)).thenReturn(employee);
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
        when(employeeMapper.toDto(employee)).thenReturn(employeeDto);

        EmployeeDto result = employeeService.create(createRequest);

        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo("test@example.com");
    }

    @Test
    void create_duplicateEmail_throwsException() {
        when(employeeRepository.existsByEmail(createRequest.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> employeeService.create(createRequest))
                .isInstanceOf(DuplicateEmployeeException.class);
    }

    @Test
    void getById_notFound_throwsException() {
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> employeeService.getById(99L))
                .isInstanceOf(EmployeeNotFoundException.class);
    }

    @Test
    void getById_success() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeMapper.toDto(employee)).thenReturn(employeeDto);

        EmployeeDto result = employeeService.getById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
    }
}
