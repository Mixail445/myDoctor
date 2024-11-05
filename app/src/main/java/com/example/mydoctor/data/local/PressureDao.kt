package com.example.mydoctor.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PressureDao {
    /**
     * Получает все записи давления из базы данных.
     *
     * @return Поток списка всех PressureEntity.
     */
    @Query("SELECT * FROM PressureEntity")
    fun getAll(): Flow<List<PressureEntity>>

    /**
     * Получает записи давления за указанный день.
     *
     * @param date Дата для фильтрации записей.
     * @return Поток списка PressureEntity, соответствующих указанной дате.
     */
    @Query("SELECT * FROM PressureEntity WHERE dateOfMeasurements = :date")
    fun getPressuresByDay(date: String): Flow<List<PressureEntity>>

    /**
     * Получает записи давления за указанный период недели.
     *
     * @param startDate Начальная дата диапазона.
     * @param endDate Конечная дата диапазона.
     * @return Поток списка PressureEntity, соответствующих указанному диапазону дат.
     */
    @Query("SELECT * FROM PressureEntity WHERE dateOfMeasurements >= :startDate AND dateOfMeasurements <= :endDate")
    fun getPressuresByWeek(startDate: String, endDate: String): Flow<List<PressureEntity>>

    /**
     * Получает записи давления за указанный период месяца.
     *
     * @param startDate Начальная дата диапазона.
     * @param endDate Конечная дата диапазона.
     * @return Поток списка PressureEntity, соответствующих указанному диапазону дат.
     */
    @Query("SELECT * FROM PressureEntity WHERE dateOfMeasurements >= :startDate AND dateOfMeasurements <= :endDate")
    fun getPressuresByMonth(startDate: String, endDate: String): Flow<List<PressureEntity>>

    /**
     * Вставляет новую запись о давлении в базу данных.
     * Если запись с таким же первичным ключом уже существует, она будет заменена.
     *
     * @param pressure Объект PressureEntity для вставки в базу данных.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(pressure: PressureEntity)
}