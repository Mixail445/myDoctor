package com.example.mydoctor.domain


import com.example.mydoctor.ui.screens.addPressure.PressureUi
import com.example.mydoctor.utils.DateUtils
import com.example.mydoctor.utils.DateUtils.dateFormatter
import java.time.LocalDate
import java.time.LocalTime

data class Pressure(
    val systolicPressure: Int,
    val diastolicPressure: Int,
    val pulse: Int? = null,
    val dateOfMeasurements: LocalDate,
    val measurementTime: LocalTime,
    val note: String? = null,
    val timestamp: Long
)

fun Pressure.toUi(): PressureUi {
    return PressureUi(
        systolicPressure,
        diastolicPressure,
        pulse,
        dateOfMeasurements.format(dateFormatter),
        measurementTime.format(DateUtils.timeFormatterMillis),
        note,
        timestamp
    )
}
