package com.example.data.model

data class FoodItem(
    val id: String,
    val name: String,
    val category: String,
    val caloriesPer100g: Double,
    val proteinPer100g: Double,
    val carbsPer100g: Double,
    val fatPer100g: Double,
    val defaultServingGrams: Double = 100.0,
    val servingUnitName: String = "1 serving (100g)"
) {
    fun calculateCalories(grams: Double): Int {
        return ((caloriesPer100g * grams) / 100.0).toInt().coerceAtLeast(1)
    }

    fun calculateProtein(grams: Double): Double {
        return Math.round(((proteinPer100g * grams) / 100.0) * 10.0) / 10.0
    }

    fun calculateCarbs(grams: Double): Double {
        return Math.round(((carbsPer100g * grams) / 100.0) * 10.0) / 10.0
    }

    fun calculateFat(grams: Double): Double {
        return Math.round(((fatPer100g * grams) / 100.0) * 10.0) / 10.0
    }
}
