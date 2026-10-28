package com.example.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FoodLogEntry
import com.example.data.model.MealType

@Composable
fun EditFoodDialog(
    entry: FoodLogEntry,
    onDismiss: () -> Unit,
    onSave: (FoodLogEntry) -> Unit,
    onDelete: (FoodLogEntry) -> Unit
) {
    var gramsText by remember { mutableStateOf(entry.gramsOrServings.toInt().toString()) }
    var selectedMeal by remember { mutableStateOf(MealType.fromString(entry.mealType)) }

    val originalGrams = if (entry.gramsOrServings > 0) entry.gramsOrServings else 100.0
    val newGrams = gramsText.toDoubleOrNull() ?: originalGrams
    val scaleFactor = newGrams / originalGrams
    val updatedCalories = (entry.calories * scaleFactor).toInt().coerceAtLeast(1)
    val updatedProtein = Math.round((entry.protein * scaleFactor) * 10.0) / 10.0
    val updatedCarbs = Math.round((entry.carbs * scaleFactor) * 10.0) / 10.0
    val updatedFat = Math.round((entry.fat * scaleFactor) * 10.0) / 10.0

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Edit ${entry.foodName}",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Meal Category:",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    MealType.entries.forEach { meal ->
                        val isSelected = selectedMeal == meal
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedMeal = meal },
                            label = { Text(meal.title, fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = meal.color.copy(alpha = 0.2f),
                                selectedLabelColor = meal.color
                            ),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = gramsText,
                    onValueChange = { gramsText = it.filter { c -> c.isDigit() } },
                    label = { Text("Portion (${entry.unit})") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "New Calories: $updatedCalories kcal",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Macros: P: ${updatedProtein}g • C: ${updatedCarbs}g • F: ${updatedFat}g",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val updated = entry.copy(
                        mealType = selectedMeal.id,
                        gramsOrServings = newGrams,
                        calories = updatedCalories,
                        protein = updatedProtein,
                        carbs = updatedCarbs,
                        fat = updatedFat
                    )
                    onSave(updated)
                    onDismiss()
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            Row {
                OutlinedButton(
                    onClick = {
                        onDelete(entry)
                        onDismiss()
                    }
                ) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedButton(onClick = onDismiss) {
                    Text("Cancel")
                }
            }
        }
    )
}
