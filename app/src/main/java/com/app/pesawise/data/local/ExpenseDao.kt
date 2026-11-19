package com.app.pesawise.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete

import com.app.pesawise.data.model.Expense

import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    // CREATE: Record a new expense
    @Insert
    suspend fun insertExpense(expense: Expense)


    // READ: Retrieve all expenses
    @Query("SELECT * FROM expenses ORDER BY date DESC")
    fun getAllExpenses(): Flow<List<Expense>>


    // READ: Calculate total expenditure
    @Query("SELECT SUM(amount) FROM expenses")
    fun getTotalExpenses(): Flow<Double?>


    // READ: Retrieve expenses by category
    @Query("SELECT * FROM expenses WHERE category = :category ORDER BY date DESC")
    fun getExpensesByCategory(category: String): Flow<List<Expense>>


    // UPDATE: Modify an existing expense
    @Update
    suspend fun updateExpense(expense: Expense)


    // DELETE: Remove an expense
    @Delete
    suspend fun deleteExpense(expense: Expense)

}