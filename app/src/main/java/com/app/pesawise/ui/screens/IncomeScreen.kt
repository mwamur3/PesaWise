package com.app.pesawise.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

import com.app.pesawise.data.model.Income
import com.app.pesawise.viewmodel.IncomeViewModel
import com.app.pesawise.ui.theme.*

@Composable
fun IncomeScreen(incomeViewModel: IncomeViewModel) {

    val incomes by incomeViewModel.incomes.collectAsState()
    val totalIncome by incomeViewModel.totalIncome.collectAsState()

    var incomeSource by remember { mutableStateOf("") }
    var incomeAmount by remember { mutableStateOf("") }
    var incomeDescription by remember { mutableStateOf("") }

    var message by remember { mutableStateOf("") }

    var selectedIncome by remember { mutableStateOf<Income?>(null) }
    var incomeToDelete by remember { mutableStateOf<Income?>(null) }

    var editIncomeSource by remember { mutableStateOf("") }
    var editIncomeAmount by remember { mutableStateOf("") }
    var editIncomeDescription by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {

        Text(
            "Income Management",
            style = MaterialTheme.typography.headlineLarge,
            color = PrimaryText
        )

        Text(
            "Record and monitor your additional income.",
            color = SecondaryText
        )

        Spacer(modifier = Modifier.height(24.dp))

        // TOTAL INCOME CARD

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = SuccessGreen
            )
        ) {
            Column(modifier = Modifier.padding(20.dp)) {

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Filled.TrendingUp,
                        contentDescription = null,
                        tint = AppBackground
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        "TOTAL RECORDED INCOME",
                        color = AppBackground,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    "KSh ${"%.2f".format(totalIncome ?: 0.0)}",
                    style = MaterialTheme.typography.headlineLarge,
                    color = AppBackground,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // ADD INCOME

        Text(
            "Record Additional Income",
            style = MaterialTheme.typography.titleLarge,
            color = PrimaryText
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = incomeSource,
            onValueChange = { incomeSource = it },
            label = { Text("Income Source") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = incomeAmount,
            onValueChange = { incomeAmount = it },
            label = { Text("Amount (KSh)") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            ),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = incomeDescription,
            onValueChange = { incomeDescription = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                val amount = incomeAmount.toDoubleOrNull()

                if (incomeSource.isNotBlank() && amount != null && amount > 0) {

                    incomeViewModel.addIncome(
                        Income(
                            amount = amount,
                            source = incomeSource.trim(),
                            description = incomeDescription.trim()
                        )
                    )

                    incomeSource = ""
                    incomeAmount = ""
                    incomeDescription = ""

                    message = "Income recorded successfully!"
                } else {
                    message = "Enter a valid income source and amount."
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = SuccessGreen,
                contentColor = AppBackground
            )
        ) {
            Icon(Icons.Filled.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Add Income", fontWeight = FontWeight.Bold)
        }

        if (message.isNotBlank()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(message, color = SuccessGreen)
        }

        Spacer(modifier = Modifier.height(28.dp))

        // INCOME RECORDS

        Text(
            "Income Records",
            style = MaterialTheme.typography.titleLarge,
            color = PrimaryText
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (incomes.isEmpty()) {
            Text(
                "No income records available.",
                color = SecondaryText
            )
        } else {

            incomes.forEach { income ->

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
                            income.source,
                            style = MaterialTheme.typography.titleMedium,
                            color = PrimaryText
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            "KSh ${"%.2f".format(income.amount)}",
                            color = SuccessGreen,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            income.description,
                            color = SecondaryText
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {

                            IconButton(
                                onClick = {
                                    editIncomeSource = income.source
                                    editIncomeAmount = income.amount.toString()
                                    editIncomeDescription = income.description
                                    selectedIncome = income
                                }
                            ) {
                                Icon(
                                    Icons.Filled.Edit,
                                    contentDescription = "Edit income",
                                    tint = SuccessGreen
                                )
                            }

                            IconButton(
                                onClick = {
                                    incomeToDelete = income
                                }
                            ) {
                                Icon(
                                    Icons.Filled.Delete,
                                    contentDescription = "Delete income",
                                    tint = ErrorRed
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // EDIT INCOME DIALOG

    selectedIncome?.let { income ->

        AlertDialog(
            onDismissRequest = { selectedIncome = null },
            title = { Text("Edit Income") },
            text = {
                Column {
                    OutlinedTextField(
                        value = editIncomeSource,
                        onValueChange = { editIncomeSource = it },
                        label = { Text("Income Source") }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = editIncomeAmount,
                        onValueChange = { editIncomeAmount = it },
                        label = { Text("Amount (KSh)") },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

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

                        if (editIncomeSource.isNotBlank() && amount != null && amount > 0) {
                            incomeViewModel.updateIncome(
                                income.copy(
                                    source = editIncomeSource.trim(),
                                    amount = amount,
                                    description = editIncomeDescription.trim()
                                )
                            )

                            message = "Income updated successfully!"
                            selectedIncome = null
                        } else {
                            message = "Enter a valid income source and amount."
                        }
                    }
                ) {
                    Text("Save Changes", color = SuccessGreen)
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedIncome = null }) {
                    Text("Cancel")
                }
            },
            containerColor = AppSurface,
            titleContentColor = PrimaryText,
            textContentColor = SecondaryText
        )
    }

    // DELETE INCOME DIALOG

    incomeToDelete?.let { income ->

        AlertDialog(
            onDismissRequest = { incomeToDelete = null },
            title = { Text("Delete Income") },
            text = {
                Text("Are you sure you want to delete this income record?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        incomeViewModel.deleteIncome(income)
                        incomeToDelete = null
                        message = "Income deleted successfully!"
                    }
                ) {
                    Text("Delete", color = ErrorRed)
                }
            },
            dismissButton = {
                TextButton(onClick = { incomeToDelete = null }) {
                    Text("Cancel")
                }
            },
            containerColor = AppSurface,
            titleContentColor = PrimaryText,
            textContentColor = SecondaryText
        )
    }
}