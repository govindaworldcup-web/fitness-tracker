package com.example

import com.example.data.model.ActivityLevel
import com.example.data.model.FitnessGoal
import com.example.data.model.Sex
import com.example.util.HealthCalculator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class HealthCalculatorTest {

    @Test
    fun testBmiCalculation_normalWeight() {
        // Height: 175 cm (1.75 m), Weight: 70 kg
        // Expected BMI = 70 / (1.75 * 1.75) = 70 / 3.0625 = 22.857... -> 22.9
        val result = HealthCalculator.calculateBmi(weightKg = 70f, heightCm = 175f)
        assertEquals(22.9, result.bmi, 0.01)
        assertEquals("Normal weight", result.category)
    }

    @Test
    fun testBmiCalculation_overweight() {
        // Height: 170 cm (1.70 m), Weight: 80 kg
        // Expected BMI = 80 / (1.70 * 1.70) = 80 / 2.89 = 27.68... -> 27.7
        val result = HealthCalculator.calculateBmi(weightKg = 80f, heightCm = 170f)
        assertEquals(27.7, result.bmi, 0.01)
        assertEquals("Overweight", result.category)
    }

    @Test
    fun testBmiCalculation_underweight() {
        // Height: 180 cm (1.80 m), Weight: 55 kg
        // Expected BMI = 55 / (1.80 * 1.80) = 55 / 3.24 = 16.97... -> 17.0
        val result = HealthCalculator.calculateBmi(weightKg = 55f, heightCm = 180f)
        assertEquals(17.0, result.bmi, 0.01)
        assertEquals("Underweight", result.category)
    }

    @Test
    fun testBmrCalculation_male_mifflinStJeor() {
        // Mifflin-St Jeor (Male): (10 * W) + (6.25 * H) - (5 * A) + 5
        // W = 75 kg, H = 180 cm, A = 25 years
        // (10 * 75) + (6.25 * 180) - (5 * 25) + 5
        // = 750 + 1125 - 125 + 5 = 1755.0
        val bmr = HealthCalculator.calculateBmr(weightKg = 75f, heightCm = 180f, age = 25, sex = Sex.MALE)
        assertEquals(1755.0, bmr, 0.1)
    }

    @Test
    fun testBmrCalculation_female_mifflinStJeor() {
        // Mifflin-St Jeor (Female): (10 * W) + (6.25 * H) - (5 * A) - 161
        // W = 60 kg, H = 165 cm, A = 30 years
        // (10 * 60) + (6.25 * 165) - (5 * 30) - 161
        // = 600 + 1031.25 - 150 - 161 = 1320.25 -> 1320.3
        val bmr = HealthCalculator.calculateBmr(weightKg = 60f, heightCm = 165f, age = 30, sex = Sex.FEMALE)
        assertEquals(1320.3, bmr, 0.1)
    }

    @Test
    fun testTdeeMaintenanceAndGoals() {
        val bmr = 1755.0
        // Sedentary: 1755 * 1.2 = 2106.0
        val sedentaryMaintenance = HealthCalculator.calculateMaintenanceCalories(bmr, ActivityLevel.SEDENTARY)
        assertEquals(2106.0, sedentaryMaintenance, 0.1)

        // Moderate: 1755 * 1.55 = 2720.25 -> 2720.3
        val moderateMaintenance = HealthCalculator.calculateMaintenanceCalories(bmr, ActivityLevel.MODERATE)
        assertEquals(2720.3, moderateMaintenance, 0.1)

        // Goal: Weight Loss (-500 kcal)
        val lossTarget = HealthCalculator.calculateDailyTarget(bmr, ActivityLevel.MODERATE, FitnessGoal.WEIGHT_LOSS)
        assertEquals(2220, lossTarget)

        // Goal: Weight Gain (+400 kcal)
        val gainTarget = HealthCalculator.calculateDailyTarget(bmr, ActivityLevel.MODERATE, FitnessGoal.WEIGHT_GAIN)
        assertEquals(3120, gainTarget)

        // Custom Target Override
        val customTarget = HealthCalculator.calculateDailyTarget(bmr, ActivityLevel.MODERATE, FitnessGoal.WEIGHT_LOSS, customTarget = 2400)
        assertEquals(2400, customTarget)
    }

    @Test
    fun testHealthyWeightRange() {
        val bmi = HealthCalculator.calculateBmi(weightKg = 70f, heightCm = 175f)
        // For 1.75m: min (18.5 * 3.0625 = 56.65 -> 56.7), max (24.9 * 3.0625 = 76.25 -> 76.3)
        assertTrue(bmi.minHealthyWeightKg > 50.0)
        assertTrue(bmi.maxHealthyWeightKg < 80.0)
        assertTrue(bmi.minHealthyWeightKg < bmi.maxHealthyWeightKg)
    }
}
