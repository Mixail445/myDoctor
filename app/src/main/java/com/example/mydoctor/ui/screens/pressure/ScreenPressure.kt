package com.example.mydoctor.ui.screens.pressure

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.mydoctor.R
import com.example.mydoctor.ui.navigation.Screen
import com.example.mydoctor.ui.theme.BackgroundGradientEnd
import com.example.mydoctor.ui.theme.BackgroundGradientMiddle
import com.example.mydoctor.ui.theme.BackgroundGradientStart
import com.example.mydoctor.ui.theme.BlackColor
import com.example.mydoctor.ui.theme.TooltipBackgroundColor
import com.example.mydoctor.ui.theme.WhiteColor
import com.example.mydoctor.ui.theme.components.CardNotes
import com.example.mydoctor.ui.theme.components.CardWithPeriod
import com.example.mydoctor.ui.theme.components.CardWithPressureGraph
import com.example.mydoctor.ui.theme.components.CustomDialogPressure
import com.example.mydoctor.utils.Constants.EMPTY_STRING
import com.github.mikephil.charting.utils.Utils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenPressure(navController: NavController? = null) {
    val tooltipState = rememberTooltipState(isPersistent = false)
    val viewModel: PressureVm = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    Utils.init(context)
    val showDialog = remember {
        mutableStateOf(false)
    }
    if (showDialog.value) {
        CustomDialogPressure(
            onDismissRequest = { showDialog.value = false },
            systolic = uiState.dialogData.systolic,
            diastolic = uiState.dialogData.diastolic,
            pulse = uiState.dialogData.pulse,
            data = uiState.dialogData.data,
            note = uiState.dialogData.note,
        )
    }
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            BackgroundGradientStart,
                            BackgroundGradientMiddle,
                            BackgroundGradientEnd
                        ),
                        center = Offset(x = 1200f, y = 500f),
                        radius = 400f
                    )
                )
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header with doctor icon and title
            item {
                Spacer(modifier = Modifier.height(40.dp))
                Row {
                    Icon(
                        tint = BlackColor,
                        painter = painterResource(id = R.drawable.icon_doctor),
                        contentDescription = EMPTY_STRING,
                        modifier = Modifier.padding(6.dp)
                    )
                    Text(
                        color = BlackColor,
                        text = stringResource(R.string.screenPressure_title),
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
            }
            // Display current pressure data and add pressure button
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(stringResource(R.string.screenPressure_pressure), color = BlackColor)
                        Text(uiState.data, color = BlackColor)
                    }
                    Card(
                        onClick = { navController?.navigate(Screen.ScreenAddPressure.screenName) },
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = WhiteColor),
                        modifier = Modifier.size(32.dp)
                    ) {
                        Box(Modifier.size(32.dp)) {
                            Icon(
                                tint = BlackColor,
                                painter = painterResource(id = R.drawable.icon_add),
                                contentDescription = EMPTY_STRING,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
                }
            }
            // Card for selecting period (day/week/month)
            item {
                Spacer(modifier = Modifier.height(32.dp))
                CardWithPeriod { period ->
                    viewModel.onEvent(PressureView.Event.OnClickCard(period))
                }
            }
            /**
             * <h1>Display Pressure Graph</h1>
             * This function creates a UI element to display a pressure graph
             * using the CardWithPressureGraph component, setting up parameters
             * like average pressure and pulse. It also handles user interactions
             * for dismissing dialogs and showing details on pressure points.
             *
             * @author  Mike
             * @version 1.0
             * @since   2024-11-01
             */
            // Display the pressure graph with interaction handling
            item {
                Spacer(modifier = Modifier.height(16.dp))
                CardWithPressureGraph(modifier = Modifier.fillMaxWidth(),
                    mediumPulse = uiState.mediumPulse,
                    mediumPressure = uiState.mediumPressure,
                    mediumPressureDate = uiState.dataPressure,
                    tooltipState = tooltipState,
                    isShowFullText = uiState.showFullText,
                    context = context,
                    pressurePoint =
                    viewModel.pressurePoints.collectAsState().value,
                    onDismissPoint = { viewModel.closeDialog() },
                    onClickPoint = { systolic, diastolic, pulse, data, note ->
                        showDialog.value = true
                        viewModel.onEvent(
                            PressureView.Event.ShowDialogForCharts(
                                systolic,
                                diastolic,
                                pulse ?: context.getString(R.string.screenPressure_pulse_empty),
                                data ?: EMPTY_STRING,
                                note ?: EMPTY_STRING
                            )
                        )
                    }
                )
            }
            // Display notes related to pressure readings
            item {
                Spacer(modifier = Modifier.height(16.dp))
                CardNotes(cardState = uiState.cardState, note = uiState.note, modifier = Modifier)
                Spacer(modifier = Modifier.height(64.dp))
            }
        }
        // Display tooltip background if persistent state is active
        if (tooltipState.isPersistent) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(TooltipBackgroundColor)
            )
        }
    }
}
