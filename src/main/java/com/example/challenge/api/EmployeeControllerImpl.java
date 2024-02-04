package com.example.challenge.api;

import com.example.challenge.dto.CreateEmployeeResponse;
import com.example.challenge.dto.EmployeeResponse;
import com.example.challenge.dto.GetAllEmployeeResponse;
import com.example.challenge.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;
import java.util.Map;

//@RestController
@RestController
@RequestMapping("/employees")
@Slf4j
public class EmployeeControllerImpl implements IEmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Override
    public Mono<GetAllEmployeeResponse> getAllEmployees() throws IOException {
        return employeeService.getAllEmployees();
    }

    @Override
    public Mono<GetAllEmployeeResponse> getEmployeesByNameSearch(String searchString) {
        return employeeService.getEmployeesByName(searchString);
    }

    @Override
    public Mono<EmployeeResponse> getEmployeeById(String id) {
        return employeeService.getEmployeeById(id);
    }

    @Override
    public Mono<Integer> getHighestSalaryOfEmployees() {
        return employeeService.getHighestSalary();
    }

    @Override
    public Mono<List<String>> getTopTenHighestEarningEmployeeNames() {
        return employeeService.getTenHighestEarningEmployeesNames();
    }

    @Override
    public Mono<CreateEmployeeResponse> createEmployee(Map<String, Object> employeeInput) {
        return employeeService.createEmployee(employeeInput);
    }

    @Override
    public Mono<String> deleteEmployeeById(String id) {
        return employeeService.deleteEmployee(id);
    }
}