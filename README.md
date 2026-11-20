# PesaWise

A student budget app for Android. PesaWise helps students plan a monthly budget, track income and expenses, and see how much money is left to use.

## The Problem

Many students run out of money before the month ends because they don't track where it goes. PesaWise lets them split a budget into categories and warns them when they are overspending.

## The Solution

PesaWise gives students one place to plan and track their monthly money. They set a monthly budget, divide it into categories, and record their income and expenses. The dashboard shows the totals and what is left, and warns them when they are overspending, so they can adjust before the month runs out.

## Features

- **Monthly budget:** Set a budget for the month and split it into categories.
- **Income:** Add, edit and delete income.
- **Expenses:** Add, edit and delete expenses.
- **Dashboard:** View totals, the remaining balance and overspending warnings.

## Tech Stack

- **Language:** Kotlin
- **UI:** Jetpack Compose
- **Local database:** Room
- **Navigation:** Navigation Compose
- **Architecture:** MVVM
- **Minimum Android version:** 8.0 (API 26)

## Architecture

- Compose screens display state and send user actions to ViewModels.
- ViewModels hold the budget, income and expense logic and read and write data through Room.
- Room stores all data locally on the device.

## Getting Started

### Requirements

- Android Studio
- A device or emulator running Android 8.0 (API 26) or higher

### Build and Run

1. Clone the repository: `git clone <repository-url>`
2. Open the project in Android Studio.
3. Wait for Gradle to finish syncing.
4. Run the app on a device or emulator.

## Testing

The following has been tested:

- Adding, editing and deleting income and expenses
- Dashboard totals and balance update correctly
- Overspending warning appears
