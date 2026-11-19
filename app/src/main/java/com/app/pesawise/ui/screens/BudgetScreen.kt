package com.app.pesawise.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

import com.app.pesawise.data.model.Budget
import com.app.pesawise.viewmodel.BudgetViewModel
import com.app.pesawise.ui.theme.*

import java.util.Calendar

@Composable
fun BudgetScreen(budgetViewModel: BudgetViewModel) {

    val budgets by budgetViewModel.budgets.collectAsState()

    val calendar = Calendar.getInstance()
    val currentMonth = calendar.get(Calendar.MONTH) + 1
    val currentYear = calendar.get(Calendar.YEAR)

    val monthNames = listOf(
        "January", "February", "March", "April",
        "May", "June", "July", "August",
        "September", "October", "November", "December"
    )

    var budgetInput by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    var selectedBudget by remember { mutableStateOf<Budget?>(null) }
    var budgetToDelete by remember { mutableStateOf<Budget?>(null) }
    var editBudgetAmount by remember { mutableStateOf("") }

    val currentBudget = budgets.firstOrNull {
        it.month == currentMonth && it.year == currentYear
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {

        Text(
            text = "Budget Management",
            style = MaterialTheme.typography.headlineLarge,
            color = PrimaryText
        )

        Text(
            text = "Plan your spending and manage your monthly budgets.",
            color = SecondaryText
        )

        Spacer(modifier = Modifier.height(24.dp))

        // CURRENT BUDGET CARD

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = BinanceYellow
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Filled.AccountBalanceWallet,
                        contentDescription = null,
                        tint = AppBackground
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        "CURRENT MONTHLY BUDGET",
                        color = AppBackground,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    "${monthNames[currentMonth - 1]} $currentYear",
                    color = AppBackground
                )

                Text(
                    "KSh ${"%.2f".format(currentBudget?.amount ?: 0.0)}",
                    style = MaterialTheme.typography.headlineLarge,
                    color = AppBackground,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // SET BUDGET

        Text(
            "Set Monthly Budget",
            style = MaterialTheme.typography.titleLarge,
            color = PrimaryText
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = budgetInput,
            onValueChange = { budgetInput = it },
            label = { Text("Budget Amount (KSh)") },
            leadingIcon = {
                Icon(
                    Icons.Filled.AccountBalanceWallet,
                    contentDescription = null
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            ),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

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
                    message = "Please enter a valid budget amount."
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = BinanceYellow,
                contentColor = AppBackground
            )
        ) {
            Text("Save Budget", fontWeight = FontWeight.Bold)
        }

        if (message.isNotBlank()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(message, color = BinanceYellow)
        }

        Spacer(modifier = Modifier.height(28.dp))

        // SAVED BUDGETS

        Text(
            "Saved Budgets",
            style = MaterialTheme.typography.titleLarge,
            color = PrimaryText
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (budgets.isEmpty()) {
            Text(
                "No budgets have been created yet.",
                color = SecondaryText
            )
        } else {
            budgets.forEach { budget ->

                val monthName = monthNames.getOrElse(budget.month - 1) {
                    "Unknown month"
                }

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
                            "$monthName ${budget.year}",
                            color = PrimaryText,
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            "KSh ${"%.2f".format(budget.amount)}",
                            color = BinanceYellow,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {

                            IconButton(
                                onClick = {
                                    editBudgetAmount = budget.amount.toString()
                                    selectedBudget = budget
                                }
                            ) {
                                Icon(
                                    Icons.Filled.Edit,
                                    contentDescription = "Edit budget",
                                    tint = BinanceYellow
                                )
                            }

                            IconButton(
                                onClick = {
                                    budgetToDelete = budget
                                }
                            ) {
                                Icon(
                                    Icons.Filled.Delete,
                                    contentDescription = "Delete budget",
                                    tint = ErrorRed
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // EDIT DIALOG

    selectedBudget?.let { budget ->

        AlertDialog(
            onDismissRequest = { selectedBudget = null },
            title = { Text("Edit Budget") },
            text = {
                OutlinedTextField(
                    value = editBudgetAmount,
                    onValueChange = { editBudgetAmount = it },
                    label = { Text("Budget Amount (KSh)") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal
                    )
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val amount = editBudgetAmount.toDoubleOrNull()

                        if (amount != null && amount > 0) {
                            budgetViewModel.updateBudget(
                                budget.copy(amount = amount)
                            )
                            message = "Budget updated successfully!"
                            selectedBudget = null
                        } else {
                            message = "Please enter a valid amount."
                        }
                    }
                ) {
                    Text("Save Changes", color = BinanceYellow)
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedBudget = null }) {
                    Text("Cancel")
                }
            },
            containerColor = AppSurface,
            titleContentColor = PrimaryText,
            textContentColor = SecondaryText
        )
    }

    // DELETE DIALOG

    budgetToDelete?.let { budget ->

        AlertDialog(
            onDismissRequest = { budgetToDelete = null },
            title = { Text("Delete Budget") },
            text = {
                Text("Are you sure you want to delete this budget?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        budgetViewModel.deleteBudget(budget.id)
                        budgetToDelete = null
                        message = "Budget deleted successfully!"
                    }
                ) {
                    Text("Delete", color = ErrorRed)
                }
            },
            dismissButton = {
                TextButton(onClick = { budgetToDelete = null }) {
                    Text("Cancel")
                }
            },
            containerColor = AppSurface,
            titleContentColor = PrimaryText,
            textContentColor = SecondaryText
        )
    }
}