package com.rq.challenge.api.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rq.challenge.api.controller.EmployeeControllerImpl;
import com.rq.challenge.data.EmployeeFixtures;
import com.rq.challenge.dto.*;
import com.rq.challenge.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(EmployeeControllerImpl.class)
class EmployeeControllerImplTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Test
    void getAllEmployees() throws Exception {
        List<Employee> employeeList = EmployeeFixtures.getMockEmployees(15);
        GetAllEmployeeResponse getAllEmployeeResponse = new GetAllEmployeeResponse();
        getAllEmployeeResponse.setData(employeeList);

        when(employeeService.getAllEmployees()).thenReturn(getAllEmployeeResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/employees").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(15)))
                .andDo(print());
    }

    @Test
    void getEmployeesByNameSearch() throws Exception {
        List<Employee> employeeList = EmployeeFixtures.getMockEmployees(15);
        GetAllEmployeeResponse getAllEmployeeResponse = new GetAllEmployeeResponse();
        getAllEmployeeResponse.setData(employeeList);

        when(employeeService.getEmployeesByName(anyString())).thenReturn(getAllEmployeeResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/employees/search/{searchString}", "john").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(15)))
                .andDo(print());
    }

    @Test
    void getEmployeeById() throws Exception {
        Employee mockEmployee = new Employee("1", "Lorem Ipsum", 4737, 18, "");
        GetEmployeeResponse employeeResponse = new GetEmployeeResponse();
        employeeResponse.setData(mockEmployee);

        when(employeeService.getEmployeeById(anyString())).thenReturn(employeeResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/employees/{id}}", "101").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id", is("1")))
                .andExpect(jsonPath("$.data.employee_age", is(18)))
                .andExpect(jsonPath("$.data.employee_name", is("Lorem Ipsum")))
                .andExpect(jsonPath("$.data.employee_salary", is(4737)))
                .andExpect(jsonPath("$.data.profile_image", isEmptyString()))
                .andDo(print());
    }

    @Test
    void getHighestSalaryOfEmployees() throws Exception {

        Integer highestSalary = 1000;

        when(employeeService.getHighestSalary()).thenReturn(highestSalary);

        mockMvc.perform(MockMvcRequestBuilders.get("/employees/highestSalary").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(1000)))
                .andDo(print());
    }

    @Test
    void getTopTenHighestEarningEmployeeNames() throws Exception {
        List<String> highestEarningEmployees = Arrays.asList("John", "Jeesy", "Tommy");
        when(employeeService.getTenHighestEarningEmployeesNames()).thenReturn(highestEarningEmployees);

        mockMvc.perform(MockMvcRequestBuilders.get("/employees/topTenHighestEarningEmployeeNames").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andDo(print());
    }

    @Test
    void createEmployee() throws Exception {
        CreateEmployeeRequest createEmployee = new CreateEmployeeRequest("200", "John Doe", 20, 200000);
        CreateEmployeeResponse createEmployeeResponse = new CreateEmployeeResponse();
        createEmployeeResponse.setData(createEmployee);

        when(employeeService.createEmployee(anyMap())).thenReturn(createEmployeeResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/employees").contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(createEmployee)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void deleteEmployeeById() throws Exception {
        String deletedEmployee = "John Doe";

        when(employeeService.deleteEmployeeById(anyString())).thenReturn(deletedEmployee);

        mockMvc.perform(MockMvcRequestBuilders.delete("/employees/{id}", "101").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is("John Doe")))
                .andDo(print());
    }
}
