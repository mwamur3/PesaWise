package com.app.pesawise.data.repository

import com.app.pesawise.data.local.BudgetDao
import com.app.pesawise.data.model.Budget
import kotlinx.coroutines.flow.Flow

class BudgetRepository(
    private val budgetDao: BudgetDao
) {

    // CREATE: Save a monthly budget
    suspend fun insertBudget(budget: Budget) {
        budgetDao.insertBudget(budget)
    }

    // READ: Retrieve all budgets
    fun getAllBudgets(): Flow<List<Budget>> {
        return budgetDao.getAllBudgets()
    }

    // READ: Retrieve budget for a specific month
    suspend fun getBudgetForMonth(
        month: Int,
        year: Int
    ): Budget? {
        return budgetDao.getBudgetForMonth(month, year)
    }

    // DELETE: Remove a budget
    suspend fun deleteBudget(budgetId: Int) {
        budgetDao.deleteBudget(budgetId)
    }
}