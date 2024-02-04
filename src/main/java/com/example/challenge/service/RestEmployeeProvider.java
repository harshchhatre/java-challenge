package com.example.challenge.service;

import com.example.challenge.configuration.ApiConfig;
import com.example.challenge.dto.CreateEmployeeResponse;
import com.example.challenge.dto.EmployeeRequest;
import com.example.challenge.dto.EmployeeResponse;
import com.example.challenge.dto.GetAllEmployeeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class RestEmployeeProvider {

    @Autowired
    private ReactiveWebClient webClient;

    @Autowired
    private ApiConfig apiConfig;


    public Mono<GetAllEmployeeResponse> getAllEmployees() {
        return webClient.getMono(apiConfig.getEmployeeBaseUrl() + apiConfig.getAllEmployees(), GetAllEmployeeResponse.class)
                .switchIfEmpty(Mono.error(new RuntimeException("------")));
    }

    public Mono<EmployeeResponse> getEmployeeById(String id) {
        return webClient.getMono(apiConfig.getEmployeeBaseUrl() + apiConfig.getByEmployeeId(), EmployeeResponse.class);
    }

    public Mono<CreateEmployeeResponse> createEmployee(EmployeeRequest employeeRequest) {
        try {
            return webClient.postMono(apiConfig.getEmployeeBaseUrl() + apiConfig.getCreateEmployee(), employeeRequest, CreateEmployeeResponse.class);
        } catch (Exception e) {
            throw e;
        }
    }

    public Mono<EmployeeResponse> deleteEmployee(String id) {
        return webClient.deleteMono(apiConfig.getEmployeeBaseUrl() + apiConfig.getDeleteEmployee(), EmployeeResponse.class);
    }
}
