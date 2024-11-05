package com.example.mydoctor.ui.screens.addPressure

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.mydoctor.R
import com.example.mydoctor.ui.theme.BackgroundGradientEnd
import com.example.mydoctor.ui.theme.BackgroundGradientMiddle
import com.example.mydoctor.ui.theme.BackgroundGradientStart
import com.example.mydoctor.ui.theme.BlackColor
import com.example.mydoctor.ui.theme.components.BaseButton
import com.example.mydoctor.ui.theme.components.BaseEditText
import com.example.mydoctor.ui.theme.components.BodyMText
import com.example.mydoctor.ui.theme.components.BodyXsText
import com.example.mydoctor.ui.theme.components.ButtonAction
import com.example.mydoctor.ui.theme.components.DatePickerDialog
import com.example.mydoctor.ui.theme.components.TimePickerDialogs
import com.example.mydoctor.ui.theme.components.TitleText
import com.example.mydoctor.utils.Constants.EMPTY_STRING
import kotlinx.coroutines.launch

@Preview
@Composable
fun ScreenAddPressure(navController: NavController? = null) {
    var containerSize by remember { mutableStateOf(IntSize.Zero) }
    val viewModel: AddPressureVm = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // Слушаем события для навигации по экрану
    LaunchedEffect(viewModel.uiLabels) {
        viewModel.uiLabels.collect { label ->
            when (label) {
                AddPressureView.UiLabel.ShowBackScreen -> {
                    navController?.popBackStack()
                }
            }
        }
    }

    // Диалог выбора времени
    if (uiState.showTimePicker) {
        TimePickerDialogs(
            onDismissRequest = { viewModel.onEvent(AddPressureView.Event.OnDismissDialog) },
            onTimeSelected = { time ->
                viewModel.onEvent(AddPressureView.Event.OnClickTimeChanger(time))
            }
        )
    }

    // Диалог выбора даты
    if (uiState.showDataPicker) {
        DatePickerDialog(
            onDismissRequest = { viewModel.onEvent(AddPressureView.Event.OnDismissDialog) },
            onDateSelected = { date ->
                viewModel.onEvent(AddPressureView.Event.OnClickDateChanger(date.toString()))
            }
        )
    }

    // Отображение Snackbar с сообщением об ошибке
    if (uiState.showSnackBar) {
        LaunchedEffect(uiState.snackBarMessage) {
            snackbarHostState.showSnackbar(uiState.snackBarMessage)
            viewModel.onEvent(AddPressureView.Event.OnDismissDialog)
        }
    }

    // Основной Scaffold для экрана
    Scaffold(containerColor = BackgroundGradientEnd, topBar = {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 50.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            // Кнопка "Назад"
            Card(
                onClick = { navController?.popBackStack() },
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier
                    .padding(start = 16.dp)
                    .wrapContentSize()
            ) {
                Box(Modifier.size(32.dp)) {
                    Icon(
                        tint = BlackColor,
                        painter = painterResource(id = R.drawable.icon_back),
                        contentDescription = EMPTY_STRING,
                        modifier = Modifier.align(Alignment.Center).padding(5.dp)
                    )
                }
            }
            // Заголовок в верхней части экрана
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 50.dp)
            ) {
                TitleText(
                    text = stringResource(R.string.screenAddPressure_text_add_data),
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }
        }
    }, snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .onGloballyPositioned { layoutCoordinates ->
                    containerSize = layoutCoordinates.size
                }
        ) {

            val centerX = containerSize.width / 2f * 2
            val centerY = containerSize.height / 2f

            // Основной контентный макет
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                BackgroundGradientStart,
                                BackgroundGradientMiddle,
                                BackgroundGradientEnd
                            ),
                            center = Offset(centerX, centerY),
                            radius = 400f
                        )
                    ),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Поля ввода для давления и пульса
                Column(modifier = Modifier.padding(top = 24.dp)) {
                    Row(Modifier.padding(horizontal = 16.dp)) {
                        BodyMText(text = stringResource(R.string.screenAddPressure_text_blud_pressure))
                        Spacer(modifier = Modifier.weight(1f))
                        BodyMText(
                            text = stringResource(R.string.screenAddPressure_text_pulse),
                            modifier = Modifier.padding(end = 60.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(Modifier.padding(horizontal = 16.dp)) {
                        BodyXsText(
                            text = stringResource(R.string.screenAddPressure_text_sist),
                            color = Color.LightGray,
                            modifier = Modifier
                        )
                        Spacer(modifier = Modifier.width(20.dp))
                        BodyXsText(
                            text = stringResource(R.string.screenAddPressure_text_diest),
                            color = Color.LightGray,
                            modifier = Modifier
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    // Определение фокусов для полей ввода
                    val focusRequester1 = remember { FocusRequester() }
                    val focusRequester2 = remember { FocusRequester() }
                    val focusRequester3 = remember { FocusRequester() }
                    Row(Modifier.padding(horizontal = 16.dp)) {
                        BaseEditText(
                            onNextFocus = { focusRequester1.requestFocus() },
                            onValueChange = {
                                viewModel.onEvent(AddPressureView.Event.OnSystolicChange(it))
                            },
                            modifier = Modifier
                                .padding(10.dp)
                                .weight(1f),
                            placeholderText = stringResource(R.string.screenAddPressure_text_systolic)
                        )
                        BaseEditText(
                            onValueChange = {
                                viewModel.onEvent(AddPressureView.Event.OnDiastolicChange(it))
                            },
                            onNextFocus = { focusRequester2.requestFocus() },
                            modifier = Modifier
                                .padding(10.dp)
                                .weight(1f)
                                .focusRequester(focusRequester1),
                            placeholderText = stringResource(R.string.screenAddPressure_text_distolic)
                        )
                        Spacer(modifier = Modifier.width(24.dp))
                        BaseEditText(
                            onValueChange = {
                                viewModel.onEvent(AddPressureView.Event.OnPulseChange(it))
                            },
                            modifier = Modifier
                                .padding(10.dp)
                                .weight(1f)
                                .focusRequester(focusRequester2),
                            placeholderText = stringResource(R.string.screen_add_pressure_pulse)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(Modifier.padding(horizontal = 16.dp)) {
                        // Заголовки для полей выбора даты и времени
                        BodyMText(text = stringResource(R.string.screenAddPressure_text_data_changer))
                        Spacer(modifier = Modifier.weight(1f))
                        BodyMText(
                            text = stringResource(R.string.screenAddPressure_text_time_chenger),
                            modifier = Modifier.padding(end = 25.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(Modifier.padding(horizontal = 16.dp)) {
                        // Отображение текущей даты и кнопка для её изменения
                        ButtonAction(
                            text = uiState.data,
                            onClick = {
                                viewModel.onEvent(AddPressureView.Event.OnClickDateChangerButton)
                            },
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 24.dp)
                        )

                        // Кнопка для изменения времени
                        ButtonAction(
                            onClick = {
                                viewModel.onEvent(AddPressureView.Event.OnClickTimeChangerButton)
                            },
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 8.dp),
                            text = uiState.time
                        )
                    }
                    Spacer(modifier = Modifier.height(24.dp))

                    // Заголовок для секции заметок
                    BodyMText(
                        text = stringResource(R.string.screenAddPressure_text_note),
                        modifier = Modifier.padding(start = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Поле ввода для заметок, связанных с измерением давления
                    BaseEditText(
                        isNumericInput = false,
                        onValueChange = { value ->
                            viewModel.onEvent(AddPressureView.Event.OnNoteChange(value))
                        },
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 10.dp)
                            .focusRequester(focusRequester3),
                        placeholderText = stringResource(R.string.screenAddPressure_et_note)
                    )
                }
                // Кнопка сохранения для отправки данных о давлении
                BaseButton(
                    onClick = {
                        coroutineScope.launch {
                            viewModel.onEvent(AddPressureView.Event.OnClickSave)
                        }
                    },
                    isActive = uiState.isActiveButton,
                    text = stringResource(R.string.screenAddPressure_text_save),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .align(Alignment.End)
                )
            }
        }
    }
}