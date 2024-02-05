package com.example.challenge.api.controller.impl;

import com.example.challenge.api.controller.IEmployeeController;
import com.example.challenge.dto.CreateEmployeeResponse;
import com.example.challenge.dto.GetAllEmployeeResponse;
import com.example.challenge.dto.GetEmployeeResponse;
import com.example.challenge.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/employees")
@Slf4j
public class EmployeeControllerImpl implements IEmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Override
    public GetAllEmployeeResponse getAllEmployees() {
        log.debug("getAllEmployees() :: Request to get all employees");
        return employeeService.getAllEmployees();
    }

    @Override
    public GetAllEmployeeResponse getEmployeesByNameSearch(String searchString) {
        log.debug("getEmployeesByNameSearch() :: Request to get all employees with matching search {}", searchString);
        return employeeService.getEmployeesByName(searchString);
    }

    @Override
    public GetEmployeeResponse getEmployeeById(String id) {
        log.debug("getEmployeeById() :: Request to get employee with id {}", id);
        return employeeService.getEmployeeById(id);
    }

    @Override
    public Integer getHighestSalaryOfEmployees() {
        log.debug("getHighestSalaryOfEmployees() :: Request to get employee with highest salary");
        return employeeService.getHighestSalary();
    }

    @Override
    public List<String> getTopTenHighestEarningEmployeeNames() {
        log.debug("getTopTenHighestEarningEmployeeNames() :: Request to get top ten highest salary employees");
        return employeeService.getTenHighestEarningEmployeesNames();
    }

    @Override
    public CreateEmployeeResponse createEmployee(Map<String, Object> employeeInput) {
        log.debug("createEmployee() :: Request to get create an employee");
        return employeeService.createEmployee(employeeInput);
    }

    @Override
    public String deleteEmployeeById(String id) {
        log.debug("deleteEmployeeById() :: Request to delete an employee with id {}", id);
        return employeeService.deleteEmployeeById(id);
    }
}