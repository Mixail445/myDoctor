package com.example.mydoctor.domain

import com.example.mydoctor.data.local.PressureEntity
import kotlinx.coroutines.flow.Flow
interface PressureLocalSource {
    /**
     * Получает все записи давления из локального источника данных.
     *
     * @return Поток списка всех PressureEntity.
     */
    fun getAllPressures(): Flow<List<PressureEntity>>

    /**
     * Вставляет новую запись о давлении в локальный источник данных.
     *
     * @param pressure Объект PressureEntity для вставки.
     */
    suspend fun insertPressure(pressure: PressureEntity)

    /**
     * Получает записи давления за указанный день.
     *
     * @param date Дата для фильтрации записей.
     * @return Поток списка PressureEntity, соответствующих указанной дате.
     */
    fun getPressuresByDay(date: String): Flow<List<PressureEntity>>

    /**
     * Получает записи давления за указанный период недели.
     *
     * @param startDate Начальная дата диапазона.
     * @param endDate Конечная дата диапазона.
     * @return Поток списка PressureEntity, соответствующих указанному диапазону дат.
     */
    fun getPressuresByWeek(startDate: String, endDate: String): Flow<List<PressureEntity>>

    /**
     * Получает записи давления за указанный период месяца.
     *
     * @param startDate Начальная дата диапазона.
     * @param endDate Конечная дата диапазона.
     * @return Поток списка PressureEntity, соответствующих указанному диапазону дат.
     */
    fun getPressuresByMonth(startDate: String, endDate: String): Flow<List<PressureEntity>>
}