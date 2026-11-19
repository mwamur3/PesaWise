package com.app.pesawise.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "budgets")
data class Budget(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val amount: Double,

    val month: Int,

    val year: Int,

    val createdAt: Long = System.currentTimeMillis()
)