package com.example.financemanager.expense.mapper;

import com.example.financemanager.expense.model.ExpenseCreateDto;
import com.example.financemanager.expense.model.ExpenseDto;
import com.example.financemanager.expense.model.ExpenseEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ExpenseMapper {

  @Mapping(target = "date", expression = "java(LocalDate.of(expense.getYear(), expense.getMonth(), expense.getDayOfMonth()))")
  ExpenseDto toExpenseDto(ExpenseEntity expense);

  @Mapping(ignore = true, target = "id")
  @Mapping(target = "dayOfMonth", expression = "java(expense.date().getDayOfMonth())")
  @Mapping(target = "month", expression = "java(expense.date().getMonth())")
  @Mapping(target = "year", expression = "java(expense.date().getYear())")
  ExpenseEntity toExpenseEntity(ExpenseCreateDto expense);
}
