package com.example.challenge.api.controller;

import com.example.challenge.dto.CreateEmployeeResponse;
import com.example.challenge.dto.GetAllEmployeeResponse;
import com.example.challenge.dto.GetEmployeeResponse;
import lombok.NonNull;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/employees")
public interface IEmployeeController {

    @GetMapping()
    GetAllEmployeeResponse getAllEmployees() throws IOException;

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
