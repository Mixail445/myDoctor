package com.example.mydoctor.ui.screens.pressure

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mydoctor.data.local.toDomain
import com.example.mydoctor.domain.Pressure
import com.example.mydoctor.domain.PressureLocalSource
import com.example.mydoctor.ui.theme.components.CardState
import com.example.mydoctor.utils.Constants
import com.example.mydoctor.utils.DateUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class PressureVm @Inject constructor(private val pressureLocalSource: PressureLocalSource) :
    ViewModel() {
    private val _uiState = MutableStateFlow(PressureView.Model())
    val uiState: StateFlow<PressureView.Model> = _uiState.asStateFlow()

    private val _currentPeriod = MutableStateFlow(Period.DAY)
    val currentPeriod: StateFlow<Period> get() = _currentPeriod

    init {
        getData()
    }

    private fun getData() {
        viewModelScope.launch {
            pressureLocalSource.getAllPressures().collect { pressures ->
                updateUiState(pressures.map { it.toDomain() })
            }
        }
    }

    @SuppressLint("DefaultLocale")
    private fun updateUiState(pressures: List<Pressure>) {

        val hasData = pressures.isNotEmpty()

        val systolicData = if (hasData) {
            pressures.map { "${it.dateOfMeasurements} ${it.measurementTime}" to it.systolicPressure.toFloat() }
        } else {
            emptyList()
        }

        val diastolicData = if (hasData) {
            pressures.map { "${it.dateOfMeasurements} ${it.measurementTime}" to it.diastolicPressure.toFloat() }
        } else {
            emptyList()
        }

        val averageSystolic = if (hasData) {
            pressures.map { it.systolicPressure }.average().toFloat().roundToInt()
        } else {
            0f
        }

        val pulseData = pressures.mapNotNull { it.pulse }
        val averagePulse = if (pulseData.isNotEmpty()) {
            pulseData.average().toFloat().roundToInt()
        } else {
            0
        }

        val pulseDisplay = if (averagePulse > 0) averagePulse.toString() else Constants.EMPTY_STRING

        val averageDiastolic = if (hasData) {
            pressures.map { it.diastolicPressure }.average().toFloat().roundToInt()
        } else {
            0f
        }

        val note = pressures.firstOrNull()?.note ?: Constants.EMPTY_STRING
        val pressureDisplay = "$averageDiastolic/$averageSystolic"

        val cardState = if (note.isNotEmpty()) {
            CardState.SingleText(data = pressureDisplay, time = "${LocalDate.now()}", note = note)
        } else {
            CardState.DoubleText
        }


        _uiState.update { currentState ->
            currentState.copy(
                systolicData = systolicData,
                diastolicData = diastolicData,
                mediumPressure = pressureDisplay,
                cardState = cardState,
                mediumPulse = pulseDisplay,
                showFullText = hasData
            )
        }
    }

    fun onEvent(event: PressureView.Event) {
        when (event) {
            is PressureView.Event.OnClickCard -> handlePeriodChange(event.period)
        }
    }

    private fun handlePeriodChange(period: Period) {
        _currentPeriod.value = period
        when (period) {
            Period.DAY -> getPressuresByDay(LocalDate.now().format(DateUtils.dateFormatter))
            Period.WEEK -> getPressuresByWeek(DateUtils.getStartOfWeek(), DateUtils.getEndOfWeek())
            Period.MONTH -> getPressuresByMonth(
                DateUtils.getStartOfMonth(),
                DateUtils.getEndOfMonth()
            )
        }
    }

    private fun getPressuresByDay(date: String) {
        val formattedStartDate = DateUtils.formatDate(date)

        _uiState.update {
            it.copy(dataPressure = formattedStartDate)
        }
        viewModelScope.launch {
            pressureLocalSource.getPressuresByDay(date).collect { pressures ->
                updateUiState(pressures.map { it.toDomain() })
            }
        }
    }

    private fun getPressuresByWeek(startDate: String, endDate: String) {
        val formattedStartDate = DateUtils.formatDate(startDate)
        val formattedEndDate = DateUtils.formatDate(endDate)
        _uiState.update {
            it.copy(dataPressure = "$formattedStartDate - $formattedEndDate")
        }
        viewModelScope.launch {
            pressureLocalSource.getPressuresByWeek(startDate, endDate).collect { pressures ->
                updateUiState(pressures.map { it.toDomain() })
            }
        }
    }

    private fun getPressuresByMonth(startDate: String, endDate: String) {
        val formattedStartDate = DateUtils.formatDate(startDate)
        val formattedEndDate = DateUtils.formatDate(endDate)
        _uiState.update {
            it.copy(dataPressure = "$formattedStartDate - $formattedEndDate")
        }
        viewModelScope.launch {
            pressureLocalSource.getPressuresByMonth(startDate, endDate).collect { pressures ->
                updateUiState(pressures.map { it.toDomain() })
            }
        }
    }
}