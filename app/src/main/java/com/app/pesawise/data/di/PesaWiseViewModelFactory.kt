package com.app.pesawise.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import com.app.pesawise.data.repository.BudgetRepository
import com.app.pesawise.data.repository.IncomeRepository
import com.app.pesawise.data.repository.ExpenseRepository

import com.app.pesawise.viewmodel.BudgetViewModel
import com.app.pesawise.viewmodel.IncomeViewModel
import com.app.pesawise.viewmodel.ExpenseViewModel

class PesaWiseViewModelFactory(
    private val budgetRepository: BudgetRepository,
    private val incomeRepository: IncomeRepository,
    private val expenseRepository: ExpenseRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        return when {

            modelClass.isAssignableFrom(BudgetViewModel::class.java) ->
                BudgetViewModel(budgetRepository) as T

            modelClass.isAssignableFrom(IncomeViewModel::class.java) ->
                IncomeViewModel(incomeRepository) as T

            modelClass.isAssignableFrom(ExpenseViewModel::class.java) ->
                ExpenseViewModel(expenseRepository) as T

            else -> throw IllegalArgumentException(
                "Unknown ViewModel class"
            )
        }
    }
}