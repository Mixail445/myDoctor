package com.example.mydoctor.domain

import com.example.mydoctor.data.local.PressureEntity
import kotlinx.coroutines.flow.Flow
interface PressureLocalSource {
    fun getAllPressures(): Flow<List<PressureEntity>>

    suspend fun insertPressure(pressure: PressureEntity)

    fun getPressuresByDay(date: String): Flow<List<PressureEntity>>

    fun getPressuresByWeek(startDate: String, endDate: String): Flow<List<PressureEntity>>

    fun getPressuresByMonth(startDate: String, endDate: String): Flow<List<PressureEntity>>
}