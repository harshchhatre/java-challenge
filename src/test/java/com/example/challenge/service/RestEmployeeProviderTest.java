package com.example.challenge.service;

import com.example.challenge.common.RestConnector;
import com.example.challenge.configuration.ApiConfig;
import com.example.challenge.data.TestData;
import com.example.challenge.dto.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;

@SpringBootTest
class RestEmployeeProviderTest {
    @Mock
    private RestConnector restConnector;

    @Mock
    private ApiConfig apiConfig;

    @InjectMocks
    private RestEmployeeProvider employeeProvider;

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
        mockedResponse.setData(TestData.getMockEmployees());
        Mockito.when(apiConfig.getAllEmployeesPath()).thenReturn(GET_ALL_PATH);
        Mockito.when(restConnector.get(BASE_URL + GET_ALL_PATH, GetAllEmployeeResponse.class))
                .thenReturn(new ResponseEntity<>(mockedResponse, HttpStatus.OK));

        GetAllEmployeeResponse response = employeeProvider.getAllEmployees();

        Assertions.assertEquals(2, response.getData().size());
    }

    @Test
    void testGetEmployeeById() {
        GetEmployeeResponse mockedResponse = new GetEmployeeResponse();
        mockedResponse.setStatus(STATUS_SUCCESS);
        mockedResponse.setData(new Employee("101", "Lorem Ipsum", 4737, 18, ""));
        Mockito.when(apiConfig.getByEmployeeIdPath()).thenReturn(SEARCH_BY_ID_PATH);
        Mockito.when(restConnector.get(BASE_URL + SEARCH_BY_ID_PATH + "101", GetEmployeeResponse.class))
                .thenReturn(new ResponseEntity<>(mockedResponse, HttpStatus.OK));
        GetEmployeeResponse response = employeeProvider.getEmployeeById("101");

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getData());
        Assertions.assertEquals("101", response.getData().getId());
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

        CreateEmployeeResponse response = employeeProvider.createEmployee(payload);
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getData());
        Assertions.assertEquals(STATUS_SUCCESS, response.getStatus());
    }

    @Test
    void testDeleteEmployees() {
        GetEmployeeResponse mockedResponse = new GetEmployeeResponse();
        mockedResponse.setStatus(STATUS_SUCCESS);
        mockedResponse.setData(new Employee("1", "Lorem Ipsum", 4737, 18, ""));
        Mockito.when(apiConfig.getDeleteEmployeePath()).thenReturn(DELETE_PATH);
        Mockito.when(restConnector.delete(BASE_URL + DELETE_PATH + "123", GetEmployeeResponse.class))
                .thenReturn(new ResponseEntity<>(mockedResponse, HttpStatus.OK));

        GetEmployeeResponse response = employeeProvider.deleteEmployee("123");
        Assertions.assertNotNull(response);
        Assertions.assertEquals(STATUS_SUCCESS, response.getStatus());
    }
}
