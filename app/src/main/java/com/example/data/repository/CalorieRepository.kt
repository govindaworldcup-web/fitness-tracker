package com.example.data.repository

import com.example.data.local.FoodLogDao
import com.example.data.local.UserPreferencesRepository
import com.example.data.model.FoodLogEntry
import com.example.data.model.MealType
import com.example.data.model.UserProfile
import com.example.util.DateUtils
import kotlinx.coroutines.flow.Flow

class CalorieRepository(
    private val foodLogDao: FoodLogDao,
    private val userPrefs: UserPreferencesRepository
) {
    val userProfile: Flow<UserProfile> = userPrefs.userProfile
    val isOnboardingCompleted: Flow<Boolean> = userPrefs.isOnboardingCompleted

    fun getEntriesForDate(date: String): Flow<List<FoodLogEntry>> {
        return foodLogDao.getEntriesForDate(date)
    }

    fun getAllEntries(): Flow<List<FoodLogEntry>> {
        return foodLogDao.getAllEntries()
    }

    fun getEntriesBetweenDates(startDate: String, endDate: String): Flow<List<FoodLogEntry>> {
        return foodLogDao.getEntriesBetweenDates(startDate, endDate)
    }

    suspend fun insertEntry(entry: FoodLogEntry): Long {
        return foodLogDao.insertEntry(entry)
    }

    suspend fun updateEntry(entry: FoodLogEntry) {
        foodLogDao.updateEntry(entry)
    }

    suspend fun deleteEntry(entry: FoodLogEntry) {
        foodLogDao.deleteEntry(entry)
    }

    suspend fun deleteById(id: Long) {
        foodLogDao.deleteById(id)
    }

    suspend fun deleteEntriesForDate(date: String) {
        foodLogDao.deleteEntriesForDate(date)
    }

    suspend fun clearAllEntries() {
        foodLogDao.clearAllEntries()
    }

    suspend fun saveUserProfile(profile: UserProfile) {
        userPrefs.saveProfile(profile)
    }

    suspend fun setOnboardingCompleted(completed: Boolean) {
        userPrefs.setOnboardingCompleted(completed)
    }

    suspend fun resetAllData() {
        foodLogDao.clearAllEntries()
        userPrefs.resetProfile()
    }

    /**
     * Seeds realistic meal log entries across the last 7 days for demo & testing
     */
    suspend fun seedSampleWeekData() {
        val days = DateUtils.getLast7DaysIso()
        val entries = mutableListOf<FoodLogEntry>()

        days.forEachIndexed { index, date ->
            // Seed varied realistic logs
            entries.add(
                FoodLogEntry(
                    foodName = "Rolled Oats with Berries",
                    category = "Grains & Bakery",
                    mealType = MealType.BREAKFAST.id,
                    gramsOrServings = 60.0,
                    unit = "g",
                    calories = 260 + (index * 15 % 40),
                    protein = 9.0,
                    carbs = 45.0,
                    fat = 4.0,
                    date = date
                )
            )
            entries.add(
                FoodLogEntry(
                    foodName = "Cafe Latte (with Milk)",
                    category = "Meals & Snacks",
                    mealType = MealType.BREAKFAST.id,
                    gramsOrServings = 250.0,
                    unit = "ml",
                    calories = 135,
                    protein = 8.0,
                    carbs = 12.0,
                    fat = 6.0,
                    date = date
                )
            )
            entries.add(
                FoodLogEntry(
                    foodName = "Grilled Chicken Breast with Quinoa",
                    category = "Protein & Meat",
                    mealType = MealType.LUNCH.id,
                    gramsOrServings = 200.0,
                    unit = "g",
                    calories = 520 + (index * 30 % 90),
                    protein = 46.0,
                    carbs = 42.0,
                    fat = 12.0,
                    date = date
                )
            )
            entries.add(
                FoodLogEntry(
                    foodName = "Raw Almonds & Apple",
                    category = "Fruits & Berries",
                    mealType = MealType.SNACKS.id,
                    gramsOrServings = 150.0,
                    unit = "g",
                    calories = 230 + (index * 20 % 50),
                    protein = 5.0,
                    carbs = 24.0,
                    fat = 14.0,
                    date = date
                )
            )
            entries.add(
                FoodLogEntry(
                    foodName = "Baked Atlantic Salmon & Sweet Potato",
                    category = "Protein & Meat",
                    mealType = MealType.DINNER.id,
                    gramsOrServings = 220.0,
                    unit = "g",
                    calories = 580 + (index * 25 % 80),
                    protein = 42.0,
                    carbs = 38.0,
                    fat = 22.0,
                    date = date
                )
            )
        }

        foodLogDao.insertEntries(entries)
    }
}
