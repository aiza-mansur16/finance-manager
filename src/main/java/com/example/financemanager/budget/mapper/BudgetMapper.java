package com.example.financemanager.budget.mapper;

import com.example.financemanager.budget.model.BudgetCreateDto;
import com.example.financemanager.budget.model.BudgetDto;
import com.example.financemanager.budget.model.BudgetEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BudgetMapper {

    @Mapping(ignore = true, target = "id")
    BudgetEntity toBudgetEntity(BudgetCreateDto budgetCreateDto);

    BudgetDto toBudgetDto(BudgetEntity budgetEntity);
}
