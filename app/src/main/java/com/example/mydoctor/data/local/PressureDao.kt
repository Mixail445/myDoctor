package com.example.mydoctor.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PressureDao {
    @Query("SELECT * FROM PressureEntity")
    fun getAll(): Flow<List<PressureEntity>>

    @Query("SELECT * FROM PressureEntity WHERE dateOfMeasurements = :date")
    fun getPressuresByDay(date: String): Flow<List<PressureEntity>>

    @Query("SELECT * FROM PressureEntity WHERE dateOfMeasurements >= :startDate AND dateOfMeasurements <= :endDate")
    fun getPressuresByWeek(startDate: String, endDate: String): Flow<List<PressureEntity>>

    @Query("SELECT * FROM PressureEntity WHERE dateOfMeasurements >= :startDate AND dateOfMeasurements <= :endDate")
    fun getPressuresByMonth(startDate: String, endDate: String): Flow<List<PressureEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(pressure: PressureEntity)
}