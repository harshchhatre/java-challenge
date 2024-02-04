package com.example.challenge.converter.entitytoentity;

import com.example.challenge.dto.Employee;
import com.example.challenge.dto.EmployeeRequest;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class EmployeeRecordToEmployeeConverter implements Converter<EmployeeRequest, Employee> {
    @Override
    public Employee convert(EmployeeRequest value) {
        return null;
    }
}
