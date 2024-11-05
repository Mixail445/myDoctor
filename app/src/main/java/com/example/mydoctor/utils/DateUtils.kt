package com.example.mydoctor.utils

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.util.Locale

object DateUtils {
    private const val DEFAULT = "yyyy-MM-dd"
    const val TIME = "HH:mm"
    const val MONTH = "dd.MM"
    private const val TIME_MILLIS = "HH:mm"

    // Форматтеры для различных форматов даты и времени
    val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern(DEFAULT)
    val timeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern(TIME)

    // Текущая дата
    val currentDate: LocalDate = LocalDate.now()

    // Форматтер для даты и времени
    val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    /**
     * Получает текущую дату в формате "dd.MM.yyyy".
     *
     * @return Строка с текущей датой.
     */
    fun getCurrentDate(): String = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))

    /**
     * Получает текущую дату в виде строки.
     *
     * @return Строка с текущей датой.
     */
    fun getCurrentDateString(): String {
        return currentDate.toString()
    }

    /**
     * Получает текущее время в формате "HH:mm".
     *
     * @return Строка с текущим временем.
     */
    fun getCurrentTimeString(): String {
        return LocalTime.now().format(timeFormatter)
    }

    /**
     * Получает начало текущей недели в формате даты.
     *
     * @return Строка с датой начала недели.
     */
    fun getStartOfWeek(): String {
        return LocalDate.now().with(DayOfWeek.MONDAY).format(DateTimeFormatter.ofPattern(DEFAULT))
    }

    /**
     * Получает конец текущей недели в формате даты.
     *
     * @return Строка с датой конца недели.
     */
    fun getEndOfWeek(): String {
        return LocalDate.now().with(DayOfWeek.SUNDAY).format(DateTimeFormatter.ofPattern(DEFAULT))
    }

    /**
     * Получает начало текущего месяца в формате даты.
     *
     * @return Строка с датой начала месяца.
     */
    fun getStartOfMonth(): String {
        return LocalDate.now().withDayOfMonth(1).format(DateTimeFormatter.ofPattern(DEFAULT))
    }

    /**
     * Получает конец текущего месяца в формате даты.
     *
     * @return Строка с датой конца месяца.
     */
    fun getEndOfMonth(): String {
        return LocalDate.now().with(TemporalAdjusters.lastDayOfMonth())
            .format(DateTimeFormatter.ofPattern(DEFAULT))
    }

    /**
     * Форматирует дату из строки в формат "d MMMM".
     *
     * @param dateString Дата в виде строки.
     * @return Отформатированная дата в виде строки.
     */
    fun formatDate(dateString: String): String {
        val date = LocalDate.parse(dateString)
        val formatter = DateTimeFormatter.ofPattern("d MMMM", Locale("ru"))
        return date.format(formatter)
    }

    /**
     * Форматирует дату в формате "MMMM yyyy 'г.'".
     *
     * @param date Дата для форматирования.
     * @return Отформатированная дата в виде строки.
     */
    fun formatMonthYear(date: LocalDate): String {
        val formatter = DateTimeFormatter.ofPattern("MMMM yyyy 'г.'", Locale("ru"))
        return date.format(formatter)
    }
}