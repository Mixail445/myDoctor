package com.example.mydoctor.ui.screens.addPressure

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mydoctor.R
import com.example.mydoctor.domain.Pressure
import com.example.mydoctor.domain.PressureLocalSource
import com.example.mydoctor.domain.mapToEntity
import com.example.mydoctor.utils.Constants.EMPTY_STRING
import com.example.mydoctor.utils.Constants.ZERO_INT
import com.example.mydoctor.utils.DateUtils
import com.example.mydoctor.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class AddPressureVm @Inject constructor(
    private val pressureLocalSource: PressureLocalSource,
    private val resource: Resource
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        AddPressureView.Model(
            Pressure(ZERO_INT, ZERO_INT, null, EMPTY_STRING, EMPTY_STRING, EMPTY_STRING, 1),
            EMPTY_STRING,
            EMPTY_STRING,
            snackBarMessage = EMPTY_STRING
        )
    )
    val uiState: StateFlow<AddPressureView.Model> = _uiState.asStateFlow()

    private val _uiLabels = MutableSharedFlow<AddPressureView.UiLabel>()
    val uiLabels: SharedFlow<AddPressureView.UiLabel> get() = _uiLabels

    init {
        val currentDate = DateUtils.getCurrentDateString()
        val currentTime = DateUtils.getCurrentTimeString()

        _uiState.update {
            it.copy(data = currentDate, time = currentTime)
        }
    }

    /**
     * Обрабатывает события, поступающие из пользовательского интерфейса.
     *
     * @param event Событие, которое необходимо обработать.
     */
    fun onEvent(event: AddPressureView.Event) {
        when (event) {
            is AddPressureView.Event.OnClickBack -> handlerClickBack()
            is AddPressureView.Event.OnClickSave -> handleSaveEvent()
            is AddPressureView.Event.OnClickDateChanger -> handlerClickDataChanger(event.selectedData)
            is AddPressureView.Event.OnClickTimeChanger -> handlerClickTimeChanger(event.selectedTime)
            is AddPressureView.Event.OnSystolicChange -> handleSystolicChange(event.value)
            is AddPressureView.Event.OnDiastolicChange -> handleDiastolicChange(event.value)
            is AddPressureView.Event.OnPulseChange -> handlePulseChange(event.value)
            is AddPressureView.Event.OnNoteChange -> handleNoteChange(event.value)
            AddPressureView.Event.Error -> showErrorSnackBar()
            AddPressureView.Event.OnDismissDialog -> dismissDialog()
            AddPressureView.Event.OnClickTimeChangerButton -> showTimePicker()
            AddPressureView.Event.OnClickDateChangerButton -> showDatePicker()
        }
    }

    /**
     * <h1>Handle Save Event</h1>
     * Инициирует процесс сохранения данных о давлении асинхронно.
     */
    private fun handleSaveEvent() {
        viewModelScope.launch {
            handlerClickSave()
        }
    }

    /**
     * Обрабатывает изменение значения систолического давления.
     *
     * @param value Новое значение систолического давления.
     */
    private fun handleSystolicChange(value: String) {
        val newSystolic = value.toIntOrNull() ?: ZERO_INT
        updatePressureUi { it.copy(systolicPressure = newSystolic) }
        updateButtonState()
    }

    /**
     * Обрабатывает изменение значения диастолического давления.
     *
     * @param value Новое значение диастолического давления.
     */
    private fun handleDiastolicChange(value: String) {
        val newDiastolic = value.toIntOrNull() ?: ZERO_INT
        updatePressureUi { it.copy(diastolicPressure = newDiastolic) }
        updateButtonState()
    }

    /**
     * Обрабатывает изменение значения пульса.
     *
     * @param value Новое значение пульса.
     */
    private fun handlePulseChange(value: String) {
        updatePressureUi { it.copy(pulse = value.toIntOrNull() ?: ZERO_INT) }
    }

    /**
     * Обрабатывает изменение заметки.
     *
     * @param value Новая заметка.
     */
    private fun handleNoteChange(value: String) {
        updatePressureUi { it.copy(note = value) }
    }

    /**
     * Показывает сообщение об ошибке в виде Snackbar.
     */
    private fun showErrorSnackBar() {
        viewModelScope.launch {
            _uiState.update { it.copy(showSnackBar = true) }
        }
    }

    /**
     * Закрывает диалоговое окно и сбрасывает состояния выбора даты и времени.
     */
    private fun dismissDialog() {
        _uiState.update {
            it.copy(
                showDataPicker = false,
                showTimePicker = false,
                showSnackBar = false
            )
        }
    }

    /**
     * Показывает диалог выбора времени.
     */
    private fun showTimePicker() {
        _uiState.update { it.copy(showTimePicker = true) }
    }

    /**
     * Показывает диалог выбора даты.
     */
    private fun showDatePicker() {
        _uiState.update { it.copy(showDataPicker = true) }
    }

    /**
     * <h1>Update Button State</h1>
     * Проверяет корректность систолического и диастолического давления для активации или деактивации кнопки сохранения.
     */
    private fun updateButtonState() {
        val systolicValid = _uiState.value.pressureUi.systolicPressure > ZERO_INT
        val diastolicValid = _uiState.value.pressureUi.diastolicPressure > ZERO_INT
        _uiState.update { it.copy(isActiveButton = systolicValid && diastolicValid) }
    }

    /**
     * <h1>Handle Time Change Event</h1>
     * Проверяет и обновляет выбранное время
     *
     * @param selectedTime Выбранное время в виде строки.
     */
    private fun handlerClickTimeChanger(selectedTime: String) {
        val selectedLocalTime = LocalTime.parse(selectedTime, DateUtils.timeFormatter)
        val currentT = LocalTime.parse(DateUtils.getCurrentTimeString(), DateUtils.timeFormatter)

        if (selectedLocalTime.isBefore(currentT)) {
            showError(resource.getString(R.string.vm_helper_time))
        } else {
            _uiState.update { it.copy(time = selectedTime, showTimePicker = false) }
        }
    }

    /**
     * <h1>Handle Date Change Event</h1>
     * Проверяет и обновляет выбранную дату в состоянии UI, если она не раньше текущей даты.
     *
     * @param selectData Выбранная дата в виде строки.
     */
    private fun handlerClickDataChanger(selectData: String) {
        val selectedLocalDate = LocalDate.parse(selectData, DateUtils.dateFormatter)

        if (selectedLocalDate.isBefore(DateUtils.currentDate)) {
            showError(resource.getString(R.string.vm_helper_date))
        } else {
            _uiState.update { it.copy(data = selectData, showDataPicker = false) }
        }
    }

    /**
     * Сохраняет данные о давлении в локальном источнике данных и обновляет состояние UI.
     */
    private suspend fun handlerClickSave() {
        val currentDate = DateUtils.getCurrentDateString()
        val currentTime = DateUtils.getCurrentTimeString()

        val selectedDate = uiState.value.data.ifEmpty { currentDate }
        val selectedTime = uiState.value.time.ifEmpty { currentTime }

        val pressureEntity = _uiState.value.pressureUi.copy(
            dateOfMeasurements = selectedDate,
            measurementTime = selectedTime
        )

        pressureLocalSource.insertPressure(pressureEntity.mapToEntity())

        _uiLabels.emit(AddPressureView.UiLabel.ShowBackScreen)
    }

    /**
     * Обрабатывает нажатие кнопки "Назад".
     */
    private fun handlerClickBack() {}

    /**
     * Показывает сообщение об ошибке с переданным текстом.
     *
     * @param message Сообщение об ошибке.
     */
    private fun showError(message: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(showSnackBar = true, snackBarMessage = message)
            }
        }
    }


    /**
     * Обновляет состояние пользовательского интерфейса для давления на основе переданного обновления.
     *
     * @param update Функция для обновления состояния давления.
     */
    private fun updatePressureUi(update: (Pressure) -> Pressure) {
        _uiState.value = _uiState.value.copy(
            pressureUi = update(_uiState.value.pressureUi)
        )
    }
}
