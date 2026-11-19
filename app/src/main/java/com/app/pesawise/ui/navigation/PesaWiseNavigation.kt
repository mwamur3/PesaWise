package com.app.pesawise.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

import com.app.pesawise.ui.screens.DashboardScreen
import com.app.pesawise.ui.screens.BudgetScreen
import com.app.pesawise.ui.screens.IncomeScreen
import com.app.pesawise.ui.screens.ExpenseScreen
import com.app.pesawise.ui.screens.InsightsScreen

import com.app.pesawise.ui.theme.AppSurface
import com.app.pesawise.ui.theme.BinanceYellow
import com.app.pesawise.ui.theme.SecondaryText

import com.app.pesawise.viewmodel.BudgetViewModel
import com.app.pesawise.viewmodel.IncomeViewModel
import com.app.pesawise.viewmodel.ExpenseViewModel

private object Routes {
    const val DASHBOARD = "dashboard"
    const val BUDGETS = "budgets"
    const val INCOME = "income"
    const val EXPENSES = "expenses"
    const val INSIGHTS = "insights"
}

private data class NavigationItem(
    val route: String,
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

@Composable
fun PesaWiseNavigation(
    budgetViewModel: BudgetViewModel,
    incomeViewModel: IncomeViewModel,
    expenseViewModel: ExpenseViewModel
) {

    val navController = rememberNavController()

    val navigationItems = listOf(
        NavigationItem(
            Routes.DASHBOARD,
            "Home",
            Icons.Filled.Home
        ),
        NavigationItem(
            Routes.BUDGETS,
            "Budgets",
            Icons.Filled.AccountBalanceWallet
        ),
        NavigationItem(
            Routes.INCOME,
            "Income",
            Icons.Filled.TrendingUp
        ),
        NavigationItem(
            Routes.EXPENSES,
            "Expenses",
            Icons.Filled.ReceiptLong
        ),
        NavigationItem(
            Routes.INSIGHTS,
            "Insights",
            Icons.Filled.BarChart
        )
    )

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    Scaffold(

        bottomBar = {

            NavigationBar(
                containerColor = AppSurface
            ) {

                navigationItems.forEach { item ->

                    val selected = currentRoute == item.route

                    NavigationBarItem(

                        selected = selected,

                        onClick = {

                            navController.navigate(item.route) {

                                popUpTo(
                                    navController.graph.findStartDestination().id
                                ) {
                                    saveState = true
                                }

                                launchSingleTop = true
                                restoreState = true
                            }
                        },

                        icon = {

                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.label
                            )
                        },

                        label = {
                            androidx.compose.material3.Text(item.label)
                        },

                        colors = NavigationBarItemDefaults.colors(

                            selectedIconColor = BinanceYellow,
                            selectedTextColor = BinanceYellow,

                            unselectedIconColor = SecondaryText,
                            unselectedTextColor = SecondaryText,

                            indicatorColor = BinanceYellow.copy(
                                alpha = 0.15f
                            )
                        )
                    )
                }
            }
        }

    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = Routes.DASHBOARD,
            modifier = Modifier.padding(innerPadding)
        ) {

            composable(Routes.DASHBOARD) {
                DashboardScreen(
                    budgetViewModel = budgetViewModel,
                    incomeViewModel = incomeViewModel,
                    expenseViewModel = expenseViewModel
                )
            }

            composable(Routes.BUDGETS) {
                BudgetScreen(
                    budgetViewModel = budgetViewModel
                )
            }

            composable(Routes.INCOME) {
                IncomeScreen(
                    incomeViewModel = incomeViewModel
                )
            }

            composable(Routes.EXPENSES) {
                ExpenseScreen(
                    expenseViewModel = expenseViewModel
                )
            }

            composable(Routes.INSIGHTS) {
                InsightsScreen(
                    incomeViewModel = incomeViewModel,
                    expenseViewModel = expenseViewModel
                )
            }
        }
    }
}