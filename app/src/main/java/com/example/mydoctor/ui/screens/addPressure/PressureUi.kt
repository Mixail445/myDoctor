package com.example.mydoctor.ui.screens.addPressure

import com.example.mydoctor.data.local.PressureEntity
import com.example.mydoctor.utils.Constants

data class PressureUi(
    val systolicPressure: Int,
    val diastolicPressure: Int,
    val pulse: Int? = null,
    val dateOfMeasurements: String,
    val measurementTime: String,
    val note: String? = null,
    val timestamp: Long
)
fun PressureUi.toEntity(): PressureEntity {
    return PressureEntity(
        systolicPressure = this.systolicPressure,
        diastolicPressure = this.diastolicPressure,
        pulse = this.pulse ?: Constants.ZERO_INT,
        dateOfMeasurements = this.dateOfMeasurements,
        measurementTime = this.measurementTime,
        note = this.note ?:Constants.EMPTY_STRING,
        timestamp = System.currentTimeMillis()
    )
}
