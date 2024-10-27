package com.example.mydoctor.ui.screens.pressure

import com.example.mydoctor.ui.theme.components.CardState
import com.example.mydoctor.utils.Constants

interface PressureView {
    data class Model(
        val data: String = Constants.TODAY_TEXT,
        val pressure: String = Constants.NO_DATA_TEXT,
        val pulse: String = Constants.EMPTY_STRING,
        val systolicData: List<Pair<String, Float>> = emptyList(),
        val diastolicData: List<Pair<String, Float>> = emptyList(),
        val pulseData: List<Pair<String, Float>> = emptyList(),
        val noteData: List<Pair<String, String>> = emptyList(),
        val mediumPressure: String = Constants.EMPTY_STRING,
        val mediumPulse: String = Constants.EMPTY_STRING,
        val dataPressure: String = Constants.EMPTY_STRING,
        val note: String = Constants.EMPTY_STRING,
        val cardState: CardState = CardState.DoubleText,
        val showFullText:Boolean = false
    )

    sealed class Event {
        data class OnClickCard(val period: Period) : Event()
    }
}

enum class Period {
    DAY, WEEK, MONTH
}