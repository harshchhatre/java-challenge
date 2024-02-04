package com.example.challenge.dto;

import lombok.Data;

import java.util.List;

@Data
public class GetAllEmployeeResponse {
    private String status;
    private List<Employee> data;
    private String message;
}
