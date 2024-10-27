package com.example.mydoctor.ui.screens.addPressure

interface AddPressureView {
    data class Model(
        val pressureUi: PressureUi,
        val data: String,
        val time: String,
        val isActiveButton: Boolean = false,
        val showDataPicker: Boolean = false,
        val showTimePicker: Boolean = false,
        val showSnackBar: Boolean = false,
        val snackBarMessage: String
    )

    sealed interface Event {
        data object Error : Event
        data object OnClickSave : Event
        data object OnClickBack : Event
        data object OnDismissDialog : Event
        data object OnClickTimeChangerButton : Event
        data object OnClickDateChangerButton : Event
        data class OnClickDateChanger(val selectedData: String) : Event
        data class OnClickTimeChanger(val selectedTime: String) : Event
        data class OnSystolicChange(val value: String) : Event
        data class OnDiastolicChange(val value: String) : Event
        data class OnPulseChange(val value: String) : Event
        data class OnNoteChange(val value: String) : Event
    }

    sealed interface UiLabel {
        data object ShowBackScreen : UiLabel
    }
}