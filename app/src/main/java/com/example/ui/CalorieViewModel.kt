package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.FoodCatalog
import com.example.data.local.UserPreferencesRepository
import com.example.data.model.ActivityLevel
import com.example.data.model.BmiResult
import com.example.data.model.BmrResult
import com.example.data.model.DailyNutritionSummary
import com.example.data.model.FitnessGoal
import com.example.data.model.FoodItem
import com.example.data.model.FoodLogEntry
import com.example.data.model.MealType
import com.example.data.model.Sex
import com.example.data.model.UserProfile
import com.example.data.repository.CalorieRepository
import com.example.util.DateUtils
import com.example.util.HealthCalculator
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

enum class AppTab(val title: String) {
    DASHBOARD("Dashboard"),
    DIARY("Food Diary"),
    CALCULATOR("Health Tools"),
    PROGRESS("Progress"),
    PROFILE("Profile")
}

data class InteractiveCalcState(
    val age: Int = 26,
    val sex: Sex = Sex.MALE,
    val heightCm: Float = 175f,
    val weightKg: Float = 72f,
    val activityLevel: ActivityLevel = ActivityLevel.MODERATE,
    val goal: FitnessGoal = FitnessGoal.WEIGHT_LOSS
)

class CalorieViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: CalorieRepository

    init {
        val database = AppDatabase.getInstance(application)
        val userPrefs = UserPreferencesRepository(application)
        repository = CalorieRepository(database.foodLogDao(), userPrefs)
    }

    val isOnboardingCompleted: StateFlow<Boolean> = repository.isOnboardingCompleted
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    val userProfile: StateFlow<UserProfile> = repository.userProfile
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserProfile())

    private val _currentTab = MutableStateFlow(AppTab.DASHBOARD)
    val currentTab: StateFlow<AppTab> = _currentTab.asStateFlow()

    private val _selectedDate = MutableStateFlow(DateUtils.getTodayIso())
    val selectedDate: StateFlow<String> = _selectedDate.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val selectedDateEntries: StateFlow<List<FoodLogEntry>> = _selectedDate
        .flatMapLatest { date -> repository.getEntriesForDate(date) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val bmiResult: StateFlow<BmiResult> = userProfile.combine(_currentTab) { profile, _ ->
        HealthCalculator.calculateBmi(profile.weightKg, profile.heightCm)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        HealthCalculator.calculateBmi(72f, 175f)
    )

    val bmrResult: StateFlow<BmrResult> = userProfile.combine(_currentTab) { profile, _ ->
        HealthCalculator.computeFullBmrResult(
            weightKg = profile.weightKg,
            heightCm = profile.heightCm,
            age = profile.age,
            sex = profile.sex,
            activityLevel = profile.activityLevel,
            goal = profile.fitnessGoal,
            customTarget = profile.customCalorieTarget
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        HealthCalculator.computeFullBmrResult(72f, 175f, 26, Sex.MALE, ActivityLevel.MODERATE, FitnessGoal.WEIGHT_LOSS)
    )

    val dailySummary: StateFlow<DailyNutritionSummary> = combine(
        selectedDateEntries,
        bmrResult,
        _selectedDate
    ) { entries, bmrRes, date ->
        val totalCalories = entries.sumOf { it.calories }
        val totalProtein = (entries.sumOf { it.protein } * 10.0).roundToInt() / 10.0
        val totalCarbs = (entries.sumOf { it.carbs } * 10.0).roundToInt() / 10.0
        val totalFat = (entries.sumOf { it.fat } * 10.0).roundToInt() / 10.0
        val targetCalories = bmrRes.targetCalories
        val remaining = targetCalories - totalCalories

        val mealCaloriesMap = MealType.entries.associateWith { mealType ->
            entries.filter { it.mealType.equals(mealType.id, ignoreCase = true) }.sumOf { it.calories }
        }

        val entriesByMealMap = MealType.entries.associateWith { mealType ->
            entries.filter { it.mealType.equals(mealType.id, ignoreCase = true) }
        }

        DailyNutritionSummary(
            date = date,
            targetCalories = targetCalories,
            totalCalories = totalCalories,
            remainingCalories = remaining,
            totalProtein = totalProtein,
            totalCarbs = totalCarbs,
            totalFat = totalFat,
            mealCalories = mealCaloriesMap,
            entriesByMeal = entriesByMealMap
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        DailyNutritionSummary(
            date = DateUtils.getTodayIso(),
            targetCalories = 2000,
            totalCalories = 0,
            remainingCalories = 2000,
            totalProtein = 0.0,
            totalCarbs = 0.0,
            totalFat = 0.0,
            mealCalories = MealType.entries.associateWith { 0 },
            entriesByMeal = MealType.entries.associateWith { emptyList() }
        )
    )

    // Last 7 days entries for progress trends
    val weekEntries: StateFlow<List<FoodLogEntry>> = repository.getAllEntries()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Catalog search & filter state
    val foodSearchQuery = MutableStateFlow("")
    val foodSelectedCategory = MutableStateFlow("All")

    val filteredFoodCatalog: StateFlow<List<FoodItem>> = combine(
        foodSearchQuery,
        foodSelectedCategory
    ) { query, category ->
        val q = query.trim().lowercase()
        FoodCatalog.sampleFoods.filter { food ->
            val matchesCategory = category == "All" || food.category.equals(category, ignoreCase = true)
            val matchesQuery = q.isEmpty() || food.name.lowercase().contains(q) || food.category.lowercase().contains(q)
            matchesCategory && matchesQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), FoodCatalog.sampleFoods)

    // Interactive calculator test state
    val interactiveCalc = MutableStateFlow(InteractiveCalcState())

    fun updateInteractiveCalc(updater: (InteractiveCalcState) -> InteractiveCalcState) {
        interactiveCalc.value = updater(interactiveCalc.value)
    }

    fun syncInteractiveWithProfile() {
        val profile = userProfile.value
        interactiveCalc.value = InteractiveCalcState(
            age = profile.age,
            sex = profile.sex,
            heightCm = profile.heightCm,
            weightKg = profile.weightKg,
            activityLevel = profile.activityLevel,
            goal = profile.fitnessGoal
        )
    }

    fun applyInteractiveToProfile() {
        val current = interactiveCalc.value
        val updated = userProfile.value.copy(
            age = current.age,
            sex = current.sex,
            heightCm = current.heightCm,
            weightKg = current.weightKg,
            activityLevel = current.activityLevel,
            fitnessGoal = current.goal
        )
        saveProfile(updated)
    }

    fun setTab(tab: AppTab) {
        _currentTab.value = tab
    }

    fun selectDate(date: String) {
        _selectedDate.value = date
    }

    fun goToPreviousDay() {
        _selectedDate.value = DateUtils.addDaysToIso(_selectedDate.value, -1)
    }

    fun goToNextDay() {
        _selectedDate.value = DateUtils.addDaysToIso(_selectedDate.value, 1)
    }

    fun goToToday() {
        _selectedDate.value = DateUtils.getTodayIso()
    }

    fun addFoodEntry(
        name: String,
        category: String,
        mealType: MealType,
        gramsOrServings: Double,
        unit: String,
        calories: Int,
        protein: Double,
        carbs: Double,
        fat: Double,
        date: String = _selectedDate.value
    ) {
        viewModelScope.launch {
            val entry = FoodLogEntry(
                foodName = name,
                category = category,
                mealType = mealType.id,
                gramsOrServings = gramsOrServings,
                unit = unit,
                calories = calories,
                protein = protein,
                carbs = carbs,
                fat = fat,
                date = date
            )
            repository.insertEntry(entry)
        }
    }

    fun updateFoodEntry(entry: FoodLogEntry) {
        viewModelScope.launch {
            repository.updateEntry(entry)
        }
    }

    fun deleteFoodEntry(id: Long) {
        viewModelScope.launch {
            repository.deleteById(id)
        }
    }

    fun clearEntriesForDate(date: String = _selectedDate.value) {
        viewModelScope.launch {
            repository.deleteEntriesForDate(date)
        }
    }

    fun saveProfile(profile: UserProfile) {
        viewModelScope.launch {
            repository.saveUserProfile(profile)
        }
    }

    fun completeOnboarding(profile: UserProfile) {
        viewModelScope.launch {
            repository.saveUserProfile(profile)
            repository.setOnboardingCompleted(true)
        }
    }

    fun seedSampleData() {
        viewModelScope.launch {
            repository.seedSampleWeekData()
        }
    }

    fun resetAllData() {
        viewModelScope.launch {
            repository.resetAllData()
        }
    }
}
