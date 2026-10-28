package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.FoodLogEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodLogDao {

    @Query("SELECT * FROM food_logs WHERE date = :date ORDER BY timestamp ASC")
    fun getEntriesForDate(date: String): Flow<List<FoodLogEntry>>

    @Query("SELECT * FROM food_logs ORDER BY date DESC, timestamp DESC")
    fun getAllEntries(): Flow<List<FoodLogEntry>>

    @Query("SELECT * FROM food_logs WHERE date BETWEEN :startDate AND :endDate ORDER BY date ASC, timestamp ASC")
    fun getEntriesBetweenDates(startDate: String, endDate: String): Flow<List<FoodLogEntry>>

    @Query("SELECT * FROM food_logs WHERE id = :id LIMIT 1")
    suspend fun getEntryById(id: Long): FoodLogEntry?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntry(entry: FoodLogEntry): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntries(entries: List<FoodLogEntry>)

    @Update
    suspend fun updateEntry(entry: FoodLogEntry)

    @Delete
    suspend fun deleteEntry(entry: FoodLogEntry)

    @Query("DELETE FROM food_logs WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM food_logs WHERE date = :date")
    suspend fun deleteEntriesForDate(date: String)

    @Query("DELETE FROM food_logs")
    suspend fun clearAllEntries()
}
