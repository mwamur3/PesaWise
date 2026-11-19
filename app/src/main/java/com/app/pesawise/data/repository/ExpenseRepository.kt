package com.app.pesawise.data.repository

import com.app.pesawise.data.local.ExpenseDao
import com.app.pesawise.data.model.Expense
import kotlinx.coroutines.flow.Flow

class ExpenseRepository(
    private val expenseDao: ExpenseDao
) {

    // CREATE: Record a new expense
    suspend fun insertExpense(expense: Expense) {
        expenseDao.insertExpense(expense)
    }

    // READ: Retrieve all expenses
    fun getAllExpenses(): Flow<List<Expense>> {
        return expenseDao.getAllExpenses()
    }

    // READ: Calculate total expenditure
    fun getTotalExpenses(): Flow<Double?> {
        return expenseDao.getTotalExpenses()
    }

    // READ: Retrieve expenses by category
    fun getExpensesByCategory(
        category: String
    ): Flow<List<Expense>> {
        return expenseDao.getExpensesByCategory(category)
    }

    // UPDATE: Modify an existing expense
    suspend fun updateExpense(expense: Expense) {
        expenseDao.updateExpense(expense)
    }

    // DELETE: Remove an expense
    suspend fun deleteExpense(expense: Expense) {
        expenseDao.deleteExpense(expense)
    }
}