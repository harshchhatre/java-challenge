package com.rq.challenge.api.controller;

import com.rq.challenge.dto.CreateEmployeeResponse;
import com.rq.challenge.dto.GetAllEmployeeResponse;
import com.rq.challenge.dto.GetEmployeeResponse;
import lombok.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

public interface IEmployeeController {

    @GetMapping()
    GetAllEmployeeResponse getAllEmployees();

    @GetMapping("/search/{searchString}")
    GetAllEmployeeResponse getEmployeesByNameSearch(@PathVariable @NonNull String searchString);

    @GetMapping("/{id}")
    GetEmployeeResponse getEmployeeById(@PathVariable @NonNull String id);

    @GetMapping("/highestSalary")
    Integer getHighestSalaryOfEmployees();

    @GetMapping("/topTenHighestEarningEmployeeNames")
    List<String> getTopTenHighestEarningEmployeeNames();

    @PostMapping()
    CreateEmployeeResponse createEmployee(@RequestBody @NonNull Map<String, Object> employeeInput);

    @DeleteMapping("/{id}")
    String deleteEmployeeById(@PathVariable @NonNull String id);
}
