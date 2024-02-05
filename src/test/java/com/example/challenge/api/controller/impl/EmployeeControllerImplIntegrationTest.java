package com.example.challenge.api.controller.impl;

import com.example.challenge.data.TestData;
import com.example.challenge.dto.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Rest API Integration tests
 */

class EmployeeControllerImplIntegrationTest {

    private String baseUrl;

    private static final String STATUS_SUCCESS = "success";
    private static final String STATUS_FAILURE = "failure";
    private static final String KEY_STATUS = "status";
    private static final String KEY_DATA = "data";
    private static final String KEY_MESSAGE = "message";

    private static final String SUCCESS_MESSAGE = "Successfully! All records has been fetched.";

    public static MockWebServer mockBackEnd;

    @BeforeAll
    static void setUp() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start();

    }

    @AfterAll
    static void tearDown() throws IOException {
        mockBackEnd.shutdown();
    }

    @BeforeEach
    void initialize() {
        baseUrl = "http://" + mockBackEnd.getHostName() + ":" + mockBackEnd.getPort();
    }

    @Test
    void testWhenGetAllEmployeesApiCalledThenShouldSuccess() throws JsonProcessingException {
        List<Employee> employees = TestData.getMockEmployees();
        GetAllEmployeeResponse mockGetAllEmployeeApiResponse = new GetAllEmployeeResponse();
        mockGetAllEmployeeApiResponse.setStatus(STATUS_SUCCESS);
        mockGetAllEmployeeApiResponse.setData(employees);
        mockGetAllEmployeeApiResponse.setMessage(SUCCESS_MESSAGE);
        ObjectMapper objectMapper = new ObjectMapper();
        mockBackEnd.enqueue(new MockResponse()
                .setBody(objectMapper.writeValueAsString(mockGetAllEmployeeApiResponse))
                .addHeader("Content-Type", "application/json"));

        WebTestClient
                .bindToServer().baseUrl(baseUrl)
                .build()
                .get()
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath(KEY_STATUS).isEqualTo(STATUS_SUCCESS)
                .jsonPath(KEY_DATA).isArray()
                .jsonPath(KEY_MESSAGE).isEqualTo(SUCCESS_MESSAGE);
    }

    @Test
    void testWhenGetEmployeesByNameSearchApiCalledThenShouldSuccess() throws JsonProcessingException {
        List<Employee> employees = TestData.getMockEmployees();
        GetAllEmployeeResponse mockGetAllEmployeeApiResponse = new GetAllEmployeeResponse();
        mockGetAllEmployeeApiResponse.setStatus(STATUS_SUCCESS);
        mockGetAllEmployeeApiResponse.setData(employees);
        mockGetAllEmployeeApiResponse.setMessage(SUCCESS_MESSAGE);
        ObjectMapper objectMapper = new ObjectMapper();
        mockBackEnd.enqueue(new MockResponse()
                .setBody(objectMapper.writeValueAsString(mockGetAllEmployeeApiResponse))
                .addHeader("Content-Type", "application/json"));

        WebTestClient.bindToServer().baseUrl(baseUrl)
                .build()
                .get()
                .uri("/search/Lorem%20Ipsum")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath(KEY_STATUS).isEqualTo(STATUS_SUCCESS)
                .jsonPath(KEY_DATA).isArray()
                .jsonPath(KEY_MESSAGE).isEqualTo(SUCCESS_MESSAGE);
    }


    @Test
    void testWhenGetEmployeeByIdApiCalledThenShouldSuccess() throws JsonProcessingException {
        Employee mockEmployee = new Employee("20", "Hawet Moss", 27075, 42, "");
        GetEmployeeResponse mockGetEmployeeApiResponse = new GetEmployeeResponse();
        mockGetEmployeeApiResponse.setStatus(STATUS_SUCCESS);
        mockGetEmployeeApiResponse.setData(mockEmployee);
        mockGetEmployeeApiResponse.setMessage(SUCCESS_MESSAGE);
        ObjectMapper objectMapper = new ObjectMapper();
        mockBackEnd.enqueue(new MockResponse()
                .setBody(objectMapper.writeValueAsString(mockGetEmployeeApiResponse))
                .addHeader("Content-Type", "application/json"));
        WebTestClient.bindToServer().baseUrl(baseUrl)
                .build()
                .get()
                .uri("/2")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath(KEY_STATUS).isEqualTo(STATUS_SUCCESS)
                .jsonPath(KEY_MESSAGE).isEqualTo(SUCCESS_MESSAGE)
                .jsonPath("data.id").isEqualTo(20)
                .jsonPath("data.employee_name").isEqualTo("Hawet Moss")
                .jsonPath("data.employee_salary").isEqualTo(27075)
                .jsonPath("data.employee_age").isEqualTo(42);

    }


    @Test
    void testWhenGetHighestSalaryOfEmployeesApiCalledThenShouldSuccess() {
        mockBackEnd.enqueue(new MockResponse()
                .setBody(String.valueOf(43000))
                .addHeader("Content-Type", "application/json"));

        WebTestClient
                .bindToServer().baseUrl(baseUrl)
                .build()
                .get()
                .uri("/highestSalary")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Integer.class).isEqualTo(43000);
    }


    @Test
    void testWhenGetTopTenHighestEarningEmployeeNamesApiCalledThenShouldSuccess() throws JsonProcessingException {
        List<String> mockEmployeeNames = new ArrayList<>();
        mockEmployeeNames.add("Shane Warn");
        mockEmployeeNames.add("Alex lehman");
        mockEmployeeNames.add("Chad Devis");
        mockEmployeeNames.add("Mahendrasingh Dhoni");
        mockEmployeeNames.add("Margaret Wales");
        mockEmployeeNames.add("Robert Downey");
        mockEmployeeNames.add("Mark Wood");
        mockEmployeeNames.add("James Thompson");
        mockEmployeeNames.add("Sachin Tendulkar");
        mockEmployeeNames.add("Enzo Ferrari");
        ObjectMapper objectMapper = new ObjectMapper();
        mockBackEnd.enqueue(new MockResponse()
                .setBody(objectMapper.writeValueAsString(mockEmployeeNames))
                .addHeader("Content-Type", "application/json"));

        WebTestClient
                .bindToServer().baseUrl(baseUrl)
                .build()
                .get()
                .uri("/topTenHighestEarningEmployeeNames")
                .exchange()
                .expectStatus().isOk()
                .expectBody(List.class).isEqualTo(mockEmployeeNames);
    }


    @Test
    void testWhenCreateEmployeeApiCalledThenShouldCreateNewEmployeeRecord() throws JsonProcessingException {
        CreateEmployeeRequest createEmployee = new CreateEmployeeRequest("200", "John Doe", 200000, 20);
        CreateEmployeeResponse createEmployeeResponse = new CreateEmployeeResponse();
        createEmployeeResponse.setStatus(STATUS_SUCCESS);
        createEmployeeResponse.setData(createEmployee);
        createEmployeeResponse.setMessage(SUCCESS_MESSAGE);
        ObjectMapper objectMapper = new ObjectMapper();
        mockBackEnd.enqueue(new MockResponse()
                .setBody(objectMapper.writeValueAsString(createEmployeeResponse))
                .addHeader("Content-Type", "application/json")
                .setStatus(HttpStatus.CREATED.toString())
                .setResponseCode(200));

        Map<String, Object> payload = new HashMap<>();
        payload.put("name", "John Doe");
        payload.put("salary", "200000");
        payload.put("age", "20");
        WebTestClient
                .bindToServer().baseUrl(baseUrl)
                .build()
                .post()
                .bodyValue(payload)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("status").isEqualTo(STATUS_SUCCESS)
                .jsonPath("data.name").isEqualTo("John Doe")
                .jsonPath("data.salary").isEqualTo("200000")
                .jsonPath("data.age").isEqualTo("20")
                .jsonPath("message").isEqualTo(SUCCESS_MESSAGE);
    }

    @Test
    void testWhenDeleteEmployeeByIdApiIsCalledThenShouldDeleteEmployeeRecord() throws JsonProcessingException {
        String deletedEmployeeName = "John Doe";
        ObjectMapper objectMapper = new ObjectMapper();
        mockBackEnd.enqueue(new MockResponse()
                .setBody(objectMapper.writeValueAsString(deletedEmployeeName))
                .addHeader("Content-Type", "application/json"));

        WebTestClient.bindToServer().baseUrl(baseUrl)
                .build()
                .delete()
                .uri("/24")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Object.class).isEqualTo(deletedEmployeeName);

    }
}