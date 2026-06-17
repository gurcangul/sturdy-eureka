package com.example.employeemanagement.controller;

import com.example.employeemanagement.dto.request.EmployeeCreateRequest;
import com.example.employeemanagement.dto.request.EmployeeSearchRequest;
import com.example.employeemanagement.dto.request.EmployeeUpdateRequest;
import com.example.employeemanagement.dto.response.ApiResponse;
import com.example.employeemanagement.dto.response.EmployeeDto;
import com.example.employeemanagement.dto.response.PageResponse;
import com.example.employeemanagement.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor
@Tag(name = "Çalışan Yönetimi", description = "Çalışan CRUD ve arama işlemleri")
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Yeni çalışan kaydet")
    public ApiResponse<EmployeeDto> create(@Valid @RequestBody EmployeeCreateRequest request) {
        return ApiResponse.success(employeeService.create(request), "Çalışan başarıyla oluşturuldu");
    }

    @GetMapping("/{id}")
    @Operation(summary = "ID ile çalışan detayı")
    public ApiResponse<EmployeeDto> getById(@PathVariable Long id) {
        return ApiResponse.success(employeeService.getById(id));
    }

    @GetMapping("/number/{employeeNumber}")
    @Operation(summary = "Sicil numarası ile çalışan detayı")
    public ApiResponse<EmployeeDto> getByNumber(@PathVariable String employeeNumber) {
        return ApiResponse.success(employeeService.getByEmployeeNumber(employeeNumber));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Çalışan bilgilerini güncelle")
    public ApiResponse<EmployeeDto> update(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeUpdateRequest request) {
        return ApiResponse.success(employeeService.update(id, request), "Çalışan güncellendi");
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Çalışanı sil")
    public void delete(@PathVariable Long id) {
        employeeService.delete(id);
    }

    @GetMapping
    @Operation(summary = "Tüm çalışanları listele (sayfalı)")
    public ApiResponse<PageResponse<EmployeeDto>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        return ApiResponse.success(employeeService.list(page, size, sortBy, sortDirection));
    }

    @PostMapping("/search")
    @Operation(summary = "Çalışan ara (filtre + sayfalama)")
    public ApiResponse<PageResponse<EmployeeDto>> search(@RequestBody EmployeeSearchRequest request) {
        return ApiResponse.success(employeeService.search(request));
    }
}
