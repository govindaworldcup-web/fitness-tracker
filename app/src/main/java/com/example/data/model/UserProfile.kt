package com.example.data.model

data class UserProfile(
    val age: Int = 26,
    val sex: Sex = Sex.MALE,
    val heightCm: Float = 175f,
    val weightKg: Float = 72f,
    val activityLevel: ActivityLevel = ActivityLevel.MODERATE,
    val fitnessGoal: FitnessGoal = FitnessGoal.WEIGHT_LOSS,
    val customCalorieTarget: Int = 0 // 0 means automatic calculation
)
