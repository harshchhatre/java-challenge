package com.example.challenge.api;

import com.example.challenge.dto.CreateEmployeeResponse;
import com.example.challenge.dto.EmployeeResponse;
import com.example.challenge.dto.GetAllEmployeeResponse;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
public interface IEmployeeController {

    @GetMapping()
    Mono<GetAllEmployeeResponse> getAllEmployees() throws IOException;

    @GetMapping("/search/{searchString}")
    Mono<GetAllEmployeeResponse> getEmployeesByNameSearch(@PathVariable String searchString);

    @GetMapping("/{id}")
    Mono<EmployeeResponse> getEmployeeById(@PathVariable String id);

    @GetMapping("/highestSalary")
    Mono<Integer> getHighestSalaryOfEmployees();

    @GetMapping("/topTenHighestEarningEmployeeNames")
    Mono<List<String>> getTopTenHighestEarningEmployeeNames();

    @PostMapping()
    Mono<CreateEmployeeResponse> createEmployee(@RequestBody Map<String, Object> employeeInput);

    @DeleteMapping("/{id}")
    Mono<String> deleteEmployeeById(@PathVariable String id);

}
