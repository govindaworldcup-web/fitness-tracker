package com.example.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.data.model.ActivityLevel
import com.example.data.model.FitnessGoal
import com.example.data.model.Sex
import com.example.data.model.UserProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.userDataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class UserPreferencesRepository(private val context: Context) {

    companion object {
        private val KEY_ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
        private val KEY_AGE = intPreferencesKey("profile_age")
        private val KEY_SEX = stringPreferencesKey("profile_sex")
        private val KEY_HEIGHT = floatPreferencesKey("profile_height")
        private val KEY_WEIGHT = floatPreferencesKey("profile_weight")
        private val KEY_ACTIVITY = stringPreferencesKey("profile_activity")
        private val KEY_GOAL = stringPreferencesKey("profile_goal")
        private val KEY_CUSTOM_TARGET = intPreferencesKey("profile_custom_target")
    }

    val isOnboardingCompleted: Flow<Boolean> = context.userDataStore.data.map { prefs ->
        prefs[KEY_ONBOARDING_COMPLETED] ?: false
    }

    val userProfile: Flow<UserProfile> = context.userDataStore.data.map { prefs ->
        UserProfile(
            age = prefs[KEY_AGE] ?: 26,
            sex = Sex.fromString(prefs[KEY_SEX] ?: Sex.MALE.name),
            heightCm = prefs[KEY_HEIGHT] ?: 175f,
            weightKg = prefs[KEY_WEIGHT] ?: 72f,
            activityLevel = ActivityLevel.fromString(prefs[KEY_ACTIVITY] ?: ActivityLevel.MODERATE.name),
            fitnessGoal = FitnessGoal.fromString(prefs[KEY_GOAL] ?: FitnessGoal.WEIGHT_LOSS.name),
            customCalorieTarget = prefs[KEY_CUSTOM_TARGET] ?: 0
        )
    }

    suspend fun saveProfile(profile: UserProfile) {
        context.userDataStore.edit { prefs ->
            prefs[KEY_AGE] = profile.age
            prefs[KEY_SEX] = profile.sex.name
            prefs[KEY_HEIGHT] = profile.heightCm
            prefs[KEY_WEIGHT] = profile.weightKg
            prefs[KEY_ACTIVITY] = profile.activityLevel.name
            prefs[KEY_GOAL] = profile.fitnessGoal.name
            prefs[KEY_CUSTOM_TARGET] = profile.customCalorieTarget
        }
    }

    suspend fun setOnboardingCompleted(completed: Boolean) {
        context.userDataStore.edit { prefs ->
            prefs[KEY_ONBOARDING_COMPLETED] = completed
        }
    }

    suspend fun resetProfile() {
        context.userDataStore.edit { prefs ->
            prefs.remove(KEY_AGE)
            prefs.remove(KEY_SEX)
            prefs.remove(KEY_HEIGHT)
            prefs.remove(KEY_WEIGHT)
            prefs.remove(KEY_ACTIVITY)
            prefs.remove(KEY_GOAL)
            prefs.remove(KEY_CUSTOM_TARGET)
            prefs[KEY_ONBOARDING_COMPLETED] = false
        }
    }
}
