package com.example.data.model

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.BmiNormalColor
import com.example.ui.theme.BmiObeseColor
import com.example.ui.theme.BmiOverweightColor
import com.example.ui.theme.BmiSevereObeseColor
import com.example.ui.theme.BmiUnderweightColor

data class BmiResult(
    val bmi: Double,
    val category: String,
    val color: Color,
    val minHealthyWeightKg: Double,
    val maxHealthyWeightKg: Double,
    val advice: String
)

data class BmrResult(
    val bmr: Double,
    val maintenanceCalories: Double,
    val targetCalories: Int,
    val goal: FitnessGoal,
    val activityLevel: ActivityLevel
)

data class DailyNutritionSummary(
    val date: String,
    val targetCalories: Int,
    val totalCalories: Int,
    val remainingCalories: Int,
    val totalProtein: Double,
    val totalCarbs: Double,
    val totalFat: Double,
    val mealCalories: Map<MealType, Int>,
    val entriesByMeal: Map<MealType, List<FoodLogEntry>>
)
