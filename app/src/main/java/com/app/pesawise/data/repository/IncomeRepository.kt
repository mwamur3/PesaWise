package com.app.pesawise.data.repository

import com.app.pesawise.data.local.IncomeDao
import com.app.pesawise.data.model.Income
import kotlinx.coroutines.flow.Flow

class IncomeRepository(
    private val incomeDao: IncomeDao
) {

    // CREATE: Record additional income
    suspend fun insertIncome(income: Income) {
        incomeDao.insertIncome(income)
    }

    // READ: Retrieve all income records
    fun getAllIncome(): Flow<List<Income>> {
        return incomeDao.getAllIncome()
    }

    // READ: Calculate total income
    fun getTotalIncome(): Flow<Double?> {
        return incomeDao.getTotalIncome()
    }

    // UPDATE: Modify an income record
    suspend fun updateIncome(income: Income) {
        incomeDao.updateIncome(income)
    }

    // DELETE: Remove an income record
    suspend fun deleteIncome(income: Income) {
        incomeDao.deleteIncome(income)
    }
}