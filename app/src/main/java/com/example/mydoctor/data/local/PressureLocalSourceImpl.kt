package com.example.mydoctor.data.local

import com.example.mydoctor.domain.PressureLocalSource
import com.example.mydoctor.utils.DispatchersProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class PressureLocalSourceImpl(
    private val dao: PressureDao,
    private val dispatchersProvider: DispatchersProvider
) : PressureLocalSource {

    override fun getAllPressures(): Flow<List<PressureEntity>> = dao.getAll()

    override fun getPressuresByDay(date: String): Flow<List<PressureEntity>> = dao.getPressuresByDay(date)

    override fun getPressuresByWeek(startDate: String, endDate: String): Flow<List<PressureEntity>> =
        dao.getPressuresByWeek(startDate, endDate)

    override fun getPressuresByMonth(startDate: String, endDate: String): Flow<List<PressureEntity>> =
        dao.getPressuresByMonth(startDate, endDate)

    override suspend fun insertPressure(pressure: PressureEntity) =
        withContext(dispatchersProvider.io) {
            dao.insert(pressure)
        }
}