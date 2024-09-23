package com.example.financemanager.expense;

import com.example.financemanager.expense.model.ExpenseCreateDto;
import com.example.financemanager.expense.model.ExpenseDto;
import com.example.financemanager.expense.service.ExpenseService;
import com.example.financemanager.utils.model.Category;
import com.example.financemanager.utils.model.ResponseEnvelope;
import com.example.financemanager.utils.model.exception.UserNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.HttpClientErrorException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ExpenseControllerTest {

    @LocalServerPort
    int port = 0;

    @Autowired
    TestRestTemplate testRestTemplate;

    @MockBean
    private ExpenseService expenseService;

    @BeforeEach
    public void setUp() {
        testRestTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
    }

    @Test
    void createExpenseSuccess() {
        Mockito.when(expenseService.createExpense(any())).thenReturn(
                new ExpenseDto(
                        1L,
                        1L,
                        BigDecimal.valueOf(1000.00),
                        Category.GROCERY,
                        LocalDate.of(2025, Month.JANUARY, 10),
                        "Grocery purchase"
                )
        );
        var response = testRestTemplate.exchange(
                "http://localhost:" + port + "/api/expenses",
                HttpMethod.POST,
                new HttpEntity<>(new ExpenseCreateDto(
                        1L,
                        BigDecimal.valueOf(1000.00),
                        Category.GROCERY,
                        LocalDate.of(2025, Month.JANUARY, 10),
                        "Grocery purchase"
                )),
                new ParameterizedTypeReference<ResponseEnvelope<ExpenseDto>>() {
                }
        );
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void createExpenseFailure() {
        Mockito.when(expenseService.createExpense(any())).thenThrow(
                new UserNotFoundException("User not found.")
        );
        try {
            testRestTemplate.exchange(
                    "http://localhost:" + port + "/api/expenses",
                    HttpMethod.POST,
                    new HttpEntity<>(new ExpenseCreateDto(
                            1L,
                            BigDecimal.valueOf(1000.00),
                            Category.GROCERY,
                            LocalDate.of(2025, Month.JANUARY, 10),
                            "Grocery purchase"
                    )),
                    new ParameterizedTypeReference<ResponseEnvelope<ExpenseDto>>() {
                    }
            );
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, e.getStatusCode());
        }
    }

    @Test
    void getAllExpensesSuccess() {
        Mockito.when(expenseService.findAllExpenses(any())).thenReturn(
                new ResponseEnvelope<>(
                        List.of(
                                new ExpenseDto(
                                        1L,
                                        1L,
                                        BigDecimal.valueOf(1000.00),
                                        Category.GROCERY,
                                        LocalDate.of(2025, Month.JANUARY, 10),
                                        "Grocery purchase"
                                )
                        ),
                        null,
                        null));
        var response = testRestTemplate.exchange(
                "http://localhost:" + port + "/api/expenses?page=0&size=10",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ResponseEnvelope<List<ExpenseDto>>>() {
                }
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().data());
        assertEquals(1, response.getBody().data().size());
    }

    @Test
    void deleteExpenseSuccess() {
        Mockito.doNothing().when(expenseService).deleteExpense(anyLong());
        var response = testRestTemplate.exchange(
                "http://localhost:" + port + "/api/expenses/1",
                HttpMethod.DELETE,
                null,
                new ParameterizedTypeReference<ResponseEnvelope<Void>>() {
                }
        );
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void getExpenseByIdSuccess() {
        Mockito.when(expenseService.findById(anyLong())).thenReturn(
                new ExpenseDto(
                        1L,
                        1L,
                        BigDecimal.valueOf(1000.00),
                        Category.GROCERY,
                        LocalDate.of(2025, Month.JANUARY, 10),
                        "Grocery purchase"
                )
        );
        var response = testRestTemplate.exchange(
                "http://localhost:" + port + "/api/expenses/1",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ResponseEnvelope<ExpenseDto>>() {
                }
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().data().id());
    }

    @Test
    void getExpenseByIdFailure() {
        Mockito.when(expenseService.findById(anyLong())).thenThrow(
                new EntityNotFoundException("Expense not found for given id.")
        );
        try {
            testRestTemplate.exchange(
                    "http://localhost:" + port + "/api/expenses/1",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<ResponseEnvelope<ExpenseDto>>() {
                    }
            );
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
        }
    }
}
