package com.example.financemanager.budget;

import com.example.financemanager.budget.model.BudgetCreateDto;
import com.example.financemanager.budget.model.BudgetDto;
import com.example.financemanager.budget.model.BudgetPatchDto;
import com.example.financemanager.budget.service.BudgetService;
import com.example.financemanager.utils.model.Category;
import com.example.financemanager.utils.model.ResponseEnvelope;
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
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class BudgetControllerTest {

    @LocalServerPort
    int port = 0;

    @Autowired
    TestRestTemplate testRestTemplate;

    @MockBean
    private BudgetService budgetService;

    @BeforeEach
    public void setUp() {
        testRestTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
    }

    @Test
    void createBudgetSuccess() {
        Mockito.when(budgetService.createBudget(any()))
                .thenReturn(new BudgetDto(
                        1L,
                        1L,
                        Category.GROCERY,
                        BigDecimal.valueOf(10000.00),
                        Month.JANUARY,
                        2025,
                        "Grocery Budget"
                ));
        var response = testRestTemplate.exchange(
                "http://localhost:" + port + "/api/budgets",
                HttpMethod.POST,
                new HttpEntity<>(new BudgetCreateDto(
                        1L,
                        Category.GROCERY,
                        BigDecimal.valueOf(10000.00),
                        Month.JANUARY,
                        2025,
                        "Grocery Budget"
                )),
                new ParameterizedTypeReference<ResponseEnvelope<BudgetDto>>() {
                }
        );
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void createBudgetFailure() {
        Mockito.when(budgetService.createBudget(any()))
                .thenThrow(new IllegalArgumentException(
                                "Budget already exists for this user with given month, year and category."
                        )
                );
        try {
            testRestTemplate.exchange(
                    "http://localhost:" + port + "/api/budgets",
                    HttpMethod.POST,
                    new HttpEntity<>(new BudgetCreateDto(
                            1L,
                            Category.GROCERY,
                            BigDecimal.valueOf(10000.00),
                            Month.JANUARY,
                            2025,
                            "Grocery Budget"
                    )),
                    ResponseEnvelope.class
            );
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
        }
    }

    @Test
    void getAllBudgetsSuccess() {
        Mockito.when(budgetService.findAllBudgets(any()))
               .thenReturn(new ResponseEnvelope<>(
                        List.of(
                                new BudgetDto(
                                        1L,
                                        1L,
                                        Category.GROCERY,
                                        BigDecimal.valueOf(10000.00),
                                        Month.JANUARY,
                                        2025,
                                        "Grocery Budget"
                                )
                        ),
                       null,
                       null
                ));
        var response = testRestTemplate.exchange(
                "http://localhost:" + port + "/api/budgets?page=0&size=10",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ResponseEnvelope<List<BudgetDto>>>() {
                }
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().data());
        assertEquals(1, response.getBody().data().size());
    }

    @Test
    void patchBudgetSuccess() {
        Mockito.when(budgetService.patchBudget(any(), any()))
               .thenReturn(new BudgetDto(
                        1L,
                        1L,
                        Category.GROCERY,
                        BigDecimal.valueOf(11000.00),
                        Month.JANUARY,
                        2025,
                        "Grocery Budget"
                ));
        var response = testRestTemplate.exchange(
                "http://localhost:" + port + "/api/budgets/1",
                HttpMethod.PATCH,
                new HttpEntity<>(new BudgetPatchDto(
                        BigDecimal.valueOf(11000.00),
                        null
                )),
                new ParameterizedTypeReference<ResponseEnvelope<BudgetDto>>() {
                }
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void patchBudgetFailure() {
        Mockito.when(budgetService.patchBudget(any(), any()))
               .thenThrow(new EntityNotFoundException("Budget not found for given id."));
        try {
            testRestTemplate.exchange(
                    "http://localhost:" + port + "/api/budgets/1",
                    HttpMethod.PATCH,
                    new HttpEntity<>(new BudgetPatchDto(
                            BigDecimal.valueOf(11000.00),
                            null
                    )),
                    ResponseEnvelope.class
            );
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
        }
    }

    @Test
    void deleteBudgetSuccess() {
        Mockito.doNothing().when(budgetService).deleteBudget(any());
        var response = testRestTemplate.exchange(
                "http://localhost:" + port + "/api/budgets/1",
                HttpMethod.DELETE,
                null,
                ResponseEnvelope.class
        );
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void getBudgetByIdSuccess() {
        Mockito.when(budgetService.findById(any()))
               .thenReturn(new BudgetDto(
                        1L,
                        1L,
                        Category.GROCERY,
                        BigDecimal.valueOf(10000.00),
                        Month.JANUARY,
                        2025,
                        "Grocery Budget"
                ));
        var response = testRestTemplate.exchange(
                "http://localhost:" + port + "/api/budgets/1",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ResponseEnvelope<BudgetDto>>() {
                }
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getBudgetByIdFailure() {
        Mockito.when(budgetService.findById(any()))
               .thenThrow(new EntityNotFoundException("Budget not found for given id."));
        try {
            testRestTemplate.exchange(
                    "http://localhost:" + port + "/api/budgets/1",
                    HttpMethod.GET,
                    null,
                    ResponseEnvelope.class
            );
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
        }
    }

}
