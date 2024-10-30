package com.example.mydoctor.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "PressureEntity")
data class PressureEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val systolicPressure: Int,
    val diastolicPressure: Int,
    val pulse: Int? = null,
    val dateOfMeasurements: String,
    val measurementTime: String,
    val note: String? = null,
    val timestamp: Long
)
