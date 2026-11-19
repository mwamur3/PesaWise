package com.app.pesawise.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

import com.app.pesawise.data.model.Budget

import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao {

    // CREATE: Insert a new budget
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBudget(budget: Budget)

    // READ: Retrieve all budgets
    @Query("SELECT * FROM budgets ORDER BY year DESC, month DESC")
    fun getAllBudgets(): Flow<List<Budget>>

    // READ: Retrieve a budget for a specific month
    @Query("SELECT * FROM budgets WHERE month = :month AND year = :year LIMIT 1")
    suspend fun getBudgetForMonth(
        month: Int,
        year: Int
    ): Budget?

    // UPDATE: Modify an existing budget
    @Update
    suspend fun updateBudget(budget: Budget)

    // DELETE: Remove a budget
    @Query("DELETE FROM budgets WHERE id = :budgetId")
    suspend fun deleteBudget(budgetId: Int)
}