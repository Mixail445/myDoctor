package com.example.mydoctor.ui.theme.components


import android.content.Context
import androidx.compose.foundation.MutatePriority
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
import androidx.compose.ui.res.stringResource
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
import com.example.mydoctor.utils.Constants.EMPTY_STRING
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
                    text = stringResource(R.string.card_text_day),
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
                    text = stringResource(R.string.card_week),
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
                    text = stringResource(R.string.card_text_month),
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
                    text = stringResource(R.string.card_text_long_text),
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    modifier = Modifier.fillMaxWidth(0.9f)
                )
            }, caretSize = DpSize(32.dp, 16.dp), shadowElevation = 0.dp, tonalElevation = 10.dp
            ) {
                Box(Modifier.fillMaxWidth()) {
                    TitleText(
                        text = stringResource(R.string.card_text_add_data),
                        modifier = Modifier.align(Alignment.Center)
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
    tooltipState: TooltipState,
    modifier: Modifier = Modifier,
    mediumPressure: String = stringResource(R.string.card_text_no_data),
    mediumPulse: String = EMPTY_STRING,
    mediumPressureDate: String = stringResource(R.string.card_text_now),
    isShowFullText: Boolean = false,
    pressurePoint: List<PressurePoint>,
    onClickPoint: (String, String, String?, String?, String?) -> Unit,
    onDismissPoint: () -> Unit,
    context: Context
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
                        text = stringResource(R.string.card_text_pressure),
                        color = CancelButtonColor.copy(alpha = 0.5f),
                        modifier = Modifier
                    )
                    HeadingText(
                        text = mediumPressure,
                        modifier = Modifier.padding(start = 8.dp, end = 4.dp, top = 2.dp),
                    )
                    BodyXsText(
                        text = "мм рт.ст", color = CancelButtonColor.copy(alpha = 0.5f), Modifier
                    )
                } else {
                    BodyLText(
                        text = stringResource(R.string.card_text_not_data),
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
            Row(Modifier.padding(start = 16.dp)) {
                if (isShowFullText) {
                    BodyXsText(
                        text = stringResource(R.string.screenAddPressure_text_pulse),
                        color = CancelButtonColor,
                        Modifier
                    )
                    HeadingText(
                        text = mediumPulse,
                        modifier = Modifier.padding(start = 28.dp, end = 4.dp, top = 2.dp),
                    )
                    BodyXsText(
                        text = stringResource(R.string.card_text_parametr),
                        color = CancelButtonColor,
                        Modifier
                    )
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
                BodyXsText(text = stringResource(R.string.card_systolic), modifier = Modifier)

                DrawCircle(
                    DiastolicColor, 4.dp, padding = PaddingValues(start = 16.dp, end = 16.dp)
                )
                BodyXsText(text = stringResource(R.string.card_distolic), modifier = Modifier)
            }

            Spacer(modifier = Modifier.height(16.dp))

            LineChartComponent(
                context = context,
                modifier = Modifier
                    .height(283.dp)
                    .padding(horizontal = 16.dp),
                pressurePoints = pressurePoint,
                onClickPoint = onClickPoint,
                onDismissPoint = onDismissPoint
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
    note: String = EMPTY_STRING, cardState: CardState = CardState.DoubleText, modifier: Modifier
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
                            color = Color.Black,
                            text = stringResource(R.string.card_text_note),
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
                        BodyXsText(text = cardState.time, color = DarkGray, modifier = Modifier)
                    }
                    BodySText(text = note, modifier = Modifier)
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
                            text = stringResource(R.string.card_text_note),
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
                    BodySText(
                        text = stringResource(R.string.card_text_text_medium),
                        color = StateDescriptionColor,
                        modifier = Modifier
                    )
                }
            }
        }

        is CardState.Empty -> {
            Card(colors = CardDefaults.cardColors(containerColor = Color.White)) {
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
                        text = stringResource(R.string.card_text_note),
                        modifier = Modifier
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