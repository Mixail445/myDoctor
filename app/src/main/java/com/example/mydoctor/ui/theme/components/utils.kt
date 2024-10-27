package com.example.mydoctor.ui.theme.components

import android.annotation.SuppressLint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.mydoctor.ui.theme.ActiveButtonColor
import com.example.mydoctor.ui.theme.BackgroundColor
import com.example.mydoctor.ui.theme.CancelButtonColor
import com.example.mydoctor.ui.theme.TitleTextColor
import com.example.mydoctor.utils.Constants.CANCEL_BUTTON_TEXT
import com.example.mydoctor.utils.Constants.OK_BUTTON_TEXT
import com.example.mydoctor.utils.Constants.TIME_PICKER_TITLE
import java.time.LocalDate
import java.util.Calendar

@Composable
fun DrawCircle(color: Color, size: Dp, padding: PaddingValues) {
    Canvas(
        modifier = Modifier
            .padding(padding)
            .size(size)
            .background(Color.Transparent)
    ) {
        drawCircle(color = color, radius = size.toPx())
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun DatePickerDialog(
    onDismissRequest: () -> Unit = {},
    onDateSelected: (LocalDate) -> Unit = {},
) {
    val datePickerState = rememberDatePickerState()

    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = BackgroundColor,
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                DatePicker(
                    state = datePickerState,
                    colors = DatePickerDefaults.colors(containerColor = BackgroundColor),
                    modifier = Modifier.fillMaxWidth()
                )
                Row(
                    horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = onDismissRequest) {
                        Text(CANCEL_BUTTON_TEXT, color = CancelButtonColor)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    TextButton(onClick = {
                        val selectedMillis = datePickerState.selectedDateMillis
                        if (selectedMillis != null) {
                            val date = LocalDate.ofEpochDay(selectedMillis / (1000 * 60 * 60 * 24))
                            onDateSelected(date)
                        }
                    }) {
                        Text(OK_BUTTON_TEXT, color = ActiveButtonColor)
                    }
                }
            }
        }
    }
}

@SuppressLint("DefaultLocale")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialogs(
    onDismissRequest: () -> Unit = {}, onTimeSelected: (String) -> Unit = {}
) {
    val currentTime = Calendar.getInstance()
    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = true
    )

    AlertDialog(containerColor = Color.White,
        onDismissRequest = onDismissRequest,
        title = { Text(TIME_PICKER_TITLE, color = TitleTextColor) },
        text = {
            TimePicker(
                state = timePickerState, colors = TimePickerDefaults.colors(
                    clockDialSelectedContentColor = Color.Black,
                    clockDialColor = BackgroundColor,
                    selectorColor = ActiveButtonColor,
                    timeSelectorSelectedContainerColor = Color.White,
                    periodSelectorSelectedContentColor = Color.Black,
                    periodSelectorUnselectedContainerColor = Color.Red,
                    timeSelectorUnselectedContainerColor = Color.White
                )
            )
        },
        confirmButton = {
            TextButton(onClick = {
                val selectedHour = timePickerState.hour
                val selectedMinute = timePickerState.minute
                val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                onTimeSelected(formattedTime)
            }) {
                Text(OK_BUTTON_TEXT, color = ActiveButtonColor)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(CANCEL_BUTTON_TEXT, color = CancelButtonColor)
            }
        })
}