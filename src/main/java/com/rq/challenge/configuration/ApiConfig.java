package com.rq.challenge.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class ApiConfig {
    @Value("${api.baseUrl}")
    private String employeeBaseUrl;

    @Value("${api.employee.createPath}")
    private String createEmployeePath;

    @Value("${api.employee.deletePath}")
    private String deleteEmployeePath;

    @Value("${api.employee.getAllPath}")
    private String allEmployeesPath;

    @Value("${api.employee.searchByIdPath}")
    private String byEmployeeIdPath;
}