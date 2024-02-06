package com.rq.challenge.api.aspect;

import com.rq.challenge.data.EmployeeFixtures;
import com.rq.challenge.dto.Employee;
import com.rq.challenge.dto.GetAllEmployeeResponse;
import com.rq.challenge.exceptions.RestClientException;
import com.rq.challenge.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class RestClientExceptionHandlerTest {

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private MockMvc mockMvc;
    private static final String STATUS_FAILED = "Failed";

    @Test
    void testRestClientExceptionHandler() throws Exception {
        List<Employee> employeeList = EmployeeFixtures.getMockEmployees(20);
        GetAllEmployeeResponse getAllEmployeeResponse = new GetAllEmployeeResponse();
        getAllEmployeeResponse.setData(employeeList);

        when(employeeService.getAllEmployees()).thenThrow(new RestClientException("Mocked Exception"));

        mockMvc.perform(MockMvcRequestBuilders.get("/employees").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(STATUS_FAILED)))
                .andDo(print());
    }
}
