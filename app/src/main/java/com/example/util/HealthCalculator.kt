package com.example.util

import com.example.data.model.ActivityLevel
import com.example.data.model.BmiResult
import com.example.data.model.BmrResult
import com.example.data.model.FitnessGoal
import com.example.data.model.Sex
import com.example.ui.theme.BmiNormalColor
import com.example.ui.theme.BmiObeseColor
import com.example.ui.theme.BmiOverweightColor
import com.example.ui.theme.BmiSevereObeseColor
import com.example.ui.theme.BmiUnderweightColor
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.roundToInt

object HealthCalculator {

    /**
     * Calculates Body Mass Index (BMI).
     * Formula: BMI = Weight (kg) / Height (m)^2
     */
    fun calculateBmi(weightKg: Float, heightCm: Float): BmiResult {
        if (heightCm <= 0f || weightKg <= 0f) {
            return BmiResult(
                bmi = 0.0,
                category = "Unknown",
                color = BmiNormalColor,
                minHealthyWeightKg = 50.0,
                maxHealthyWeightKg = 75.0,
                advice = "Enter valid height and weight to calculate BMI."
            )
        }

        val heightM = heightCm / 100.0
        val rawBmi = weightKg / heightM.pow(2)
        val roundedBmi = (rawBmi * 10.0).roundToInt() / 10.0

        val minHealthyWeight = (18.5 * heightM.pow(2) * 10.0).roundToInt() / 10.0
        val maxHealthyWeight = (24.9 * heightM.pow(2) * 10.0).roundToInt() / 10.0

        val (category, color, advice) = when {
            roundedBmi < 18.5 -> Triple(
                "Underweight",
                BmiUnderweightColor,
                "Your BMI indicates you may be underweight. A gentle calorie surplus and nutrient-dense foods can help reach a healthy weight."
            )
            roundedBmi in 18.5..24.9 -> Triple(
                "Normal weight",
                BmiNormalColor,
                "Great job! Your BMI is within the healthy range for adults. Keep maintaining your balanced diet and active lifestyle."
            )
            roundedBmi in 25.0..29.9 -> Triple(
                "Overweight",
                BmiOverweightColor,
                "Your BMI is slightly above the standard range. A mild caloric deficit and regular physical activity can help you reach a healthy weight."
            )
            roundedBmi in 30.0..34.9 -> Triple(
                "Obesity (Class I)",
                BmiObeseColor,
                "Your BMI falls into Obesity Class I. Consistent daily tracking and moderate cardiovascular exercise will support your weight loss journey."
            )
            roundedBmi in 35.0..39.9 -> Triple(
                "Obesity (Class II)",
                BmiObeseColor,
                "Your BMI is classified as Obesity Class II. Focusing on whole foods, portion control, and daily movement can yield significant health benefits."
            )
            else -> Triple(
                "Obesity (Class III)",
                BmiSevereObeseColor,
                "Your BMI falls into Obesity Class III. Consider consulting a healthcare professional or nutritionist for personalized guidance."
            )
        }

        return BmiResult(
            bmi = roundedBmi,
            category = category,
            color = color,
            minHealthyWeightKg = minHealthyWeight,
            maxHealthyWeightKg = maxHealthyWeight,
            advice = advice
        )
    }

    /**
     * Calculates Basal Metabolic Rate (BMR) using the Mifflin–St Jeor equation.
     * Men: (10 × W) + (6.25 × H) - (5 × A) + 5
     * Women: (10 × W) + (6.25 × H) - (5 × A) - 161
     */
    fun calculateBmr(
        weightKg: Float,
        heightCm: Float,
        age: Int,
        sex: Sex
    ): Double {
        if (weightKg <= 0f || heightCm <= 0f || age <= 0) return 1500.0

        val base = (10.0 * weightKg) + (6.25 * heightCm) - (5.0 * age)
        val rawBmr = if (sex == Sex.MALE) {
            base + 5.0
        } else {
            base - 161.0
        }

        return (rawBmr * 10.0).roundToInt() / 10.0
    }

    /**
     * Calculates Total Daily Energy Expenditure (Maintenance Calories)
     */
    fun calculateMaintenanceCalories(bmr: Double, activityLevel: ActivityLevel): Double {
        val maintenance = bmr * activityLevel.factor
        return (maintenance * 10.0).roundToInt() / 10.0
    }

    /**
     * Calculates suggested daily calorie target based on goal and custom override
     */
    fun calculateDailyTarget(
        bmr: Double,
        activityLevel: ActivityLevel,
        goal: FitnessGoal,
        customTarget: Int = 0
    ): Int {
        if (customTarget > 500) {
            return customTarget
        }

        val maintenance = calculateMaintenanceCalories(bmr, activityLevel)
        val target = maintenance + goal.calorieDelta
        // Apply safe floor (minimum 1200 kcal for general safety)
        return max(1200, target.roundToInt())
    }

    fun computeFullBmrResult(
        weightKg: Float,
        heightCm: Float,
        age: Int,
        sex: Sex,
        activityLevel: ActivityLevel,
        goal: FitnessGoal,
        customTarget: Int = 0
    ): BmrResult {
        val bmr = calculateBmr(weightKg, heightCm, age, sex)
        val maintenance = calculateMaintenanceCalories(bmr, activityLevel)
        val target = calculateDailyTarget(bmr, activityLevel, goal, customTarget)
        return BmrResult(
            bmr = bmr,
            maintenanceCalories = maintenance,
            targetCalories = target,
            goal = goal,
            activityLevel = activityLevel
        )
    }
}
