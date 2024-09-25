package com.example.financemanager.budget.controller;

import com.example.financemanager.budget.model.BudgetCreateDto;
import com.example.financemanager.budget.model.BudgetDto;
import com.example.financemanager.budget.model.BudgetPatchDto;
import com.example.financemanager.budget.model.BudgetQueryDto;
import com.example.financemanager.budget.service.BudgetService;
import com.example.financemanager.utils.model.ResponseEnvelope;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/budgets")
@Validated
@edu.umd.cs.findbugs.annotations.SuppressFBWarnings(value = {
    "EI_EXPOSE_REP2"
}, justification = "It is only used as an entry point for budget api requests")
public class BudgetController {
  private final BudgetService budgetService;

  public BudgetController(BudgetService budgetService) {
    this.budgetService = budgetService;
  }

  @Operation(
      summary = "Add a new budget",
      description = "Create a new budget with the provided details.",
      operationId = "addbudget",
      responses = {
          @ApiResponse(responseCode = "201", description = "Budget created successfully",
              content = @Content(schema = @Schema(implementation = ResponseEnvelope.class))),
          @ApiResponse(responseCode = "400", description = "Invalid request",
              content = @Content(schema = @Schema(implementation = ResponseEnvelope.class)))
      }
  )
  @PostMapping
  public ResponseEntity<ResponseEnvelope<BudgetDto>> addBudget(@RequestBody @Valid BudgetCreateDto budget) {
    return new ResponseEntity<>(new ResponseEnvelope<>(budgetService.createBudget(budget), null, null),
        HttpStatus.CREATED);
  }

  @Operation(
      summary = "Get all budgets",
      description = "Get all budgets based on provided query parameters.",
      operationId = "getAllBudgets",
      responses = {
          @ApiResponse(responseCode = "200", description = "Budgets retrieved successfully",
              content = @Content(schema = @Schema(implementation = ResponseEnvelope.class)))
      }
  )
  @GetMapping
  public ResponseEntity<ResponseEnvelope<List<BudgetDto>>> getAllBudgets(@Valid BudgetQueryDto budgetQuery) {
    return new ResponseEntity<>(budgetService.findAllBudgets(budgetQuery), HttpStatus.OK);
  }

  @Operation(summary = "Get budget by id", description = "Retrieve budget by id",
      responses = {
          @ApiResponse(responseCode = "200", description = "Budget retrieved successfully",
              content = @Content(schema = @Schema(implementation = ResponseEnvelope.class))),
          @ApiResponse(responseCode = "404", description = "Budget not found",
              content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
      }
  )
  @GetMapping("/{id}")
  public ResponseEntity<ResponseEnvelope<BudgetDto>> getBudgetById(@PathVariable Long id) {
    return new ResponseEntity<>(new ResponseEnvelope<>(budgetService.findById(id), null, null),
        HttpStatus.OK);
  }

  @Operation(
      summary = "Update a budget",
      description = "Update the details of an existing budget.",
      operationId = "updateBudget",
      responses = {
          @ApiResponse(responseCode = "200", description = "Budget updated successfully",
              content = @Content(schema = @Schema(implementation = ResponseEnvelope.class))),
          @ApiResponse(responseCode = "400", description = "Invalid request",
              content = @Content(schema = @Schema(implementation = ResponseEnvelope.class))),
          @ApiResponse(responseCode = "404", description = "Budget not found",
              content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
      }
  )
  @PatchMapping("/{id}")
  public ResponseEntity<ResponseEnvelope<BudgetDto>> patchBudget(@PathVariable Long id,
                                                                 @RequestBody @Valid BudgetPatchDto budget) {
    return new ResponseEntity<>(new ResponseEnvelope<>(budgetService.patchBudget(id, budget), null, null),
        HttpStatus.OK);
  }

  @Operation(
      summary = "Delete a budget",
      description = "Delete an existing budget.",
      operationId = "deleteBudget",
      responses = {
          @ApiResponse(responseCode = "204", description = "Budget deleted successfully")
      }
  )
  @DeleteMapping("/{id}")
  public ResponseEntity<ResponseEnvelope<Void>> deleteBudget(@PathVariable Long id) {
    budgetService.deleteBudget(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
