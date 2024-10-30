package com.example.mydoctor.ui.screens.pressure

import com.example.mydoctor.ui.screens.addPressure.PressureDialogData
import com.example.mydoctor.ui.theme.components.CardState
import com.example.mydoctor.utils.Constants

interface PressureView {
    data class Model(
        val data: String,
        val pressure: String,
        val pulse: String = Constants.EMPTY_STRING,
        val mediumPressure: String = Constants.EMPTY_STRING,
        val mediumPulse: String = Constants.EMPTY_STRING,
        val dataPressure: String = Constants.EMPTY_STRING,
        val note: String = Constants.EMPTY_STRING,
        val cardState: CardState = CardState.DoubleText,
        val showFullText: Boolean = false,
        val showDialogWithInfoPoints: Boolean = false,
        val isDialogVisible: Boolean = false,
        val dialogData: PressureDialogData = PressureDialogData()
    )

    sealed class Event {
        data class OnClickCard(val period: Period) : Event()
        data class ShowDialogForCharts(
            val systolic: String,
            val diastolic: String,
            val pulse: String,
            val note: String,
            val data: String
        ) : Event()
    }
}

enum class Period {
    DAY, WEEK, MONTH
}