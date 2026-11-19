package com.app.pesawise.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Delete
import androidx.room.Update
import com.app.pesawise.data.model.Income
import kotlinx.coroutines.flow.Flow

@Dao
interface IncomeDao {

    // CREATE: Insert new income
    @Insert
    suspend fun insertIncome(income: Income)

    // READ: Retrieve all income records
    @Query("SELECT * FROM income ORDER BY date DESC")
    fun getAllIncome(): Flow<List<Income>>

    // READ: Retrieve total additional income
    @Query("SELECT SUM(amount) FROM income")
    fun getTotalIncome(): Flow<Double?>

    // UPDATE: Modify an existing income record
    @Update
    suspend fun updateIncome(income: Income)

    // DELETE: Remove an income record
    @Delete
    suspend fun deleteIncome(income: Income)
}