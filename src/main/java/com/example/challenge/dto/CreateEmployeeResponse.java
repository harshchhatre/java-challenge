package com.example.challenge.dto;

import lombok.Data;

@Data
public class CreateEmployeeResponse {
    private String status;
    private CreateEmployeeRequest data;
    private String message;
}
