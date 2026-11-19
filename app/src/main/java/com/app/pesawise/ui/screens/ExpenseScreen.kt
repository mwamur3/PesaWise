package com.app.pesawise.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

import com.app.pesawise.data.model.Expense
import com.app.pesawise.viewmodel.ExpenseViewModel
import com.app.pesawise.ui.theme.*

@Composable
fun ExpenseScreen(expenseViewModel: ExpenseViewModel) {

    val expenses by expenseViewModel.expenses.collectAsState()
    val totalExpenses by expenseViewModel.totalExpenses.collectAsState()

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

    var expenseTitle by remember { mutableStateOf("") }
    var expenseAmount by remember { mutableStateOf("") }
    var expenseCategory by remember { mutableStateOf("Food") }
    var expenseDescription by remember { mutableStateOf("") }

    var message by remember { mutableStateOf("") }

    var selectedExpense by remember { mutableStateOf<Expense?>(null) }
    var expenseToDelete by remember { mutableStateOf<Expense?>(null) }

    var editExpenseTitle by remember { mutableStateOf("") }
    var editExpenseAmount by remember { mutableStateOf("") }
    var editExpenseCategory by remember { mutableStateOf("Food") }
    var editExpenseDescription by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {

        Text(
            "Expense Management",
            style = MaterialTheme.typography.headlineLarge,
            color = PrimaryText
        )

        Text(
            "Track your spending and understand where your money goes.",
            color = SecondaryText
        )

        Spacer(modifier = Modifier.height(24.dp))

        // TOTAL EXPENSES CARD

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = ErrorRed
            )
        ) {
            Column(modifier = Modifier.padding(20.dp)) {

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Filled.ReceiptLong,
                        contentDescription = null,
                        tint = White
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        "TOTAL RECORDED EXPENSES",
                        color = White,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    "KSh ${"%.2f".format(totalExpenses ?: 0.0)}",
                    style = MaterialTheme.typography.headlineLarge,
                    color = White,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // RECORD EXPENSE

        Text(
            "Record New Expense",
            style = MaterialTheme.typography.titleLarge,
            color = PrimaryText
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = expenseTitle,
            onValueChange = { expenseTitle = it },
            label = { Text("Expense Title") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = expenseAmount,
            onValueChange = { expenseAmount = it },
            label = { Text("Amount (KSh)") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            ),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            "Expense Category",
            style = MaterialTheme.typography.titleMedium,
            color = PrimaryText
        )

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = AppSurface
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                categories.forEach { category ->

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = expenseCategory == category,
                            onClick = { expenseCategory = category },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = BinanceYellow
                            )
                        )

                        Text(
                            category,
                            color = PrimaryText
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = expenseDescription,
            onValueChange = { expenseDescription = it },
            label = { Text("Description (Optional)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                val amount = expenseAmount.toDoubleOrNull()

                if (expenseTitle.isNotBlank() && amount != null && amount > 0) {

                    expenseViewModel.addExpense(
                        Expense(
                            title = expenseTitle.trim(),
                            amount = amount,
                            category = expenseCategory,
                            description = expenseDescription.trim()
                                .ifBlank { expenseTitle.trim() }
                        )
                    )

                    expenseTitle = ""
                    expenseAmount = ""
                    expenseCategory = "Food"
                    expenseDescription = ""

                    message = "Expense recorded successfully!"
                } else {
                    message = "Enter a valid expense title and amount."
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = ErrorRed,
                contentColor = White
            )
        ) {
            Text("Record Expense", fontWeight = FontWeight.Bold)
        }

        if (message.isNotBlank()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(message, color = BinanceYellow)
        }

        Spacer(modifier = Modifier.height(28.dp))

        // EXPENSE RECORDS

        Text(
            "Expense Records",
            style = MaterialTheme.typography.titleLarge,
            color = PrimaryText
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (expenses.isEmpty()) {
            Text(
                "No expenses recorded yet.",
                color = SecondaryText
            )
        } else {

            expenses.forEach { expense ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = AppSurface
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {

                        Text(
                            expense.title,
                            style = MaterialTheme.typography.titleMedium,
                            color = PrimaryText
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            "KSh ${"%.2f".format(expense.amount)}",
                            color = ErrorRed,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            "Category: ${expense.category}",
                            color = SecondaryText
                        )

                        Text(
                            "Description: ${expense.description}",
                            color = SecondaryText
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {

                            IconButton(
                                onClick = {
                                    editExpenseTitle = expense.title
                                    editExpenseAmount = expense.amount.toString()
                                    editExpenseCategory = expense.category
                                    editExpenseDescription = expense.description
                                    selectedExpense = expense
                                }
                            ) {
                                Icon(
                                    Icons.Filled.Edit,
                                    contentDescription = "Edit expense",
                                    tint = BinanceYellow
                                )
                            }

                            IconButton(
                                onClick = {
                                    expenseToDelete = expense
                                }
                            ) {
                                Icon(
                                    Icons.Filled.Delete,
                                    contentDescription = "Delete expense",
                                    tint = ErrorRed
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // EDIT EXPENSE DIALOG

    selectedExpense?.let { expense ->

        AlertDialog(
            onDismissRequest = { selectedExpense = null },
            title = { Text("Edit Expense") },
            text = {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState())
                ) {

                    OutlinedTextField(
                        value = editExpenseTitle,
                        onValueChange = { editExpenseTitle = it },
                        label = { Text("Expense Title") }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = editExpenseAmount,
                        onValueChange = { editExpenseAmount = it },
                        label = { Text("Amount (KSh)") },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Select Category")

                    categories.forEach { category ->

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = editExpenseCategory == category,
                                onClick = { editExpenseCategory = category }
                            )

                            Text(category)
                        }
                    }

                    OutlinedTextField(
                        value = editExpenseDescription,
                        onValueChange = { editExpenseDescription = it },
                        label = { Text("Description") }
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val amount = editExpenseAmount.toDoubleOrNull()

                        if (editExpenseTitle.isNotBlank() && amount != null && amount > 0) {
                            expenseViewModel.updateExpense(
                                expense.copy(
                                    title = editExpenseTitle.trim(),
                                    amount = amount,
                                    category = editExpenseCategory,
                                    description = editExpenseDescription.trim()
                                )
                            )

                            message = "Expense updated successfully!"
                            selectedExpense = null
                        } else {
                            message = "Enter a valid expense title and amount."
                        }
                    }
                ) {
                    Text("Save Changes", color = BinanceYellow)
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedExpense = null }) {
                    Text("Cancel")
                }
            },
            containerColor = AppSurface,
            titleContentColor = PrimaryText,
            textContentColor = SecondaryText
        )
    }

    // DELETE EXPENSE DIALOG

    expenseToDelete?.let { expense ->

        AlertDialog(
            onDismissRequest = { expenseToDelete = null },
            title = { Text("Delete Expense") },
            text = {
                Text("Are you sure you want to delete this expense record?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        expenseViewModel.deleteExpense(expense)
                        expenseToDelete = null
                        message = "Expense deleted successfully!"
                    }
                ) {
                    Text("Delete", color = ErrorRed)
                }
            },
            dismissButton = {
                TextButton(onClick = { expenseToDelete = null }) {
                    Text("Cancel")
                }
            },
            containerColor = AppSurface,
            titleContentColor = PrimaryText,
            textContentColor = SecondaryText
        )
    }
}