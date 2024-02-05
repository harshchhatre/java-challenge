package com.rq.challenge.service;

import com.rq.challenge.common.RestConnector;
import com.rq.challenge.configuration.ApiConfig;
import com.rq.challenge.data.DummyEmployeeFixtures;
import com.rq.challenge.dto.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;

@SpringBootTest
class EmployeeServiceTest {


    @Mock
    private RestConnector restConnector;

    @Mock
    private ApiConfig apiConfig;

    @InjectMocks
    private EmployeeService employeeService;

    private static final String STATUS_SUCCESS = "success";

    private static final String BASE_URL = "https://dummy.restapiexample.com/api/v1/";
    private static final String GET_ALL_PATH = "employees";
    public static final String SEARCH_BY_ID_PATH = "employee/";
    public static final String CREATE_PATH = "create";
    public static final String DELETE_PATH = "delete/";


    @BeforeEach
    void setUp() {
        Mockito.when(apiConfig.getEmployeeBaseUrl()).thenReturn(BASE_URL);
    }

    @Test
    void testGetAllEmployees() {
        GetAllEmployeeResponse mockedResponse = new GetAllEmployeeResponse();
        mockedResponse.setStatus(STATUS_SUCCESS);
        mockedResponse.setData(DummyEmployeeFixtures.getMockEmployees(10));

        Mockito.when(apiConfig.getAllEmployeesPath()).thenReturn(GET_ALL_PATH);
        Mockito.when(restConnector.get(BASE_URL + GET_ALL_PATH, GetAllEmployeeResponse.class))
                .thenReturn(new ResponseEntity<>(mockedResponse, HttpStatus.OK));

        GetAllEmployeeResponse response = employeeService.getAllEmployees();
        Assertions.assertEquals(10, response.getData().size());
    }

    @Test
    void testGetEmployeesById() {
        GetEmployeeResponse mockedResponse = new GetEmployeeResponse();
        mockedResponse.setData(new Employee("1", "Lorem Ipsum", 4737, 18, ""));

        Mockito.when(apiConfig.getByEmployeeIdPath()).thenReturn(SEARCH_BY_ID_PATH);
        Mockito.when(restConnector.get(BASE_URL + SEARCH_BY_ID_PATH + "123", GetEmployeeResponse.class))
                .thenReturn(new ResponseEntity<>(mockedResponse, HttpStatus.OK));

        GetEmployeeResponse response = employeeService.getEmployeeById("123");
        Assertions.assertNotNull(response.getData());
    }

    @Test
    void testGetEmployeesByNameForMatchFound() {
        GetAllEmployeeResponse mockedResponse = new GetAllEmployeeResponse();
        mockedResponse.setStatus(STATUS_SUCCESS);
        Employee expected = new Employee("1", "Lorem Ipsum", 4737, 18, "");
        List<Employee> employees = DummyEmployeeFixtures.getMockEmployees(10);
        employees.add(expected); // add expected employee object to completely random data for verification
        mockedResponse.setData(employees);

        Mockito.when(apiConfig.getAllEmployeesPath()).thenReturn(GET_ALL_PATH);
        Mockito.when(restConnector.get(BASE_URL + GET_ALL_PATH, GetAllEmployeeResponse.class))
                .thenReturn(new ResponseEntity<>(mockedResponse, HttpStatus.OK));

        GetAllEmployeeResponse getAllEmployeeResponse =
                employeeService.getEmployeesByName("Lorem Ipsum");
        Optional<Employee> employee = getAllEmployeeResponse.getData().stream().findFirst();
        Assertions.assertTrue(employee.isPresent());
        Assertions.assertEquals(expected, employee.get());
    }

    @Test
    void testGetEmployeesByNameNoMatchFound() {
        GetAllEmployeeResponse mockedResponse = new GetAllEmployeeResponse();
        mockedResponse.setStatus(STATUS_SUCCESS);
        mockedResponse.setData(DummyEmployeeFixtures.getMockEmployees(20));

        Mockito.when(apiConfig.getAllEmployeesPath()).thenReturn(GET_ALL_PATH);
        Mockito.when(restConnector.get(BASE_URL + GET_ALL_PATH, GetAllEmployeeResponse.class))
                .thenReturn(new ResponseEntity<>(mockedResponse, HttpStatus.OK));

        GetAllEmployeeResponse getAllEmployeeResponse =
                employeeService.getEmployeesByName("random");
        Optional<Employee> employee = getAllEmployeeResponse.getData().stream().findFirst();
        Assertions.assertFalse(employee.isPresent());
    }

    @Test
    void testGetHighestSalary() {
        GetAllEmployeeResponse mockedResponse = new GetAllEmployeeResponse();
        mockedResponse.setStatus(STATUS_SUCCESS);
        Employee expected = new Employee("1", "Lorem Ipsum", 900750, 18, "");
        List<Employee> employees = DummyEmployeeFixtures.getMockEmployees(15);
        employees.add(expected); // add expected employee object to completely random data for verification
        mockedResponse.setData(employees);

        Mockito.when(apiConfig.getAllEmployeesPath()).thenReturn(GET_ALL_PATH);
        Mockito.when(restConnector.get(BASE_URL + GET_ALL_PATH, GetAllEmployeeResponse.class))
                .thenReturn(new ResponseEntity<>(mockedResponse, HttpStatus.OK));

        Integer highestSalary = employeeService.getHighestSalary();
        Assertions.assertEquals(expected.getSalary(), highestSalary);
    }

    @Test
    void testDeleteById() {
        GetEmployeeResponse mockedResponse = new GetEmployeeResponse();
        mockedResponse.setData(new Employee("123", "Lorem Ipsum", 4737, 18, ""));

        Mockito.when(apiConfig.getDeleteEmployeePath()).thenReturn(DELETE_PATH);
        Mockito.when(apiConfig.getByEmployeeIdPath()).thenReturn(SEARCH_BY_ID_PATH);
        Mockito.when(restConnector.delete(BASE_URL + DELETE_PATH + "123", GetEmployeeResponse.class))
                .thenReturn(new ResponseEntity<>(mockedResponse, HttpStatus.OK));

        Mockito.when(restConnector.get(BASE_URL + SEARCH_BY_ID_PATH + "123", GetEmployeeResponse.class))
                .thenReturn(new ResponseEntity<>(mockedResponse, HttpStatus.OK));


        String response = employeeService.deleteEmployeeById("123");
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
        Mockito.when(apiConfig.getAllEmployeesPath()).thenReturn(GET_ALL_PATH);
        Mockito.when(restConnector.get(BASE_URL + GET_ALL_PATH, GetAllEmployeeResponse.class))
                .thenReturn(new ResponseEntity<>(mockedResponse, HttpStatus.OK));

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

        Mockito.when(apiConfig.getCreateEmployeePath()).thenReturn(CREATE_PATH);
        Mockito.when(restConnector.post(eq(BASE_URL + CREATE_PATH), anyMap(), eq(CreateEmployeeResponse.class)))
                .thenReturn(new ResponseEntity<>(mockedResponse, HttpStatus.OK));

        Map<String, Object> payload = new HashMap<>();
        payload.put("name", "John Doe");
        payload.put("salary", "200000");
        payload.put("age", "20");

        CreateEmployeeResponse response = employeeService.createEmployee(payload);

        Assertions.assertEquals(mockedResponse, response);
    }
}