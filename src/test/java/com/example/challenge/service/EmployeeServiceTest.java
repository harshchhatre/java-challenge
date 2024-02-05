package com.example.challenge.service;

import com.example.challenge.data.TestData;
import com.example.challenge.dto.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;

@SpringBootTest
class EmployeeServiceTest {

    @Mock
    private RestEmployeeProvider employeeProvider;

    @InjectMocks
    private EmployeeService employeeService;

    private static final String STATUS_SUCCESS = "success";

    @Test
    void testGetAllEmployees() {
        GetAllEmployeeResponse mockedResponse = new GetAllEmployeeResponse();
        mockedResponse.setStatus(STATUS_SUCCESS);
        mockedResponse.setData(TestData.getMockEmployees());

        Mockito.when(employeeProvider.getAllEmployees()).thenReturn(mockedResponse);

        GetAllEmployeeResponse response = employeeService.getAllEmployees();
        Assertions.assertEquals(2, response.getData().size());
    }

    @Test
    void testGetEmployeesById() {
        GetEmployeeResponse mockedResponse = new GetEmployeeResponse();
        mockedResponse.setData(new Employee("1", "Lorem Ipsum", 4737, 18, ""));
        Mockito.when(employeeProvider.getEmployeeById(anyString())).thenReturn(mockedResponse);

        GetEmployeeResponse response = employeeService.getEmployeeById("123");
        Assertions.assertNotNull(response.getData());
    }

    @Test
    void testGetEmployeesByNameForMatchFound() {
        GetAllEmployeeResponse mockedResponse = new GetAllEmployeeResponse();
        mockedResponse.setStatus(STATUS_SUCCESS);
        mockedResponse.setData(TestData.getMockEmployees());
        Mockito.when(employeeProvider.getAllEmployees()).thenReturn(mockedResponse);

        GetAllEmployeeResponse getAllEmployeeResponse =
                employeeService.getEmployeesByName("Lorem Ipsum");
        Optional<Employee> employee = getAllEmployeeResponse.getData().stream().findFirst();
        Assertions.assertEquals("Lorem Ipsum", employee.get().getName());
        Assertions.assertTrue(employee.isPresent());
        Assertions.assertEquals("1", employee.get().getId());
        Assertions.assertEquals(18, employee.get().getAge());
        Assertions.assertEquals(4737, employee.get().getSalary());

    }

    @Test
    void testGetEmployeesByNameNoMatchFound() {
        GetAllEmployeeResponse mockedResponse = new GetAllEmployeeResponse();
        mockedResponse.setStatus(STATUS_SUCCESS);
        mockedResponse.setData(TestData.getMockEmployees());
        Mockito.when(employeeProvider.getAllEmployees()).thenReturn(mockedResponse);

        GetAllEmployeeResponse getAllEmployeeResponse =
                employeeService.getEmployeesByName("random");
        Optional<Employee> employee = getAllEmployeeResponse.getData().stream().findFirst();
        Assertions.assertFalse(employee.isPresent());
    }

    @Test
    void testGetHighestSalary() {
        GetAllEmployeeResponse mockedResponse = new GetAllEmployeeResponse();
        mockedResponse.setStatus(STATUS_SUCCESS);
        mockedResponse.setData(TestData.getMockEmployees());
        Mockito.when(employeeProvider.getAllEmployees()).thenReturn(mockedResponse);

        Integer highestSalary = employeeService.getHighestSalary();
        Assertions.assertEquals(90750, highestSalary);
    }

    @Test
    void testDeleteById() {
        GetEmployeeResponse mockedResponse = new GetEmployeeResponse();
        mockedResponse.setData(new Employee("1", "Lorem Ipsum", 4737, 18, ""));
        Mockito.when(employeeProvider.getEmployeeById(anyString())).thenReturn(mockedResponse);
        Mockito.when(employeeProvider.deleteEmployee(anyString())).thenReturn(mockedResponse);

        String response = employeeService.deleteEmployeeById("101");
        Assertions.assertEquals("Lorem Ipsum", response);
    }

    @Test
    void testTopTenHighestSalaryEmployees() {
        List<Employee> employees = new ArrayList<>();
        // generate employees with dummy data
        IntStream.range(0, 15)
                .forEach(count -> employees.add(
                        new Employee(String.valueOf(count), "Name" + count, count * count, count + 10, "")));
        GetAllEmployeeResponse mockedResponse = new GetAllEmployeeResponse();
        mockedResponse.setStatus(STATUS_SUCCESS);
        mockedResponse.setData(employees);
        Mockito.when(employeeProvider.getAllEmployees()).thenReturn(mockedResponse);

        List<String> actualNames = employeeService.getTenHighestEarningEmployeesNames();
        List<Employee> expectedEmployees = employees.stream()
                .sorted(Comparator.comparingInt(Employee::getSalary).reversed()).limit(10)
                .collect(Collectors.toList());
        List<String> expectedNames = expectedEmployees.stream()
                .map(Employee::getName)
                .collect(Collectors.toList());
        Assertions.assertTrue(actualNames.containsAll(expectedNames));
    }

    @Test
    void testCreateEmployee() {
        CreateEmployeeResponse mockedResponse = new CreateEmployeeResponse();
        mockedResponse.setData(new CreateEmployeeRequest("200", "John Doe", 200000, 20));
        mockedResponse.setStatus(STATUS_SUCCESS);
        Mockito.when(employeeProvider.createEmployee(anyMap())).thenReturn(mockedResponse);
        Map<String, Object> payload = new HashMap<>();
        payload.put("name", "John Doe");
        payload.put("salary", "200000");
        payload.put("age", "20");

        CreateEmployeeResponse response = employeeService.createEmployee(payload);

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getData());
        Assertions.assertNotNull(response.getData().getId());
        Assertions.assertEquals(mockedResponse.getData().getAge(), response.getData().getAge());
        Assertions.assertEquals(mockedResponse.getData().getSalary(), response.getData().getSalary());
        Assertions.assertEquals(mockedResponse.getData().getName(), response.getData().getName());
    }
}