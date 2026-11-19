package com.app.pesawise.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

import androidx.compose.material3.*

import androidx.compose.runtime.*

import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import com.app.pesawise.data.model.Budget
import com.app.pesawise.data.model.Income
import com.app.pesawise.data.model.Expense

import com.app.pesawise.viewmodel.BudgetViewModel
import com.app.pesawise.viewmodel.IncomeViewModel
import com.app.pesawise.viewmodel.ExpenseViewModel

import java.util.Calendar

@Composable
fun HomeScreen(
    budgetViewModel: BudgetViewModel,
    incomeViewModel: IncomeViewModel,
    expenseViewModel: ExpenseViewModel
) {

    // Observe database information
    val budgets by budgetViewModel.budgets.collectAsState()

    val incomes by incomeViewModel.incomes.collectAsState()

    val expenses by expenseViewModel.expenses.collectAsState()

    val totalIncome by incomeViewModel.totalIncome.collectAsState()

    val totalExpenses by expenseViewModel.totalExpenses.collectAsState()

    // Current month and year
    val calendar = Calendar.getInstance()

    val currentMonth = calendar.get(Calendar.MONTH) + 1

    val currentYear = calendar.get(Calendar.YEAR)

    // Retrieve current month's budget
    val currentBudget = budgets.firstOrNull {
        it.month == currentMonth && it.year == currentYear
    }

    val budgetAmount = currentBudget?.amount ?: 0.0

    val availableBalance =
        budgetAmount + (totalIncome ?: 0.0) - (totalExpenses ?: 0.0)

    // Input fields
    var budgetInput by remember { mutableStateOf("") }

    var incomeInput by remember { mutableStateOf("") }

    var incomeSource by remember { mutableStateOf("") }

    var expenseTitle by remember { mutableStateOf("") }

    var expenseAmount by remember { mutableStateOf("") }

    var expenseCategory by remember { mutableStateOf("Food") }

    var message by remember { mutableStateOf("") }

    val categories = listOf(
        "Food",
        "Transport",
        "Accommodation",
        "Education",
        "Shopping",
        "Entertainment",
        "Personal Care",
        "Other"
    )

    Scaffold { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {

            // APPLICATION TITLE

            Text(
                text = "PesaWise",
                style = MaterialTheme.typography.headlineLarge
            )

            Text(
                text = "Smart budgeting. Better financial decisions.",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(24.dp))


            // FINANCIAL DASHBOARD

            Text(
                text = "Financial Overview",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    Text("Monthly Budget")

                    Text(
                        text = "KSh ${"%.2f".format(budgetAmount)}",
                        style = MaterialTheme.typography.headlineMedium
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Total Additional Income")

                    Text("KSh ${"%.2f".format(totalIncome ?: 0.0)}")

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Total Expenses")

                    Text("KSh ${"%.2f".format(totalExpenses ?: 0.0)}")

                    Spacer(modifier = Modifier.height(12.dp))

                    HorizontalDivider()

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Available Balance")

                    Text(
                        text = "KSh ${"%.2f".format(availableBalance)}",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            }


            Spacer(modifier = Modifier.height(28.dp))


            // SET MONTHLY BUDGET

            Text(
                text = "Set Monthly Budget",
                style = MaterialTheme.typography.titleLarge
            )

            OutlinedTextField(
                value = budgetInput,
                onValueChange = { budgetInput = it },
                label = { Text("Enter budget amount (KSh)") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {

                    val amount = budgetInput.toDoubleOrNull()

                    if (amount != null && amount > 0) {

                        budgetViewModel.addBudget(
                            Budget(
                                amount = amount,
                                month = currentMonth,
                                year = currentYear
                            )
                        )

                        budgetInput = ""
                        message = "Budget saved successfully!"

                    } else {
                        message = "Enter a valid budget amount."
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Budget")
            }


            Spacer(modifier = Modifier.height(24.dp))


            // ADDITIONAL INCOME

            Text(
                text = "Record Additional Income",
                style = MaterialTheme.typography.titleLarge
            )

            OutlinedTextField(
                value = incomeSource,
                onValueChange = { incomeSource = it },
                label = { Text("Income Source") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = incomeInput,
                onValueChange = { incomeInput = it },
                label = { Text("Amount (KSh)") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {

                    val amount = incomeInput.toDoubleOrNull()

                    if (
                        incomeSource.isNotBlank() &&
                        amount != null &&
                        amount > 0
                    ) {

                        incomeViewModel.addIncome(
                            Income(
                                amount = amount,
                                source = incomeSource,
                                description = "Additional income"
                            )
                        )

                        incomeInput = ""
                        incomeSource = ""

                        message = "Income recorded successfully!"

                    } else {
                        message = "Enter a valid income source and amount."
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add Income")
            }


            Spacer(modifier = Modifier.height(24.dp))


            // RECORD EXPENSE

            Text(
                text = "Record Expense",
                style = MaterialTheme.typography.titleLarge
            )

            OutlinedTextField(
                value = expenseTitle,
                onValueChange = { expenseTitle = it },
                label = { Text("Expense Title") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = expenseAmount,
                onValueChange = { expenseAmount = it },
                label = { Text("Amount (KSh)") },
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "Expense Category",
                style = MaterialTheme.typography.titleMedium
            )

            categories.forEach { category ->

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {

                    RadioButton(
                        selected = expenseCategory == category,
                        onClick = {
                            expenseCategory = category
                        }
                    )

                    Text(
                        text = category,
                        modifier = Modifier.padding(top = 12.dp)
                    )
                }
            }

            Button(
                onClick = {

                    val amount = expenseAmount.toDoubleOrNull()

                    if (
                        expenseTitle.isNotBlank() &&
                        amount != null &&
                        amount > 0
                    ) {

                        expenseViewModel.addExpense(
                            Expense(
                                title = expenseTitle,
                                amount = amount,
                                category = expenseCategory,
                                description = expenseTitle
                            )
                        )

                        expenseTitle = ""
                        expenseAmount = ""

                        message = "Expense recorded successfully!"

                    } else {
                        message = "Enter a valid expense title and amount."
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Record Expense")
            }


            Spacer(modifier = Modifier.height(24.dp))


            // FINANCIAL RECORDS

            Text(
                text = "Recent Expenses",
                style = MaterialTheme.typography.titleLarge
            )

            if (expenses.isEmpty()) {

                Text("No expenses recorded yet.")

            } else {

                expenses.forEach { expense ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {

                        Column(
                            modifier = Modifier.padding(12.dp)
                        ) {

                            Text(expense.title)

                            Text("Category: ${expense.category}")

                            Text(
                                "Amount: KSh ${"%.2f".format(expense.amount)}"
                            )
                        }
                    }
                }
            }


            Spacer(modifier = Modifier.height(16.dp))


            if (message.isNotBlank()) {

                Text(
                    text = message,
                    color = MaterialTheme.colorScheme.primary
                )
            }

        }
    }
}