package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "food_logs")
data class FoodLogEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val foodName: String,
    val category: String = "General",
    val mealType: String, // BREAKFAST, LUNCH, DINNER, SNACKS
    val gramsOrServings: Double,
    val unit: String = "g",
    val calories: Int,
    val protein: Double = 0.0,
    val carbs: Double = 0.0,
    val fat: Double = 0.0,
    val date: String, // ISO YYYY-MM-DD
    val timestamp: Long = System.currentTimeMillis()
)
