package com.example.challenge.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class ApiConfig {
    @Value("${api.employee.base-url}")
    private String employeeBaseUrl;

    @Value("${api.employee.create}")
    private String createEmployee;

    @Value("${api.employee.delete}")
    private String deleteEmployee;

    @Value("${api.employee.getAll}")
    private String allEmployees;

    @Value("${api.employee.searchById}")
    private String byEmployeeId;
}