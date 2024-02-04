package com.example.challenge.service;

import com.example.challenge.dto.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeService {
    @Autowired
    private ConversionService conversionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestEmployeeProvider employeeProvider;

    public Mono<GetAllEmployeeResponse> getAllEmployees() {
        return employeeProvider.getAllEmployees();
    }

    public Mono<GetAllEmployeeResponse> getEmployeesByName(String searchString) {
        return employeeProvider.getAllEmployees().map(employeeResponse -> {
                    List<Employee> result = employeeResponse.getData().stream()
                            .map(e -> conversionService.convert(e, Employee.class))
                            .filter(employee -> employee.getName().toLowerCase().contains(searchString))
                            .collect(Collectors.toList());
                    employeeResponse.setData(result);
                    return employeeResponse;
                })
                .switchIfEmpty(Mono.empty());
    }

    public Mono<EmployeeResponse> getEmployeeById(String empId) {
        return employeeProvider.getEmployeeById(empId);
    }

    public Mono<Integer> getHighestSalary() {
        return employeeProvider.getAllEmployees().map(employeeResponse -> {
                    Optional<Employee> employeeWithHighestSalary = employeeResponse.getData().stream()
                            .sorted(Comparator.comparingInt(Employee::getSalary).reversed())
                            .collect(Collectors.toList()).stream().findFirst();
                    return employeeWithHighestSalary.map(Employee::getSalary).orElse(null);
                })
                .switchIfEmpty(Mono.empty());
    }

    public Mono<List<String>> getTenHighestEarningEmployeesNames() {
        return employeeProvider.getAllEmployees().map(employeeResponse -> {
            List<Employee> topTenHighestEarningEmployees = employeeResponse.getData().stream()
                    .sorted(Comparator.comparingInt(Employee::getSalary).reversed()).limit(10)
                    .collect(Collectors.toList());
            return topTenHighestEarningEmployees.stream().map(Employee::getName).collect(Collectors.toList());

        });
    }

    public Mono<CreateEmployeeResponse> createEmployee(Map<String, Object> employeeInput) {
        EmployeeRequest employeeRequest = objectMapper.convertValue(employeeInput, EmployeeRequest.class);
        return employeeProvider.createEmployee(employeeRequest);
    }

    public Mono<String> deleteEmployee(String id) {
        return getEmployeeById(id).flatMap(employeeResponse -> employeeProvider.deleteEmployee(id)
                .map(response -> employeeResponse.getData().getName())).switchIfEmpty(Mono.empty());
    }
}
