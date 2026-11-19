package com.app.pesawise.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.app.pesawise.ui.theme.*
import com.app.pesawise.viewmodel.BudgetViewModel
import com.app.pesawise.viewmodel.IncomeViewModel
import com.app.pesawise.viewmodel.ExpenseViewModel

import java.util.Calendar
import java.util.Locale

@Composable
fun DashboardScreen(
    budgetViewModel: BudgetViewModel,
    incomeViewModel: IncomeViewModel,
    expenseViewModel: ExpenseViewModel
) {

    // Observe database records

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

    val currentMonthName = monthNames[currentMonth - 1]

    // Current monthly budget

    val currentBudget = budgets.firstOrNull {
        it.month == currentMonth && it.year == currentYear
    }

    val budgetAmount = currentBudget?.amount ?: 0.0

    // Financial calculations

    val incomeAmount = totalIncome ?: 0.0
    val expenseAmount = totalExpenses ?: 0.0

    val availableBalance =
        budgetAmount + incomeAmount - expenseAmount

    val netCashFlow = incomeAmount - expenseAmount

    val formattedBalance = String.format(
        Locale.getDefault(),
        "%,.2f",
        availableBalance
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 24.dp)
    ) {

        // HEADER

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column {

                Text(
                    text = "PesaWise",
                    color = BinanceYellow,
                    fontSize = 27.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Smart budgeting. Better decisions.",
                    color = SecondaryText,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Surface(
                shape = RoundedCornerShape(14.dp),
                color = AppSurfaceVariant
            ) {

                Icon(
                    imageVector = Icons.Default.AccountBalanceWallet,
                    contentDescription = "PesaWise wallet",
                    tint = BinanceYellow,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        // WELCOME SECTION

        Text(
            text = "Welcome back!",
            color = PrimaryText,
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(5.dp))

        Text(
            text = "Here's an overview of your finances.",
            color = SecondaryText,
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(25.dp))

        // AVAILABLE BALANCE CARD

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(
                containerColor = BinanceYellow
            )
        ) {

            Column(
                modifier = Modifier.padding(22.dp)
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        imageVector = Icons.Default.AccountBalanceWallet,
                        contentDescription = null,
                        tint = AppBackground
                    )

                    Spacer(modifier = Modifier.width(9.dp))

                    Text(
                        text = "AVAILABLE BALANCE",
                        color = AppBackground,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        letterSpacing = 1.sp
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = "KSh $formattedBalance",
                    color = AppBackground,
                    fontSize = 29.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Monthly budget + recorded income - recorded expenses",
                    color = AppBackground.copy(alpha = 0.75f),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // MONTHLY OVERVIEW

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = Icons.Default.CalendarMonth,
                contentDescription = null,
                tint = BinanceYellow
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "$currentMonthName $currentYear Overview",
                color = PrimaryText,
                style = MaterialTheme.typography.titleLarge
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // FINANCIAL SUMMARY CARDS

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            SmallDashboardCard(
                modifier = Modifier.weight(1f),
                title = "Budget",
                amount = budgetAmount,
                icon = Icons.Default.Savings,
                iconColor = BinanceYellow
            )

            SmallDashboardCard(
                modifier = Modifier.weight(1f),
                title = "Income",
                amount = incomeAmount,
                icon = Icons.Default.ArrowDownward,
                iconColor = SuccessGreen
            )

            SmallDashboardCard(
                modifier = Modifier.weight(1f),
                title = "Expenses",
                amount = expenseAmount,
                icon = Icons.Default.ArrowUpward,
                iconColor = ErrorRed
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        // CASH FLOW SECTION

        Text(
            text = "Financial Summary",
            color = PrimaryText,
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(14.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(
                containerColor = AppSurface
            )
        ) {

            Column(
                modifier = Modifier.padding(18.dp)
            ) {

                Text(
                    text = "Net Cash Flow",
                    color = SecondaryText,
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "KSh ${
                        String.format(
                            Locale.getDefault(),
                            "%,.2f",
                            netCashFlow
                        )
                    }",
                    color = if (netCashFlow >= 0) {
                        SuccessGreen
                    } else {
                        ErrorRed
                    },
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Total recorded income minus total recorded expenses",
                    color = SecondaryText,
                    style = MaterialTheme.typography.bodySmall
                )

                Spacer(modifier = Modifier.height(18.dp))

                HorizontalDivider(
                    color = BorderColor
                )

                Spacer(modifier = Modifier.height(15.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Text(
                        text = "Income records",
                        color = SecondaryText
                    )

                    Text(
                        text = "${incomes.size}",
                        color = SuccessGreen,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Text(
                        text = "Expense records",
                        color = SecondaryText
                    )

                    Text(
                        text = "${expenses.size}",
                        color = ErrorRed,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // RECENT INCOME

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "Recent Income",
                color = PrimaryText,
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                text = "${incomes.size} records",
                color = SecondaryText,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (incomes.isEmpty()) {

            EmptyDashboardCard(
                message = "No income records yet."
            )

        } else {

            incomes.take(3).forEach { income ->

                TransactionCard(
                    title = income.source,
                    subtitle = income.description,
                    amount = income.amount,
                    icon = Icons.Default.ArrowDownward,
                    amountColor = SuccessGreen
                )

                Spacer(modifier = Modifier.height(9.dp))
            }
        }

        Spacer(modifier = Modifier.height(25.dp))

        // RECENT EXPENSES

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "Recent Expenses",
                color = PrimaryText,
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                text = "${expenses.size} records",
                color = SecondaryText,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (expenses.isEmpty()) {

            EmptyDashboardCard(
                message = "No expense records yet."
            )

        } else {

            expenses.take(3).forEach { expense ->

                TransactionCard(
                    title = expense.title,
                    subtitle = expense.category,
                    amount = expense.amount,
                    icon = Icons.Default.ArrowUpward,
                    amountColor = ErrorRed
                )

                Spacer(modifier = Modifier.height(9.dp))
            }
        }

        Spacer(modifier = Modifier.height(25.dp))

        // FOOTER

        HorizontalDivider(
            color = BorderColor
        )

        Spacer(modifier = Modifier.height(15.dp))

        Text(
            text = "PesaWise | Take control of your finances.",
            color = SecondaryText,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(15.dp))
    }
}


// SMALL FINANCIAL SUMMARY CARD

@Composable
private fun SmallDashboardCard(
    modifier: Modifier = Modifier,
    title: String,
    amount: Double,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color
) {

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = AppSurface
        )
    ) {

        Column(
            modifier = Modifier.padding(12.dp)
        ) {

            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = iconColor,
                modifier = Modifier.size(22.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = title,
                color = SecondaryText,
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "KSh ${
                    String.format(
                        Locale.getDefault(),
                        "%,.0f",
                        amount
                    )
                }",
                color = PrimaryText,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }
    }
}


// RECENT TRANSACTION CARD

@Composable
private fun TransactionCard(
    title: String,
    subtitle: String,
    amount: Double,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    amountColor: Color
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(
            containerColor = AppSurface
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = amountColor.copy(alpha = 0.12f)
            ) {

                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = amountColor,
                    modifier = Modifier.padding(11.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = title,
                    color = PrimaryText,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = subtitle,
                    color = SecondaryText,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Text(
                text = "KSh ${
                    String.format(
                        Locale.getDefault(),
                        "%,.2f",
                        amount
                    )
                }",
                color = amountColor,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
            )
        }
    }
}


// EMPTY STATE CARD

@Composable
private fun EmptyDashboardCard(
    message: String
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(
            containerColor = AppSurface
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = Icons.Default.ReceiptLong,
                contentDescription = null,
                tint = SecondaryText
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = message,
                color = SecondaryText,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}