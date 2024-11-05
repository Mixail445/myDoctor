package com.example.mydoctor.ui.screens.pressure

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mydoctor.data.local.PressureEntity
import com.example.mydoctor.domain.PressureLocalSource
import com.example.mydoctor.ui.screens.addPressure.PressureDialogData
import com.example.mydoctor.ui.theme.components.CardState
import com.example.mydoctor.ui.theme.components.PressurePoint
import com.example.mydoctor.utils.Constants
import com.example.mydoctor.utils.DateUtils
import com.example.mydoctor.utils.DateUtils.dateTimeFormatter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class PressureVm @Inject constructor(private val pressureLocalSource: PressureLocalSource) :
    ViewModel() {

    private val _uiState = MutableStateFlow(
        PressureView.Model(
            data = Constants.EMPTY_STRING,
            pressure = Constants.EMPTY_STRING
        )
    )
    val uiState: StateFlow<PressureView.Model> = _uiState.asStateFlow()

    private val _currentPeriod = MutableStateFlow(Period.DAY)

    private val _pressurePoints = MutableStateFlow<List<PressurePoint>>(emptyList())
    val pressurePoints: StateFlow<List<PressurePoint>> get() = _pressurePoints

    init {
        getData()
    }

    /**
     * Получает данные о давлениях из локального источника и обновляет состояние UI.
     */
    private fun getData() {
        viewModelScope.launch {
            pressureLocalSource.getAllPressures().collect { pressures ->
                updateUiState(pressures)
            }
        }
    }

    /**
     * <h1>Обновление состояния пользовательского интерфейса</h1>
     * Обновляет UI на основе полученных данных о давлении, группируя их по текущему периоду
     * и вычисляя средние значения.
     *
     * @param pressures Список измерений давления.
     */
    private fun updateUiState(pressures: List<PressureEntity>) {
        val hasData = pressures.isNotEmpty()
        val points = pressures.map { pressure ->
            PressurePoint(
                dateTime = LocalDateTime.parse(
                    "${pressure.dateOfMeasurements} ${pressure.measurementTime}",
                    dateTimeFormatter
                ),
                systolic = pressure.systolicPressure.toFloat(),
                diastolic = pressure.diastolicPressure.toFloat(),
                pulse = pressure.pulse?.toFloat() ?: 0f,
                note = pressure.note ?: ""
            )
        }.sortedBy { it.dateTime }

        when (_currentPeriod.value) {
            Period.DAY -> {
                points.groupBy { it.dateTime.hour }.map { (hour, pointList) ->
                    hour to pointList.map { it.systolic }.average().toFloat()
                }
            }

            Period.WEEK -> {
                points.groupBy { it.dateTime.toLocalDate() }.map { (date, pointList) ->
                    date.toString() to pointList.map { it.systolic }.average().toFloat()
                }
            }

            Period.MONTH -> {
                points.groupBy { it.dateTime.dayOfMonth }.map { (day, pointList) ->
                    day.toString() to pointList.map { it.systolic }.average().toFloat()
                }
            }
        }

        _pressurePoints.value = points

        _uiState.update { currentState ->
            currentState.copy(
                mediumPressure = points.map { it.systolic }.average().toString(),
                showFullText = hasData,
                cardState = CardState.Empty,
                data = DateUtils.formatMonthYear(LocalDate.now())
            )
        }
    }

    /**
     * Обрабатывает события, поступающие из пользовательского интерфейса.
     *
     * @param event Событие, которое необходимо обработать.
     */
    fun onEvent(event: PressureView.Event) {
        when (event) {
            is PressureView.Event.OnClickCard -> handlePeriodChange(event.period)
            is PressureView.Event.ShowDialogForCharts -> handlerClickShowDialog(
                systolic = event.systolic,
                data = event.data,
                note = event.note,
                pulse = event.pulse,
                diastolic = event.diastolic,
            )
        }
    }

    /**
     * Закрывает диалоговое окно, обновляя состояние UI.
     */
    fun closeDialog() {
        _uiState.update { currentState ->
            currentState.copy(isDialogVisible = false)
        }
    }

    /**
     * Обрабатывает нажатие для отображения диалогового окна с информацией о показаниях давления.
     *
     * @param data Дата измерения.
     * @param note Заметка о измерении.
     * @param pulse Пульс.
     * @param diastolic Диастолическое давление.
     * @param systolic Систолическое давление.
     */
    private fun handlerClickShowDialog(
        data: String,
        note: String,
        pulse: String,
        diastolic: String,
        systolic: String
    ) {
        _uiState.value = _uiState.value.copy(
            showDialogWithInfoPoints = true,
            isDialogVisible = true,
            dialogData = PressureDialogData(
                systolic = systolic,
                diastolic = diastolic,
                pulse = pulse,
                note = note,
                data = data
            ),
            cardState = CardState.SingleText(data, data, note)
        )
    }

    /**
     * Обрабатывает изменение периода для отображения данных о давлении.
     *
     * @param period Новый период (день, неделя или месяц).
     */
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

    /**
     * Получает данные о давлении за указанный день и обновляет состояние UI.
     *
     * @param date Дата для получения данных о давлении.
     */
    private fun getPressuresByDay(date: String) {
        val formattedStartDate = DateUtils.formatDate(date)

        _uiState.update {
            it.copy(dataPressure = formattedStartDate)
        }

        viewModelScope.launch {
            pressureLocalSource.getPressuresByDay(date).collect { pressures ->
                updateUiState(pressures)
            }
        }
    }

    /**
     * Получает данные о давлении за указанный период недели и обновляет состояние UI.
     *
     * @param startDate Начальная дата недели.
     * @param endDate Конечная дата недели.
     */
    private fun getPressuresByWeek(startDate: String, endDate: String) {
        val formattedStartDate = DateUtils.formatDate(startDate)
        val formattedEndDate = DateUtils.formatDate(endDate)

        _uiState.update {
            it.copy(dataPressure = "$formattedStartDate - $formattedEndDate")
        }

        viewModelScope.launch {
            pressureLocalSource.getPressuresByWeek(startDate, endDate).collect { pressures ->
                updateUiState(pressures)
            }
        }
    }

    /**
     * Получает данные о давлении за указанный период месяца и обновляет состояние UI.
     *
     * @param startDate Начальная дата месяца.
     * @param endDate Конечная дата месяца.
     */
    private fun getPressuresByMonth(startDate: String, endDate: String) {
        val formattedStartDate = DateUtils.formatDate(startDate)
        val formattedEndDate = DateUtils.formatDate(endDate)

        _uiState.update {
            it.copy(dataPressure = "$formattedStartDate - $formattedEndDate")
        }

        viewModelScope.launch {
            pressureLocalSource.getPressuresByMonth(startDate, endDate).collect { pressures ->
                updateUiState(pressures)
            }
        }
    }
}