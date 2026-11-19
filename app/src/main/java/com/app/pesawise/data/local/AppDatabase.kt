package com.app.pesawise.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

import com.app.pesawise.data.model.Budget
import com.app.pesawise.data.model.Income
import com.app.pesawise.data.model.Expense

@Database(
    entities = [
        Budget::class,
        Income::class,
        Expense::class
    ],
    version = 1,
    exportSchema = false
)

abstract class AppDatabase : RoomDatabase() {

    // Budget DAO
    abstract fun budgetDao(): BudgetDao

    // Income DAO
    abstract fun incomeDao(): IncomeDao

    // Expense DAO
    abstract fun expenseDao(): ExpenseDao


    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {

            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "pesawise_database"
                ).build()

                INSTANCE = instance

                instance
            }
        }
    }
}