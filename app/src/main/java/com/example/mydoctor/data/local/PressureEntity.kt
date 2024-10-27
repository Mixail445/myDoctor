package com.example.mydoctor.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mydoctor.domain.Pressure
import com.example.mydoctor.utils.DateUtils.dateFormatter
import com.example.mydoctor.utils.DateUtils.timeFormatterMillis
import java.time.LocalDate
import java.time.LocalTime

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


fun PressureEntity.toDomain(): Pressure {
    return Pressure(
        systolicPressure,
        diastolicPressure,
        pulse,
        LocalDate.parse(dateOfMeasurements, dateFormatter),
        LocalTime.parse(measurementTime, timeFormatterMillis),
        note,
        timestamp
    )
}
