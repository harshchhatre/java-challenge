package com.rq.challenge.data;

import com.github.javafaker.Faker;
import com.rq.challenge.dto.Employee;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public final class EmployeeFixtures {
    private static final Faker faker = new Faker();

    private EmployeeFixtures() {

    }

    public static List<Employee> getMockEmployees(int size) {
        List<Employee> employees = new ArrayList<>();
        IntStream.range(0, size).forEach(count ->
                employees.add(new Employee(faker.internet().uuid(),
                        faker.name().fullName(),
                        faker.number().numberBetween(10000, 200000),
                        faker.number().numberBetween(18, 60), "")));
        return employees;
    }

    public static List<String> getDummyNames(int size) {
        List<String> names = new ArrayList<>();
        IntStream.range(0, size).forEach(count -> names.add(faker.name().fullName()));
        return names;
    }
}
