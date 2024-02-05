package com.example.challenge.service;

import com.example.challenge.dto.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestEmployeeProvider employeeProvider;

    public GetAllEmployeeResponse getAllEmployees() {
        return employeeProvider.getAllEmployees();
    }

    public GetAllEmployeeResponse getEmployeesByName(String searchString) {
        GetAllEmployeeResponse getAllEmployeeResponse = employeeProvider.getAllEmployees();
        List<Employee> result = getAllEmployeeResponse.getData().stream()
                .filter(Objects::nonNull)
                .filter(employee -> employee.getName().toLowerCase().contains(searchString.toLowerCase()))
                .collect(Collectors.toList());
        getAllEmployeeResponse.setData(result);
        return getAllEmployeeResponse;

    }

    public GetEmployeeResponse getEmployeeById(String empId) {
        return employeeProvider.getEmployeeById(empId);
    }

    public Integer getHighestSalary() {
        GetAllEmployeeResponse getAllEmployeeResponse = employeeProvider.getAllEmployees();
        Optional<Employee> employeeWithHighestSalary = getAllEmployeeResponse.getData().stream()
                .sorted(Comparator.comparingInt(Employee::getSalary).reversed())
                .collect(Collectors.toList()).stream().findFirst();
        return employeeWithHighestSalary.map(Employee::getSalary).orElse(null);

    }

    public List<String> getTenHighestEarningEmployeesNames() {
        GetAllEmployeeResponse getAllEmployeeResponse = employeeProvider.getAllEmployees();
        List<Employee> topTenHighestEarningEmployees = getAllEmployeeResponse.getData().stream()
                .sorted(Comparator.comparingInt(Employee::getSalary).reversed()).limit(10)
                .collect(Collectors.toList());
        return topTenHighestEarningEmployees.stream().map(Employee::getName).collect(Collectors.toList());
    }

    public CreateEmployeeResponse createEmployee(Map<String, Object> employeeInput) {
        CreateEmployeeRequest createEmployeeRequest = objectMapper.convertValue(employeeInput, CreateEmployeeRequest.class);
        return employeeProvider.createEmployee(createEmployeeRequest);
    }

    public String deleteEmployee(String id) {
        GetEmployeeResponse getEmployeeResponse = getEmployeeById(id);
        employeeProvider.deleteEmployee(id);
        return getEmployeeResponse.getData().getName();
    }
}
