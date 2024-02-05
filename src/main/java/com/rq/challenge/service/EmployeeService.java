package com.rq.challenge.service;

import com.rq.challenge.common.RestConnector;
import com.rq.challenge.configuration.ApiConfig;
import com.rq.challenge.dto.CreateEmployeeResponse;
import com.rq.challenge.dto.Employee;
import com.rq.challenge.dto.GetAllEmployeeResponse;
import com.rq.challenge.dto.GetEmployeeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    @Autowired
    private RestConnector restConnector;

    @Autowired
    private ApiConfig apiConfig;

    public GetAllEmployeeResponse getAllEmployees() {
        String url = apiConfig.getEmployeeBaseUrl() + apiConfig.getAllEmployeesPath();
        ResponseEntity<GetAllEmployeeResponse> responseEntity = restConnector.get(url, GetAllEmployeeResponse.class);
        return responseEntity.getBody();
    }

    public GetAllEmployeeResponse getEmployeesByName(String searchString) {
        GetAllEmployeeResponse getAllEmployeeResponse = getAllEmployees();
        List<Employee> result = getAllEmployeeResponse.getData().stream()
                .filter(Objects::nonNull)
                .filter(employee -> employee.getName().toLowerCase().contains(searchString.toLowerCase()))
                .collect(Collectors.toList());
        getAllEmployeeResponse.setData(result);
        return getAllEmployeeResponse;

    }

    public GetEmployeeResponse getEmployeeById(String empId) {
        String url = apiConfig.getEmployeeBaseUrl() + apiConfig.getByEmployeeIdPath() + empId;
        return restConnector.get(url, GetEmployeeResponse.class).getBody();
    }

    public Integer getHighestSalary() {
        GetAllEmployeeResponse getAllEmployeeResponse = getAllEmployees();
        Optional<Employee> employeeWithHighestSalary = getAllEmployeeResponse.getData().stream()
                .sorted(Comparator.comparingInt(Employee::getSalary).reversed())
                .collect(Collectors.toList()).stream().findFirst();
        return employeeWithHighestSalary.map(Employee::getSalary).orElse(null);

    }

    public List<String> getTenHighestEarningEmployeesNames() {
        GetAllEmployeeResponse getAllEmployeeResponse = getAllEmployees();
        List<Employee> topTenHighestEarningEmployees = getAllEmployeeResponse.getData().stream()
                .sorted(Comparator.comparingInt(Employee::getSalary).reversed()).limit(10)
                .collect(Collectors.toList());
        return topTenHighestEarningEmployees.stream().map(Employee::getName).collect(Collectors.toList());
    }

    public CreateEmployeeResponse createEmployee(Map<String, Object> createEmployeeRequest) {
        String url = apiConfig.getEmployeeBaseUrl() + apiConfig.getCreateEmployeePath();
        return restConnector.post(url, createEmployeeRequest, CreateEmployeeResponse.class).getBody();
    }

    public String deleteEmployeeById(String id) {
        GetEmployeeResponse getEmployeeResponse = getEmployeeById(id);
        String url = apiConfig.getEmployeeBaseUrl() + apiConfig.getDeleteEmployeePath() + id;
        restConnector.delete(url, GetEmployeeResponse.class);
        return getEmployeeResponse.getData().getName();
    }
}
