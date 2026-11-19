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

    val monthNames = listOf(
        "January", "February", "March", "April",
        "May", "June", "July", "August",
        "September", "October", "November", "December"
    )

    // Current monthly budget
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

    // Budget editing states
    var selectedBudget by remember { mutableStateOf<Budget?>(null) }
    var budgetToDelete by remember { mutableStateOf<Budget?>(null) }
    var editBudgetAmount by remember { mutableStateOf("") }

    // Income editing states
    var selectedIncome by remember { mutableStateOf<Income?>(null) }
    var incomeToDelete by remember { mutableStateOf<Income?>(null) }

    var editIncomeSource by remember { mutableStateOf("") }
    var editIncomeAmount by remember { mutableStateOf("") }
    var editIncomeDescription by remember { mutableStateOf("") }

    // Expense editing states
    var selectedExpense by remember { mutableStateOf<Expense?>(null) }
    var expenseToDelete by remember { mutableStateOf<Expense?>(null) }

    var editExpenseTitle by remember { mutableStateOf("") }
    var editExpenseAmount by remember { mutableStateOf("") }
    var editExpenseCategory by remember { mutableStateOf("Food") }

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

            // SAVED BUDGET RECORDS

            Text(
                text = "Saved Budgets",
                style = MaterialTheme.typography.titleLarge
            )

            if (budgets.isEmpty()) {

                Text("No budgets have been created yet.")

            } else {

                budgets.forEach { budget ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {

                        Column(
                            modifier = Modifier.padding(12.dp)
                        ) {

                            val monthName =
                                monthNames.getOrElse(budget.month - 1) {
                                    "Unknown month"
                                }

                            Text(
                                text = "$monthName ${budget.year}",
                                style = MaterialTheme.typography.titleMedium
                            )

                            Text(
                                text = "Budget: KSh ${"%.2f".format(budget.amount)}"
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {

                                Button(
                                    onClick = {
                                        editBudgetAmount = budget.amount.toString()
                                        selectedBudget = budget
                                    }
                                ) {
                                    Text("Edit")
                                }

                                Button(
                                    onClick = {
                                        budgetToDelete = budget
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.error
                                    )
                                ) {
                                    Text("Delete")
                                }
                            }
                        }
                    }
                }
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

            // SAVED INCOME RECORDS

            Text(
                text = "Income Records",
                style = MaterialTheme.typography.titleLarge
            )

            if (incomes.isEmpty()) {

                Text("No income records available.")

            } else {

                incomes.forEach { income ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {

                        Column(
                            modifier = Modifier.padding(12.dp)
                        ) {

                            Text(
                                text = income.source,
                                style = MaterialTheme.typography.titleMedium
                            )

                            Text(
                                "Amount: KSh ${"%.2f".format(income.amount)}"
                            )

                            Text("Description: ${income.description}")

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {

                                Button(
                                    onClick = {
                                        editIncomeSource = income.source
                                        editIncomeAmount = income.amount.toString()
                                        editIncomeDescription = income.description
                                        selectedIncome = income
                                    }
                                ) {
                                    Text("Edit")
                                }

                                Button(
                                    onClick = {
                                        incomeToDelete = income
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.error
                                    )
                                ) {
                                    Text("Delete")
                                }
                            }
                        }
                    }
                }
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
                    modifier = Modifier.fillMaxWidth()
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

            // RECENT EXPENSES

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

                            Text(
                                text = expense.title,
                                style = MaterialTheme.typography.titleMedium
                            )

                            Text("Category: ${expense.category}")

                            Text(
                                "Amount: KSh ${"%.2f".format(expense.amount)}"
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {

                                Button(
                                    onClick = {
                                        editExpenseTitle = expense.title
                                        editExpenseAmount = expense.amount.toString()
                                        editExpenseCategory = expense.category
                                        selectedExpense = expense
                                    }
                                ) {
                                    Text("Edit")
                                }

                                Button(
                                    onClick = {
                                        expenseToDelete = expense
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.error
                                    )
                                ) {
                                    Text("Delete")
                                }
                            }
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

    // EDIT BUDGET DIALOG

    if (selectedBudget != null) {

        AlertDialog(
            onDismissRequest = {
                selectedBudget = null
            },

            title = {
                Text("Edit Budget")
            },

            text = {

                OutlinedTextField(
                    value = editBudgetAmount,
                    onValueChange = { editBudgetAmount = it },
                    label = { Text("Budget Amount (KSh)") }
                )
            },

            confirmButton = {

                TextButton(
                    onClick = {

                        val amount = editBudgetAmount.toDoubleOrNull()

                        if (amount != null && amount > 0) {

                            selectedBudget?.let { budget ->

                                budgetViewModel.updateBudget(
                                    budget.copy(amount = amount)
                                )
                            }

                            message = "Budget updated successfully!"
                            selectedBudget = null

                        } else {
                            message = "Enter a valid budget amount."
                        }
                    }
                ) {
                    Text("Save Changes")
                }
            },

            dismissButton = {

                TextButton(
                    onClick = {
                        selectedBudget = null
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    // DELETE BUDGET CONFIRMATION

    if (budgetToDelete != null) {

        AlertDialog(
            onDismissRequest = {
                budgetToDelete = null
            },

            title = {
                Text("Delete Budget")
            },

            text = {
                Text("Are you sure you want to delete this budget?")
            },

            confirmButton = {

                TextButton(
                    onClick = {

                        budgetToDelete?.let { budget ->
                            budgetViewModel.deleteBudget(budget.id)
                        }

                        budgetToDelete = null
                        message = "Budget deleted successfully!"
                    }
                ) {
                    Text("Delete")
                }
            },

            dismissButton = {

                TextButton(
                    onClick = {
                        budgetToDelete = null
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    // EDIT INCOME DIALOG

    if (selectedIncome != null) {

        AlertDialog(
            onDismissRequest = {
                selectedIncome = null
            },

            title = {
                Text("Edit Income")
            },

            text = {

                Column {

                    OutlinedTextField(
                        value = editIncomeSource,
                        onValueChange = { editIncomeSource = it },
                        label = { Text("Income Source") }
                    )

                    OutlinedTextField(
                        value = editIncomeAmount,
                        onValueChange = { editIncomeAmount = it },
                        label = { Text("Amount (KSh)") }
                    )

                    OutlinedTextField(
                        value = editIncomeDescription,
                        onValueChange = { editIncomeDescription = it },
                        label = { Text("Description") }
                    )
                }
            },

            confirmButton = {

                TextButton(
                    onClick = {

                        val amount = editIncomeAmount.toDoubleOrNull()

                        if (
                            editIncomeSource.isNotBlank() &&
                            amount != null &&
                            amount > 0
                        ) {

                            selectedIncome?.let { income ->

                                incomeViewModel.updateIncome(
                                    income.copy(
                                        source = editIncomeSource,
                                        amount = amount,
                                        description = editIncomeDescription
                                    )
                                )
                            }

                            message = "Income updated successfully!"
                            selectedIncome = null

                        } else {
                            message = "Enter a valid income source and amount."
                        }
                    }
                ) {
                    Text("Save Changes")
                }
            },

            dismissButton = {

                TextButton(
                    onClick = {
                        selectedIncome = null
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    // DELETE INCOME CONFIRMATION

    if (incomeToDelete != null) {

        AlertDialog(
            onDismissRequest = {
                incomeToDelete = null
            },

            title = {
                Text("Delete Income")
            },

            text = {
                Text("Are you sure you want to delete this income record?")
            },

            confirmButton = {

                TextButton(
                    onClick = {

                        incomeToDelete?.let { income ->
                            incomeViewModel.deleteIncome(income)
                        }

                        incomeToDelete = null
                        message = "Income deleted successfully!"
                    }
                ) {
                    Text("Delete")
                }
            },

            dismissButton = {

                TextButton(
                    onClick = {
                        incomeToDelete = null
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    // EDIT EXPENSE DIALOG

    if (selectedExpense != null) {

        AlertDialog(
            onDismissRequest = {
                selectedExpense = null
            },

            title = {
                Text("Edit Expense")
            },

            text = {

                Column {

                    OutlinedTextField(
                        value = editExpenseTitle,
                        onValueChange = { editExpenseTitle = it },
                        label = { Text("Expense Title") }
                    )

                    OutlinedTextField(
                        value = editExpenseAmount,
                        onValueChange = { editExpenseAmount = it },
                        label = { Text("Amount (KSh)") }
                    )

                    Text("Select Category")

                    categories.forEach { category ->

                        Row {

                            RadioButton(
                                selected = editExpenseCategory == category,
                                onClick = {
                                    editExpenseCategory = category
                                }
                            )

                            Text(
                                text = category,
                                modifier = Modifier.padding(top = 12.dp)
                            )
                        }
                    }
                }
            },

            confirmButton = {

                TextButton(
                    onClick = {

                        val amount = editExpenseAmount.toDoubleOrNull()

                        if (
                            editExpenseTitle.isNotBlank() &&
                            amount != null &&
                            amount > 0
                        ) {

                            selectedExpense?.let { expense ->

                                expenseViewModel.updateExpense(
                                    expense.copy(
                                        title = editExpenseTitle,
                                        amount = amount,
                                        category = editExpenseCategory
                                    )
                                )
                            }

                            message = "Expense updated successfully!"
                            selectedExpense = null

                        } else {
                            message = "Enter a valid expense title and amount."
                        }
                    }
                ) {
                    Text("Save Changes")
                }
            },

            dismissButton = {

                TextButton(
                    onClick = {
                        selectedExpense = null
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    // DELETE EXPENSE CONFIRMATION

    if (expenseToDelete != null) {

        AlertDialog(
            onDismissRequest = {
                expenseToDelete = null
            },

            title = {
                Text("Delete Expense")
            },

            text = {
                Text("Are you sure you want to delete this expense?")
            },

            confirmButton = {

                TextButton(
                    onClick = {

                        expenseToDelete?.let { expense ->
                            expenseViewModel.deleteExpense(expense)
                        }

                        expenseToDelete = null
                        message = "Expense deleted successfully!"
                    }
                ) {
                    Text("Delete")
                }
            },

            dismissButton = {

                TextButton(
                    onClick = {
                        expenseToDelete = null
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}