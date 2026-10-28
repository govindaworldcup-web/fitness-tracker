package com.example.data.model

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.MealBreakfastColor
import com.example.ui.theme.MealDinnerColor
import com.example.ui.theme.MealLunchColor
import com.example.ui.theme.MealSnacksColor

enum class MealType(
    val id: String,
    val title: String,
    val iconName: String,
    val color: Color
) {
    BREAKFAST("BREAKFAST", "Breakfast", "wb_sunny", MealBreakfastColor),
    LUNCH("LUNCH", "Lunch", "restaurant", MealLunchColor),
    DINNER("DINNER", "Dinner", "nightlight", MealDinnerColor),
    SNACKS("SNACKS", "Snacks", "coffee", MealSnacksColor);

    companion object {
        fun fromString(value: String): MealType =
            entries.firstOrNull { it.id.equals(value, ignoreCase = true) } ?: BREAKFAST
    }
}
