// Concept Note: https://docs.google.com/document/d/1oz_QcdEee7b8RpWAMLvq_2RrNIektyGxyYn8o6HO8Lc/edit?usp=sharing
// Group Members:
// Hamza Abdi 191297
// Elmer Mwamure 178130
// David Wangai 179219
// Imran Hamid 191730
// Warren Okoth 192025
// Kevan Kimutai 189921
// PesaWise - Student Budget and Expenditure Management System

fun main() {
    println("======================================")
    println("        PESAWISE STUDENT BUDGET")
    println("   Student Budget and Expense Tracker")
    println("======================================")
    println()

    println("Welcome to PesaWise!")
    println("This system helps you manage your monthly")
    println("budget, record funds received, and track")
    println("your expenses.")
    println()

    println("You will be able to:")
    println("- Enter your monthly budget")
    println("- Record additional funds received")
    println("- Allocate your budget")
    println("- Record multiple expenses")
    println("- View your total expenditure")
    println("- View your remaining balance")
    println()

    // Monthly budget validation
    var monthlyBudget = -1.0

    while (monthlyBudget < 0) {
        println("Enter your monthly budget:")
        monthlyBudget = readLine()!!.toDouble()

        if (monthlyBudget < 0) {
            println("Invalid budget. Please enter a positive amount.")
        }
    }

    // Additional funds validation
    var additionalFunds = -1.0

    while (additionalFunds < 0) {
        println("Enter any additional funds received:")
        additionalFunds = readLine()!!.toDouble()

        if (additionalFunds < 0) {
            println("Invalid amount. Please enter a positive amount.")
        }
    }

    val totalFunds = monthlyBudget + additionalFunds

    println("Total available funds: KSh $totalFunds")

    println("\nEnter your budget allocations:")

    // Savings validation
    var savings = -1.0

    while (savings < 0) {
        println("Enter amount for Savings:")
        savings = readLine()!!.toDouble()

        if (savings < 0) {
            println("Invalid amount. Please enter a positive amount.")
        }
    }

    // Food validation
    var food = -1.0

    while (food < 0) {
        println("Enter amount for Food:")
        food = readLine()!!.toDouble()

        if (food < 0) {
            println("Invalid amount. Please enter a positive amount.")
        }
    }

    // Transport validation
    var transport = -1.0

    while (transport < 0) {
        println("Enter amount for Transport:")
        transport = readLine()!!.toDouble()

        if (transport < 0) {
            println("Invalid amount. Please enter a positive amount.")
        }
    }

    // Personal spending validation
    var personal = -1.0

    while (personal < 0) {
        println("Enter amount for Personal spending:")
        personal = readLine()!!.toDouble()

        if (personal < 0) {
            println("Invalid amount. Please enter a positive amount.")
        }
    }

    val totalAllocated = savings + food + transport + personal
    val remainingBalance = totalFunds - totalAllocated

    println("\n----- PESAWISE SUMMARY -----")
    println("Monthly budget: KSh $monthlyBudget")
    println("Additional funds: KSh $additionalFunds")
    println("Total available funds: KSh $totalFunds")
    println("Total allocated: KSh $totalAllocated")
    println("Remaining balance: KSh $remainingBalance")

    if (totalAllocated > totalFunds) {
        println("Warning: Your budget allocations exceed your available funds.")
    } else {
        println("Your budget is within your available funds.")
    }

    println("\n----- EXPENSE RECORDING -----")

    var totalExpenses = 0.0
    val expenses = mutableListOf<Double>()
    var continueAdding = "yes"

    while (continueAdding == "yes") {

        println("Enter expense amount:")
        val expense = readLine()!!.toDouble()

        if (expense < 0) {
            println("Invalid expense. Please enter a positive amount.")
        } else {
            totalExpenses = totalExpenses + expense
            expenses.add(expense)
        }

        println("Do you want to add another expense? (yes/no)")
        continueAdding = readLine()!!.lowercase()
    }

    println("\nRecorded expenses:")

    for (expense in expenses) {
        println("KSh $expense")
    }

    val finalBalance = remainingBalance - totalExpenses

    println("\nTotal expenditure: KSh $totalExpenses")
    println("Balance after expenses: KSh $finalBalance")

    if (finalBalance < 0) {
        println("Warning: You have exceeded your available balance.")
    } else {
        println("You are within your available balance.")
    }
}