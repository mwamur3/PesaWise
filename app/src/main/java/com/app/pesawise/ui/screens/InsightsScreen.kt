package com.app.pesawise.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

import com.app.pesawise.viewmodel.IncomeViewModel
import com.app.pesawise.viewmodel.ExpenseViewModel
import com.app.pesawise.ui.theme.*

@Composable
fun InsightsScreen(
    incomeViewModel: IncomeViewModel,
    expenseViewModel: ExpenseViewModel
) {

    val incomes by incomeViewModel.incomes.collectAsState()
    val expenses by expenseViewModel.expenses.collectAsState()

    val totalIncome = incomes.sumOf { it.amount }
    val totalExpenses = expenses.sumOf { it.amount }
    val netCashFlow = totalIncome - totalExpenses

    val categoryTotals = expenses
        .groupBy { it.category }
        .mapValues { (_, records) -> records.sumOf { it.amount } }
        .toList()
        .sortedByDescending { it.second }

    val highestCategory = categoryTotals.firstOrNull()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {

        Text(
            "Financial Insights",
            style = MaterialTheme.typography.headlineLarge,
            color = PrimaryText
        )

        Text(
            "Understand your financial habits and spending patterns.",
            color = SecondaryText
        )

        Spacer(modifier = Modifier.height(24.dp))

        // FINANCIAL SUMMARY

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Filled.Analytics,
                contentDescription = null,
                tint = BinanceYellow
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                "Financial Summary",
                style = MaterialTheme.typography.titleLarge,
                color = PrimaryText
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        InsightSummaryCard(
            title = "Total Income",
            amount = totalIncome,
            description = "All recorded income",
            color = SuccessGreen,
            icon = Icons.Filled.TrendingUp
        )

        Spacer(modifier = Modifier.height(12.dp))

        InsightSummaryCard(
            title = "Total Expenses",
            amount = totalExpenses,
            description = "All recorded expenditure",
            color = ErrorRed,
            icon = Icons.Filled.TrendingDown
        )

        Spacer(modifier = Modifier.height(12.dp))

        InsightSummaryCard(
            title = "Net Cash Flow",
            amount = netCashFlow,
            description = "Income minus expenses",
            color = BinanceYellow,
            icon = Icons.Filled.Analytics
        )

        Spacer(modifier = Modifier.height(28.dp))

        // HIGHEST SPENDING CATEGORY

        Text(
            "Highest Spending Category",
            style = MaterialTheme.typography.titleLarge,
            color = PrimaryText
        )

        Spacer(modifier = Modifier.height(12.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = AppSurface
            )
        ) {
            Column(modifier = Modifier.padding(18.dp)) {

                if (highestCategory == null || totalExpenses <= 0.0) {

                    Text(
                        "No spending information available yet.",
                        color = SecondaryText
                    )

                } else {

                    Text(
                        highestCategory.first,
                        style = MaterialTheme.typography.headlineMedium,
                        color = BinanceYellow,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        "KSh ${"%.2f".format(highestCategory.second)}",
                        color = PrimaryText,
                        style = MaterialTheme.typography.titleLarge
                    )

                    val percentage =
                        (highestCategory.second / totalExpenses) * 100

                    Text(
                        "Accounts for ${"%.1f".format(percentage)}% of total expenses.",
                        color = SecondaryText
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // CATEGORY BREAKDOWN

        Text(
            "Spending by Category",
            style = MaterialTheme.typography.titleLarge,
            color = PrimaryText
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (categoryTotals.isEmpty() || totalExpenses <= 0.0) {

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = AppSurface
                )
            ) {
                Text(
                    "No expenses recorded. Add expenses to see your spending breakdown.",
                    modifier = Modifier.padding(16.dp),
                    color = SecondaryText
                )
            }

        } else {

            categoryTotals.forEach { (category, amount) ->

                val percentage =
                    (amount / totalExpenses).toFloat().coerceIn(0f, 1f)

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = AppSurface
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            Text(
                                category,
                                color = PrimaryText,
                                fontWeight = FontWeight.Medium
                            )

                            Text(
                                "${"%.1f".format(percentage * 100)}%",
                                color = BinanceYellow
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        LinearProgressIndicator(
                            progress = { percentage },
                            modifier = Modifier.fillMaxWidth(),
                            color = BinanceYellow,
                            trackColor = AppSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            "KSh ${"%.2f".format(amount)}",
                            color = SecondaryText
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // FINANCIAL OBSERVATION

        Text(
            "Financial Observation",
            style = MaterialTheme.typography.titleLarge,
            color = PrimaryText
        )

        Spacer(modifier = Modifier.height(12.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = AppSurface
            )
        ) {
            Column(modifier = Modifier.padding(18.dp)) {

                when {
                    totalIncome == 0.0 && totalExpenses == 0.0 -> {
                        Text(
                            "Start recording your income and expenses to receive financial insights.",
                            color = SecondaryText
                        )
                    }

                    totalExpenses > totalIncome -> {
                        Text(
                            "Your recorded expenses exceed your recorded income. Consider reviewing your spending habits.",
                            color = ErrorRed
                        )
                    }

                    totalExpenses == totalIncome -> {
                        Text(
                            "Your recorded income and expenses are equal. Consider setting aside some money for savings.",
                            color = BinanceYellow
                        )
                    }

                    else -> {
                        Text(
                            "Your recorded income exceeds your expenses. You have a positive net cash flow.",
                            color = SuccessGreen
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
private fun InsightSummaryCard(
    title: String,
    amount: Double,
    description: String,
    color: androidx.compose.ui.graphics.Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = AppSurface
        )
    ) {
        Row(
            modifier = Modifier.padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(32.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {

                Text(
                    title,
                    color = SecondaryText,
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    "KSh ${"%.2f".format(amount)}",
                    color = color,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    description,
                    color = SecondaryText,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}