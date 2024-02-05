package com.example.challenge.service;

import com.example.challenge.common.RestConnector;
import com.example.challenge.configuration.ApiConfig;
import com.example.challenge.dto.CreateEmployeeRequest;
import com.example.challenge.dto.CreateEmployeeResponse;
import com.example.challenge.dto.GetAllEmployeeResponse;
import com.example.challenge.dto.GetEmployeeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class RestEmployeeProvider {

    @Autowired
    private RestConnector restConnector;

    @Autowired
    private ApiConfig apiConfig;


    public GetAllEmployeeResponse getAllEmployees() {
        String url = apiConfig.getEmployeeBaseUrl() + apiConfig.getAllEmployeesPath();
        ResponseEntity<GetAllEmployeeResponse> responseEntity = restConnector.get(url, GetAllEmployeeResponse.class);
        return responseEntity.getBody();
    }

    public GetEmployeeResponse getEmployeeById(String id) {
        String url = apiConfig.getEmployeeBaseUrl() + apiConfig.getByEmployeeIdPath() + id;
        return restConnector.get(url, GetEmployeeResponse.class).getBody();
    }

    public CreateEmployeeResponse createEmployee(CreateEmployeeRequest createEmployeeRequest) {
        String url = apiConfig.getEmployeeBaseUrl() + apiConfig.getCreateEmployeePath();
        return restConnector.post(url, createEmployeeRequest, CreateEmployeeResponse.class).getBody();
    }

    public GetEmployeeResponse deleteEmployee(String id) {
        String url = apiConfig.getEmployeeBaseUrl() + apiConfig.getDeleteEmployeePath() + id;
        return restConnector.delete(url, GetEmployeeResponse.class).getBody();
    }
}
