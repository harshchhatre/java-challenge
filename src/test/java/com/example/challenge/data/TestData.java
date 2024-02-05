package com.example.challenge.data;

import com.example.challenge.dto.Employee;

import java.util.ArrayList;
import java.util.List;

public final class TestData {
    private TestData() {

    }

    public static List<Employee> getMockEmployees() {
        Employee mockEmployee1 = new Employee("1", "Lorem Ipsum", 4737, 18, "");
        Employee mockEmployee2 = new Employee("2", "John Doe", 90750, 33, "");
        List<Employee> employees = new ArrayList<>();
        employees.add(mockEmployee1);
        employees.add(mockEmployee2);
        return employees;
    }
}
