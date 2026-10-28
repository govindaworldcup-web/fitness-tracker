package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoGraph
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.AutoGraph
import androidx.compose.material.icons.outlined.Calculate
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.FoodLogEntry
import com.example.data.model.MealType
import com.example.ui.screens.AddFoodBottomSheet
import com.example.ui.screens.CalculatorsScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.DiaryScreen
import com.example.ui.screens.EditFoodDialog
import com.example.ui.screens.OnboardingScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.ProgressScreen

@Composable
fun CalorieApp(
    viewModel: CalorieViewModel = viewModel()
) {
    val isOnboardingCompleted by viewModel.isOnboardingCompleted.collectAsStateWithLifecycle()
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val selectedDate by viewModel.selectedDate.collectAsStateWithLifecycle()
    val dailySummary by viewModel.dailySummary.collectAsStateWithLifecycle()
    val bmiResult by viewModel.bmiResult.collectAsStateWithLifecycle()
    val bmrResult by viewModel.bmrResult.collectAsStateWithLifecycle()
    val weekEntries by viewModel.weekEntries.collectAsStateWithLifecycle()
    val filteredCatalog by viewModel.filteredFoodCatalog.collectAsStateWithLifecycle()
    val searchQuery by viewModel.foodSearchQuery.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.foodSelectedCategory.collectAsStateWithLifecycle()
    val interactiveCalc by viewModel.interactiveCalc.collectAsStateWithLifecycle()

    var showAddFoodSheet by remember { mutableStateOf(false) }
    var activeAddMealType by remember { mutableStateOf(MealType.BREAKFAST) }
    var entryToEdit by remember { mutableStateOf<FoodLogEntry?>(null) }

    if (!isOnboardingCompleted) {
        OnboardingScreen(
            onComplete = { profile ->
                viewModel.completeOnboarding(profile)
            }
        )
        return
    }

    // BackHandler to return to Dashboard if on another tab
    if (currentTab != AppTab.DASHBOARD) {
        BackHandler {
            viewModel.setTab(AppTab.DASHBOARD)
        }
    }

    Scaffold(
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .navigationBarsPadding()
                    .testTag("bottom_nav_bar"),
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 6.dp
            ) {
                val items = listOf(
                    Triple(AppTab.DASHBOARD, Icons.Filled.Home, Icons.Outlined.Home),
                    Triple(AppTab.DIARY, Icons.Filled.DateRange, Icons.Outlined.DateRange),
                    Triple(AppTab.CALCULATOR, Icons.Filled.Calculate, Icons.Outlined.Calculate),
                    Triple(AppTab.PROGRESS, Icons.Filled.AutoGraph, Icons.Outlined.AutoGraph),
                    Triple(AppTab.PROFILE, Icons.Filled.Person, Icons.Outlined.Person)
                )

                items.forEach { (tab, filledIcon, outlinedIcon) ->
                    val isSelected = currentTab == tab
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = {
                            if (tab == AppTab.CALCULATOR) {
                                viewModel.syncInteractiveWithProfile()
                            }
                            viewModel.setTab(tab)
                        },
                        icon = {
                            Icon(
                                imageVector = if (isSelected) filledIcon else outlinedIcon,
                                contentDescription = tab.title
                            )
                        },
                        label = {
                            Text(
                                text = tab.title,
                                fontSize = 11.sp,
                                maxLines = 1
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        modifier = Modifier.testTag("nav_item_${tab.name.lowercase()}")
                    )
                }
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        val screenModifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)

        when (currentTab) {
            AppTab.DASHBOARD -> {
                DashboardScreen(
                    selectedDate = selectedDate,
                    dailySummary = dailySummary,
                    bmiResult = bmiResult,
                    bmrResult = bmrResult,
                    onPreviousDay = { viewModel.goToPreviousDay() },
                    onNextDay = { viewModel.goToNextDay() },
                    onTodayClick = { viewModel.goToToday() },
                    onAddFoodClick = { mealType ->
                        activeAddMealType = mealType
                        showAddFoodSheet = true
                    },
                    onEditEntry = { entry -> entryToEdit = entry },
                    onDeleteEntry = { entry -> viewModel.deleteFoodEntry(entry.id) },
                    onNavigateToCalculators = {
                        viewModel.syncInteractiveWithProfile()
                        viewModel.setTab(AppTab.CALCULATOR)
                    },
                    modifier = screenModifier
                )
            }
            AppTab.DIARY -> {
                DiaryScreen(
                    selectedDate = selectedDate,
                    dailySummary = dailySummary,
                    onPreviousDay = { viewModel.goToPreviousDay() },
                    onNextDay = { viewModel.goToNextDay() },
                    onTodayClick = { viewModel.goToToday() },
                    onAddFoodClick = { mealType ->
                        activeAddMealType = mealType
                        showAddFoodSheet = true
                    },
                    onEditEntry = { entry -> entryToEdit = entry },
                    onDeleteEntry = { entry -> viewModel.deleteFoodEntry(entry.id) },
                    onClearDayEntries = { viewModel.clearEntriesForDate() },
                    modifier = screenModifier
                )
            }
            AppTab.CALCULATOR -> {
                CalculatorsScreen(
                    userProfile = userProfile,
                    calcState = interactiveCalc,
                    onUpdateCalcState = { updater -> viewModel.updateInteractiveCalc(updater) },
                    onApplyToProfile = { viewModel.applyInteractiveToProfile() },
                    modifier = screenModifier
                )
            }
            AppTab.PROGRESS -> {
                ProgressScreen(
                    allEntries = weekEntries,
                    targetCalories = bmrResult.targetCalories,
                    onSelectDay = { date ->
                        viewModel.selectDate(date)
                        viewModel.setTab(AppTab.DIARY)
                    },
                    modifier = screenModifier
                )
            }
            AppTab.PROFILE -> {
                ProfileScreen(
                    userProfile = userProfile,
                    onSaveProfile = { profile -> viewModel.saveProfile(profile) },
                    onSeedSampleData = { viewModel.seedSampleData() },
                    onClearTodayEntries = { viewModel.clearEntriesForDate() },
                    onResetAllData = { viewModel.resetAllData() },
                    modifier = screenModifier
                )
            }
        }
    }

    // Modal Add Food Bottom Sheet
    if (showAddFoodSheet) {
        AddFoodBottomSheet(
            initialMealType = activeAddMealType,
            filteredFoods = filteredCatalog,
            searchQuery = searchQuery,
            onSearchQueryChange = { viewModel.foodSearchQuery.value = it },
            selectedCategory = selectedCategory,
            onCategoryChange = { viewModel.foodSelectedCategory.value = it },
            onDismiss = { showAddFoodSheet = false },
            onAddFood = { name, category, mealType, grams, unit, cals, p, c, f ->
                viewModel.addFoodEntry(
                    name = name,
                    category = category,
                    mealType = mealType,
                    gramsOrServings = grams,
                    unit = unit,
                    calories = cals,
                    protein = p,
                    carbs = c,
                    fat = f
                )
            }
        )
    }

    // Edit Existing Food Entry Dialog
    entryToEdit?.let { entry ->
        EditFoodDialog(
            entry = entry,
            onDismiss = { entryToEdit = null },
            onSave = { updated ->
                viewModel.updateFoodEntry(updated)
                entryToEdit = null
            },
            onDelete = { toDelete ->
                viewModel.deleteFoodEntry(toDelete.id)
                entryToEdit = null
            }
        )
    }
}
