package com.example.data.model

enum class Sex(val displayName: String) {
    MALE("Male"),
    FEMALE("Female");

    companion object {
        fun fromString(value: String): Sex = entries.firstOrNull { it.name.equals(value, ignoreCase = true) } ?: MALE
    }
}

enum class ActivityLevel(
    val title: String,
    val factor: Double,
    val description: String
) {
    SEDENTARY(
        title = "Sedentary",
        factor = 1.2,
        description = "Desk job, little to no regular exercise"
    ),
    LIGHT(
        title = "Lightly Active",
        factor = 1.375,
        description = "Light exercise or sports 1–3 days/week"
    ),
    MODERATE(
        title = "Moderately Active",
        factor = 1.55,
        description = "Moderate exercise or workouts 3–5 days/week"
    ),
    VERY_ACTIVE(
        title = "Very Active",
        factor = 1.725,
        description = "Heavy exercise or sports 6–7 days/week"
    ),
    EXTRA_ACTIVE(
        title = "Extra Active",
        factor = 1.9,
        description = "Very intense physical work or 2x daily training"
    );

    companion object {
        fun fromString(value: String): ActivityLevel =
            entries.firstOrNull { it.name.equals(value, ignoreCase = true) } ?: MODERATE
    }
}

enum class FitnessGoal(
    val title: String,
    val calorieDelta: Int,
    val description: String
) {
    WEIGHT_LOSS(
        title = "Weight Loss",
        calorieDelta = -500,
        description = "Mild healthy calorie deficit (~0.5 kg/week)"
    ),
    MAINTENANCE(
        title = "Maintain Weight",
        calorieDelta = 0,
        description = "Balance calories in with energy burned"
    ),
    WEIGHT_GAIN(
        title = "Weight Gain / Muscle",
        calorieDelta = 400,
        description = "Gradual calorie surplus to build lean mass"
    );

    companion object {
        fun fromString(value: String): FitnessGoal =
            entries.firstOrNull { it.name.equals(value, ignoreCase = true) } ?: WEIGHT_LOSS
    }
}
