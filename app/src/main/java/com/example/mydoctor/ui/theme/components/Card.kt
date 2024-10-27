package com.example.mydoctor.ui.theme.components

import android.annotation.SuppressLint
import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.RichTooltip
import androidx.compose.material3.RichTooltipColors
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupPositionProvider
import com.example.mydoctor.R
import com.example.mydoctor.ui.screens.pressure.Period
import com.example.mydoctor.ui.theme.ActiveButtonColorCard
import com.example.mydoctor.ui.theme.CancelButtonColor
import com.example.mydoctor.ui.theme.DarkGray
import com.example.mydoctor.ui.theme.DiastolicColor
import com.example.mydoctor.ui.theme.LightGray
import com.example.mydoctor.ui.theme.StateDescriptionColor
import com.example.mydoctor.ui.theme.SystolicColor
import com.example.mydoctor.ui.theme.Transparent
import com.example.mydoctor.utils.Constants
import com.example.mydoctor.utils.Constants.DAY_BUTTON_TEXT
import com.example.mydoctor.utils.Constants.DIASTOLIC_TEXT
import com.example.mydoctor.utils.Constants.EMPTY_STRING
import com.example.mydoctor.utils.Constants.MONTH_BUTTON_TEXT
import com.example.mydoctor.utils.Constants.NOTES_TITLE
import com.example.mydoctor.utils.Constants.NO_DATA_TEXT
import com.example.mydoctor.utils.Constants.PRESSURE_RATE
import com.example.mydoctor.utils.Constants.PRESSURE_TITLE_TEXT
import com.example.mydoctor.utils.Constants.PULSE
import com.example.mydoctor.utils.Constants.PULSE_RATE
import com.example.mydoctor.utils.Constants.STATE_DESCRIPTION
import com.example.mydoctor.utils.Constants.SYSTOLIC_TEXT
import com.example.mydoctor.utils.Constants.TODAY_TEXT
import com.example.mydoctor.utils.Constants.WEEK_BUTTON_TEXT
import com.patrykandpatrick.vico.core.common.data.MutableExtraStore
import kotlinx.coroutines.launch

@Composable
fun CardWithPeriod(onClick: (Period) -> Unit = {}) {
    var activeButton by remember { mutableStateOf(Period.DAY) }

    Card(
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .height(42.dp)
            .fillMaxWidth(),
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Button(
                contentPadding = PaddingValues(),
                modifier = Modifier.width(99.dp),
                onClick = {
                    activeButton = Period.DAY
                    onClick(Period.DAY)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (activeButton == Period.DAY) ActiveButtonColorCard else Transparent
                ),
            ) {
                Text(
                    text = DAY_BUTTON_TEXT,
                    color = Color.Black,
                    fontWeight = if (activeButton == Period.DAY) FontWeight.Bold else FontWeight.Normal
                )
            }

            Button(
                contentPadding = PaddingValues(),
                modifier = Modifier.width(99.dp),
                onClick = {
                    activeButton = Period.WEEK
                    onClick(Period.WEEK)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (activeButton == Period.WEEK) ActiveButtonColorCard else Transparent
                ),
            ) {
                Text(
                    text = WEEK_BUTTON_TEXT,
                    color = Color.Black,
                    fontWeight = if (activeButton == Period.WEEK) FontWeight.Bold else FontWeight.Normal
                )
            }

            Button(
                contentPadding = PaddingValues(),
                modifier = Modifier.width(99.dp),
                onClick = {
                    activeButton = Period.MONTH
                    onClick(Period.MONTH)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (activeButton == Period.MONTH) ActiveButtonColorCard else Transparent
                ),
            ) {
                Text(
                    text = MONTH_BUTTON_TEXT,
                    color = Color.Black,
                    fontWeight = if (activeButton == Period.MONTH) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RichTooltipWithCustomCaretSample(modifier: Modifier = Modifier, state: TooltipState) {
    val scope = rememberCoroutineScope()

    TooltipBox(
        positionProvider = customTooltipPositionProvider(), tooltip = {
            RichTooltip(modifier = modifier, colors = RichTooltipColors(
                containerColor = Color.White,
                contentColor = Color.Black,
                titleContentColor = Color.Black,
                actionContentColor = Color.Black
            ), title = {
                Box(Modifier.fillMaxWidth()) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_camera),
                        modifier = Modifier.align(Alignment.Center),
                        contentDescription = EMPTY_STRING,
                        tint = null,
                    )
                }
            }, action = {
                Text(
                    style = TextStyle.Default.copy(fontWeight = FontWeight(400)),
                    text = Constants.TOOLTIP_ACTION_TEXT,
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    modifier = Modifier.fillMaxWidth(0.9f)
                )
            }, caretSize = DpSize(32.dp, 16.dp), shadowElevation = 0.dp, tonalElevation = 10.dp
            ) {
                Box(Modifier.fillMaxWidth()) {
                    TitleText(
                        text = Constants.TOOLTIP_TITLE, modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }, state = state
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 18.dp),
            horizontalArrangement = Arrangement.End
        ) {
            ButtonScreenPressure(modifier = modifier,
                onClick = { scope.launch { state.show(MutatePriority.Default) } })
        }
    }
}

@Composable
fun customTooltipPositionProvider(): PopupPositionProvider {
    return object : PopupPositionProvider {
        override fun calculatePosition(
            anchorBounds: IntRect,
            windowSize: IntSize,
            layoutDirection: LayoutDirection,
            popupContentSize: IntSize
        ): IntOffset {
            return IntOffset(
                x = anchorBounds.right - (popupContentSize.width / 2),
                y = anchorBounds.top - popupContentSize.height - 100
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardWithPressureGraph(
    period: Period,
    tooltipState: TooltipState,
    systolicData: List<Pair<String, Float>>,
    diastolicData: List<Pair<String, Float>>,
    modifier: Modifier = Modifier,
    mediumPressure: String = NO_DATA_TEXT,
    mediumPulse: String = EMPTY_STRING,
    mediumPressureDate: String = TODAY_TEXT,
    isShowFullText: Boolean = false
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding()) {
            Spacer(modifier = Modifier.height(24.dp))
            Row(Modifier.padding(start = 16.dp)) {
                if (isShowFullText) {
                    BodyXsText(
                        text = PRESSURE_TITLE_TEXT,
                        color = CancelButtonColor.copy(alpha = 0.5f)
                    )
                    HeadingText(
                        text = mediumPressure,
                        modifier = Modifier.padding(start = 8.dp, end = 4.dp, top = 2.dp),
                    )
                    BodyXsText(text = PRESSURE_RATE, color = CancelButtonColor.copy(alpha = 0.5f))
                } else {
                    BodyLText(text = NO_DATA_TEXT, modifier = Modifier.padding(start = 16.dp))
                }
            }
            Row(Modifier.padding(start = 16.dp)) {
                if (isShowFullText) {
                    BodyXsText(text = PULSE, color = CancelButtonColor)
                    HeadingText(
                        text = mediumPulse,
                        modifier = Modifier.padding(start = 28.dp, end = 4.dp, top = 2.dp),
                    )
                    BodyXsText(text = PULSE_RATE, color = CancelButtonColor)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            BodyXsText(
                text = mediumPressureDate,
                color = LightGray,
                modifier = Modifier.padding(start = 16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(
                modifier
                    .padding(horizontal = 16.dp)
                    .height(1.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                DrawCircle(SystolicColor, 4.dp, padding = PaddingValues(end = 16.dp))
                BodyXsText(text = SYSTOLIC_TEXT)

                DrawCircle(
                    DiastolicColor, 4.dp, padding = PaddingValues(start = 16.dp, end = 16.dp)
                )
                BodyXsText(text = DIASTOLIC_TEXT)
            }

            Spacer(modifier = Modifier.height(16.dp))

            DiagramPressure(
                modifier = Modifier
                    .height(283.dp)
                    .padding(horizontal = 16.dp),
                systolicData,
                diastolicData,
                period = period,
                extraStore = remember { MutableExtraStore() }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Box(modifier = Modifier.fillMaxWidth()) {
                RichTooltipWithCustomCaretSample(
                    modifier = Modifier.align(Alignment.BottomCenter), state = tooltipState
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardNotes(
    note: String = EMPTY_STRING,
    cardState: CardState = CardState.DoubleText,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier.background(Color.White)
) {
    when (cardState) {
        is CardState.SingleText -> {
            Card {
                Column(modifier.padding(16.dp)) {
                    Row {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_edit),
                            contentDescription = null,
                            tint = null
                        )
                        BodyMText(
                            text = NOTES_TITLE,
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 16.dp)
                        )
                        Icon(
                            painter = painterResource(id = R.drawable.icon_next),
                            contentDescription = null,
                            tint = DarkGray
                        )
                    }
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(4.dp))
                    Row {
                        BodyXsText(
                            text = cardState.data, color = DarkGray, Modifier.padding(end = 8.dp)
                        )
                        BodyXsText(text = cardState.time, color = DarkGray)
                    }
                    BodySText(text = note)
                }
            }
        }

        CardState.DoubleText -> {
            Card {
                Column(modifier.padding(16.dp)) {
                    Row {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_edit),
                            contentDescription = null,
                            tint = null
                        )
                        BodyMText(
                            text = NOTES_TITLE,
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 16.dp)
                        )
                        Icon(
                            painter = painterResource(id = R.drawable.icon_add),
                            contentDescription = null,
                            tint = DarkGray
                        )
                    }
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(8.dp))
                    BodySText(text = STATE_DESCRIPTION, color = StateDescriptionColor)
                }
            }
        }

        is CardState.Empty -> {
            Card {
                Row(
                    Modifier.padding(vertical = 16.dp, horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_edit),
                        contentDescription = null,
                        tint = null
                    )
                    Text(
                        text = NOTES_TITLE, modifier = Modifier
                            .weight(1f)
                            .padding(start = 16.dp)
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.icon_next),
                        contentDescription = null
                    )
                }
            }
        }
    }
}

sealed interface CardState {
    data class SingleText(val data: String, val time: String, val note: String) : CardState
    data object DoubleText : CardState
    data object Empty : CardState
}