package com.rq.challenge.dto;

import lombok.Data;

@Data
public class GetEmployeeResponse {
    private String status;
    private Employee data;
    private String message;
}
