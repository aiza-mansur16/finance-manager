package com.example.financemanager.expense.controller;

import com.example.financemanager.expense.model.ExpenseCreateDto;
import com.example.financemanager.expense.model.ExpenseDto;
import com.example.financemanager.expense.model.ExpenseQueryDto;
import com.example.financemanager.expense.service.ExpenseService;
import com.example.financemanager.utils.model.ResponseEnvelope;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@Validated
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @Operation(summary = "Add a new expense", description = "Create a new expense record",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Expense created successfully",
                            content = @Content(schema = @Schema(implementation = ResponseEnvelope.class))),

                    @ApiResponse(responseCode = "400", description = "Invalid request",
                            content = @Content(schema = @Schema(implementation = ResponseEnvelope.class))),

                    @ApiResponse(responseCode = "500", description = "Internal server error",
                            content = @Content(schema = @Schema(implementation = ResponseEnvelope.class)))
            }
    )
    @PostMapping
    public ResponseEntity<ResponseEnvelope<ExpenseDto>> addExpense(@RequestBody @Valid ExpenseCreateDto expense) {
        return new ResponseEntity<>(new ResponseEnvelope<>(expenseService.createExpense(expense), null, null),
                HttpStatus.CREATED);
    }

    @Operation(summary = "Get all expenses", description = "Retrieve all expense records",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Expenses retrieved successfully",
                            content = @Content(schema = @Schema(implementation = ResponseEnvelope.class))),
            }
    )
    @GetMapping
    public ResponseEntity<ResponseEnvelope<List<ExpenseDto>>> getAllExpenses(@Valid ExpenseQueryDto expenseQueryDto) {
        return new ResponseEntity<>(expenseService.findAllExpenses(expenseQueryDto), HttpStatus.OK);
    }

    @Operation(summary = "Get expense by id", description = "Retrieve expense by id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Expense found successfully",
                            content = @Content(schema = @Schema(implementation = ResponseEnvelope.class))),
                    @ApiResponse(responseCode = "404", description = "Expense not found")
            }
    )
    @GetMapping(value = "/{id}")
    public ResponseEntity<ResponseEnvelope<ExpenseDto>> getExpenseById(@PathVariable Long id) {
        return new ResponseEntity<>(new ResponseEnvelope<>(expenseService.findById(id), null, null),
                HttpStatus.OK);
    }

    @Operation(summary = "Delete expense", description = "Delete expense by id",
            responses = {
            @ApiResponse(responseCode = "204", description = "Expense deleted successfully"),
            }
    )
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<ResponseEnvelope<Void>> deleteExpense(@PathVariable Long id) {
        expenseService.deleteExpense(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
