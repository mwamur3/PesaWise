package com.app.pesawise.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.app.pesawise.data.model.Budget
import com.app.pesawise.data.repository.BudgetRepository

import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

import kotlinx.coroutines.launch

class BudgetViewModel(
    private val repository: BudgetRepository
) : ViewModel() {

    // READ: Observe all budgets
    val budgets = repository.getAllBudgets()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    // CREATE OR UPDATE: Save a budget without duplicating its month
    fun addBudget(budget: Budget) {

        viewModelScope.launch {

            val existingBudget = repository.getBudgetForMonth(
                budget.month,
                budget.year
            )

            if (existingBudget != null) {

                repository.updateBudget(
                    existingBudget.copy(
                        amount = budget.amount
                    )
                )

            } else {

                repository.insertBudget(budget)
            }
        }
    }

    // UPDATE: Modify an existing budget
    fun updateBudget(budget: Budget) {

        viewModelScope.launch {
            repository.updateBudget(budget)
        }
    }

    // DELETE: Remove a budget
    fun deleteBudget(id: Int) {

        viewModelScope.launch {
            repository.deleteBudget(id)
        }
    }
}