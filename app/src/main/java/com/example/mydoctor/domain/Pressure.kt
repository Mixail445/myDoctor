package com.example.mydoctor.domain

import com.example.mydoctor.data.local.PressureEntity
import com.example.mydoctor.utils.Constants

data class Pressure(
    val systolicPressure: Int,
    val diastolicPressure: Int,
    val pulse: Int? = null,
    val dateOfMeasurements: String,
    val measurementTime: String,
    val note: String? = null,
    val timestamp: Long
)

fun Pressure.mapToEntity() = PressureEntity(
    id = Constants.ZERO_LONG,
    systolicPressure,
    diastolicPressure,
    pulse,
    dateOfMeasurements,
    measurementTime,
    note,
    timestamp
)

