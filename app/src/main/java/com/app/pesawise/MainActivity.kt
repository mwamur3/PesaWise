package com.app.pesawise

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel

import com.app.pesawise.data.local.AppDatabase
import com.app.pesawise.data.repository.BudgetRepository
import com.app.pesawise.data.repository.IncomeRepository
import com.app.pesawise.data.repository.ExpenseRepository
import com.app.pesawise.di.PesaWiseViewModelFactory

import com.app.pesawise.viewmodel.BudgetViewModel
import com.app.pesawise.viewmodel.IncomeViewModel
import com.app.pesawise.viewmodel.ExpenseViewModel

import com.app.pesawise.ui.navigation.PesaWiseNavigation
import com.app.pesawise.ui.theme.PesaWiseTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = AppDatabase.getDatabase(applicationContext)

        val budgetRepository = BudgetRepository(database.budgetDao())
        val incomeRepository = IncomeRepository(database.incomeDao())
        val expenseRepository = ExpenseRepository(database.expenseDao())

        val factory = PesaWiseViewModelFactory(
            budgetRepository,
            incomeRepository,
            expenseRepository
        )

        setContent {

            PesaWiseTheme {

                val budgetViewModel: BudgetViewModel =
                    viewModel(factory = factory)

                val incomeViewModel: IncomeViewModel =
                    viewModel(factory = factory)

                val expenseViewModel: ExpenseViewModel =
                    viewModel(factory = factory)

                PesaWiseNavigation(
                    budgetViewModel = budgetViewModel,
                    incomeViewModel = incomeViewModel,
                    expenseViewModel = expenseViewModel
                )
            }
        }
    }
}