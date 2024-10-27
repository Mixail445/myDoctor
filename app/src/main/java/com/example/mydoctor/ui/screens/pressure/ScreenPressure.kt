package com.example.mydoctor.ui.screens.pressure

import android.annotation.SuppressLint
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
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
import com.example.mydoctor.utils.Constants
import com.example.mydoctor.utils.Constants.EMPTY_STRING
import com.example.mydoctor.utils.Constants.PRESSURE_DATE_TEXT
import com.example.mydoctor.utils.Constants.PRESSURE_TITLE_TEXT

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenPressure(navController: NavController? = null) {
    val tooltipState = rememberTooltipState(isPersistent = false)
    val viewModel: PressureVm = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()

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
                        text = Constants.MY_DOCTOR_TEXT,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
            }
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
                        Text(PRESSURE_TITLE_TEXT, color = BlackColor)
                        Text(PRESSURE_DATE_TEXT, color = BlackColor)
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
            item {
                Spacer(modifier = Modifier.height(32.dp))
                CardWithPeriod { period ->
                    viewModel.onEvent(PressureView.Event.OnClickCard(period))
                }
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
                CardWithPressureGraph(
                    mediumPulse = uiState.mediumPulse,
                    mediumPressure = uiState.mediumPressure,
                    mediumPressureDate = uiState.dataPressure,
                    period = viewModel.currentPeriod.value,
                    tooltipState = tooltipState,
                    systolicData = uiState.systolicData,
                    diastolicData = uiState.diastolicData,
                    isShowFullText = uiState.showFullText
                )
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
                CardNotes(cardState = uiState.cardState, note = uiState.note)
                Spacer(modifier = Modifier.height(64.dp))
            }
        }

        if (tooltipState.isPersistent) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(TooltipBackgroundColor)
            )
        }
    }
}
