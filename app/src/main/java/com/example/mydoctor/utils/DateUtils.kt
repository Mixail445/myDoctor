package com.example.mydoctor.utils

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.util.Locale

object DateUtils {
    private const val DEFAULT = "yyyy-MM-dd"
    private const val TIME = "HH:mm"
    private const val TIME_MILLIS = "HH:mm"

    val timeFormatterMillis: DateTimeFormatter = DateTimeFormatter.ofPattern(TIME_MILLIS)
    val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern(DEFAULT)
    val timeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern(TIME)

    val currentDate: LocalDate = LocalDate.now()
    val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")


    fun getCurrentDate(): String = LocalDate.now().format("dd.MM.yyyy")


    private fun LocalDate.format(pattern: String): String {
        return this.format(DateTimeFormatter.ofPattern(pattern))
    }

    fun getCurrentDateString(): String {
        return currentDate.toString()
    }

    fun getCurrentTimeString(): String {
        return LocalTime.now().format(timeFormatter)
    }

    fun getStartOfWeek(): String {
        return LocalDate.now().with(DayOfWeek.MONDAY).format(DateTimeFormatter.ofPattern(DEFAULT))
    }

    fun getEndOfWeek(): String {
        return LocalDate.now().with(DayOfWeek.SUNDAY).format(DateTimeFormatter.ofPattern(DEFAULT))
    }

    fun getStartOfMonth(): String {
        return LocalDate.now().withDayOfMonth(1).format(DateTimeFormatter.ofPattern(DEFAULT))
    }

    fun getEndOfMonth(): String {
        return LocalDate.now().with(TemporalAdjusters.lastDayOfMonth())
            .format(DateTimeFormatter.ofPattern(DEFAULT))
    }

    fun formatDate(dateString: String): String {
        val date = LocalDate.parse(dateString)
        val formatter = DateTimeFormatter.ofPattern("d MMMM", Locale("ru"))
        return date.format(formatter)
    }
}