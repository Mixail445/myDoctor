package com.example.mydoctor.ui.theme.components

import android.annotation.SuppressLint
import android.text.Layout
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils
import com.example.mydoctor.R
import com.example.mydoctor.ui.screens.pressure.Period
import com.example.mydoctor.ui.theme.BackgroundColor
import com.example.mydoctor.utils.DateUtils.dateTimeFormatter
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberAxisGuidelineComponent
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberAxisLineComponent
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberEnd
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLine
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoZoomState
import com.patrykandpatrick.vico.compose.common.component.fixed
import com.patrykandpatrick.vico.compose.common.component.rememberLayeredComponent
import com.patrykandpatrick.vico.compose.common.component.rememberShapeComponent
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import com.patrykandpatrick.vico.compose.common.component.shadow
import com.patrykandpatrick.vico.compose.common.dimensions
import com.patrykandpatrick.vico.compose.common.fill
import com.patrykandpatrick.vico.compose.common.shape.dashedShape
import com.patrykandpatrick.vico.compose.common.shape.markerCorneredShape
import com.patrykandpatrick.vico.core.cartesian.CartesianMeasuringContext
import com.patrykandpatrick.vico.core.cartesian.HorizontalDimensions
import com.patrykandpatrick.vico.core.cartesian.Zoom
import com.patrykandpatrick.vico.core.cartesian.axis.BaseAxis
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModel
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianLayerRangeProvider
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.patrykandpatrick.vico.core.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.core.cartesian.marker.CartesianMarker
import com.patrykandpatrick.vico.core.cartesian.marker.DefaultCartesianMarker
import com.patrykandpatrick.vico.core.common.DrawingContext
import com.patrykandpatrick.vico.core.common.Insets
import com.patrykandpatrick.vico.core.common.LayeredComponent
import com.patrykandpatrick.vico.core.common.component.Component
import com.patrykandpatrick.vico.core.common.component.Shadow
import com.patrykandpatrick.vico.core.common.component.ShapeComponent
import com.patrykandpatrick.vico.core.common.component.TextComponent
import com.patrykandpatrick.vico.core.common.data.ExtraStore
import com.patrykandpatrick.vico.core.common.data.MutableExtraStore
import com.patrykandpatrick.vico.core.common.shape.Corner
import com.patrykandpatrick.vico.core.common.shape.CorneredShape
import com.patrykandpatrick.vico.core.common.shape.Shape
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import kotlin.math.ceil

@Composable
fun DiagramPressure(
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
    systolicData: List<Pair<String, Float>> = emptyList(),
    diastolicData: List<Pair<String, Float>> = emptyList(),
    period: Period,
    extraStore: MutableExtraStore
) {

    val systolic = systolicData.associate { (dateString, yValue) ->
        LocalDateTime.parse(dateString, dateTimeFormatter) to yValue
    }

    val diastolic = diastolicData.associate { (dateString, yValue) ->
        LocalDateTime.parse(dateString, dateTimeFormatter) to yValue
    }

    val modelProducer = remember { CartesianChartModelProducer() }

    LaunchedEffect(period) {
        extraStore[periodKey] = period
    }

    LaunchedEffect(systolic, diastolic) {
        withContext(Dispatchers.Default) {
            modelProducer.runTransaction {
                lineSeries {
                    if (systolicData.isEmpty() || diastolicData.isEmpty()) {
                        series(y = listOf(1), x = listOf(1))
                        series(y = listOf(1), x = listOf(1))
                    } else {
                        when (period) {
                            Period.DAY -> {
                                series(y = systolic.values, x = systolic.keys.map { it.hour })
                                series(y = diastolic.values, x = diastolic.keys.map { it.hour })
                            }

                            Period.WEEK -> {
                                series(
                                    y = systolic.values,
                                    x = systolic.keys.map { it.dayOfWeek.value })
                                series(
                                    y = diastolic.values,
                                    x = diastolic.keys.map { it.dayOfWeek.value })
                            }

                            Period.MONTH -> {
                                series(y = systolic.values, x = systolic.keys.map { it.dayOfMonth })
                                series(
                                    y = diastolic.values,
                                    x = diastolic.keys.map { it.dayOfMonth })
                            }
                        }
                    }
                }
            }
        }
    }

    ComposeChart1(modelProducer = modelProducer, modifier = modifier)
}

val periodKey = ExtraStore.Key<Period>()
private val rangeProvider =
    object : CartesianLayerRangeProvider {
        override fun getMaxY(minY: Double, maxY: Double, extraStore: ExtraStore): Double =
            ceil(200f).toDouble()

        override fun getMaxX(minX: Double, maxX: Double, extraStore: ExtraStore): Double {

            val currentPeriod = extraStore.getOrNull(periodKey) ?: Period.DAY

            return when (currentPeriod) {
                Period.DAY -> 24.0
                Period.WEEK -> 7.0
                Period.MONTH -> 31.0
            }
        }
    }

@SuppressLint("RememberReturnType", "RestrictedApi")
@Composable
private fun ComposeChart1(
    modelProducer: CartesianChartModelProducer,
    modifier: Modifier,
) {

    val guideline50 = rememberAxisGuidelineComponent(
        color = Color.LightGray,
        thickness = 1.dp,
        shape = dashedShape(Shape.Rectangle, 5.dp, 5.dp)
    )
    val guideline0 = rememberAxisLineComponent(
        color = Color.Black,
    )
    CartesianChartHost(
        chart = rememberCartesianChart(
            rememberLineCartesianLayer(
                LineCartesianLayer.LineProvider.series(
                    LineCartesianLayer.rememberLine(
                        remember { LineCartesianLayer.LineFill.single(fill(Color(0x33725E33))) },
                        pointProvider = pointProvider(colorBall = Color(0xffFF725E)),
                        areaFill = null,
                    ),
                    LineCartesianLayer.rememberLine(
                        remember { LineCartesianLayer.LineFill.single(fill(Color(0x33B34233))) },
                        pointProvider = pointProvider(colorBall = Color(0xffFFB342)),
                        areaFill = null,
                    )
                ),
                rangeProvider = rangeProvider
            ),
            startAxis = VerticalAxis.start(guideline50),
            marker = rememberMarker(),
            endAxis = VerticalAxis.rememberEnd(
                line = guideline0,
                label = TextComponent(color = R.color.light_gray),
                itemPlacer = VerticalAxis.ItemPlacer.step(step = { 50.0 }),
                tick = null,
                size = BaseAxis.Size.Auto(),
                guideline = guideline50
            ),
            bottomAxis = HorizontalAxis.rememberBottom(
                line = guideline0,
                guideline = null,
                label = TextComponent(color = R.color.light_gray),
                tick = null,
                itemPlacer = remember { HorizontalAxis.ItemPlacer.segmented() },
            ),
            topAxis = HorizontalAxis.top(guideline50),
        ),
        modelProducer = modelProducer,
        zoomState = rememberVicoZoomState(initialZoom = Zoom.Content),
        modifier = modifier,
        scrollState = rememberVicoScrollState(scrollEnabled = false),
    )

}

fun pointProvider(colorBall: Color): LineCartesianLayer.PointProvider {
    return LineCartesianLayer.PointProvider.single(
        LineCartesianLayer.Point(
            component = object : Component {

                override fun draw(
                    context: DrawingContext,
                    left: Float,
                    top: Float,
                    right: Float,
                    bottom: Float
                ) {
                    val paint = android.graphics.Paint().apply {
                        color = colorBall.toArgb()
                        style = android.graphics.Paint.Style.FILL
                    }

                    val centerX = (left + right) / 2
                    val centerY = (top + bottom) / 2
                    context.canvas.drawCircle(
                        centerX,
                        centerY,
                        8f,
                        paint
                    )
                }
            },
            sizeDp = 16f
        )
    )
}


@Composable
internal fun rememberMarker(
    labelPosition: DefaultCartesianMarker.LabelPosition = DefaultCartesianMarker.LabelPosition.AbovePoint,
    showIndicator: Boolean = true,
): CartesianMarker {
    val labelBackgroundShape = markerCorneredShape(Corner.FullyRounded)
    val labelBackground =
        rememberShapeComponent(
            color = BackgroundColor,
            shape = labelBackgroundShape,
            shadow =
            shadow(
                radius = LABEL_BACKGROUND_SHADOW_RADIUS_DP.dp,
                dy = LABEL_BACKGROUND_SHADOW_DY_DP.dp
            ),
        )
    val label =
        rememberTextComponent(
            color = Color.Black,
            textAlignment = Layout.Alignment.ALIGN_CENTER,
            padding = dimensions(8.dp, 4.dp),
            background = labelBackground,
            minWidth = TextComponent.MinWidth.fixed(4.dp),
        )
    val indicatorFrontComponent =
        rememberShapeComponent(MaterialTheme.colorScheme.surface, CorneredShape.Pill)
    val indicatorCenterComponent = rememberShapeComponent(shape = CorneredShape.Pill)
    val indicatorRearComponent = rememberShapeComponent(shape = CorneredShape.Pill)
    val indicator =
        rememberLayeredComponent(
            rear = indicatorRearComponent,
            front =
            rememberLayeredComponent(
                rear = indicatorCenterComponent,
                front = indicatorFrontComponent,
                padding = dimensions(5.dp),
            ),
            padding = dimensions(10.dp),
        )
    val guideline = rememberAxisGuidelineComponent()
    return remember(label, labelPosition, indicator, showIndicator, guideline) {
        object :
            DefaultCartesianMarker(
                label = label,
                labelPosition = labelPosition,
                indicator =
                if (showIndicator) {
                    { color ->
                        LayeredComponent(
                            rear = ShapeComponent(
                                ColorUtils.setAlphaComponent(color, 38),
                                CorneredShape.Pill
                            ),
                            front =
                            LayeredComponent(
                                rear =
                                ShapeComponent(
                                    color = color,
                                    shape = CorneredShape.Pill,
                                    shadow = Shadow(radiusDp = 0f, color = color),
                                ),
                                front = indicatorFrontComponent,
                                padding = dimensions(5.dp),
                            ),
                            padding = dimensions(10.dp),
                        )
                    }
                } else {
                    null
                },
                indicatorSizeDp = 36f,
            ) {
            override fun updateInsets(
                context: CartesianMeasuringContext,
                horizontalDimensions: HorizontalDimensions,
                model: CartesianChartModel,
                insets: Insets,
            ) {
                with(context) {
                    val baseShadowInsetDp =
                        CLIPPING_FREE_SHADOW_RADIUS_MULTIPLIER * LABEL_BACKGROUND_SHADOW_RADIUS_DP
                    var topInset = (baseShadowInsetDp - LABEL_BACKGROUND_SHADOW_DY_DP).pixels
                    var bottomInset = (baseShadowInsetDp + LABEL_BACKGROUND_SHADOW_DY_DP).pixels
                    when (labelPosition) {
                        LabelPosition.Top,
                        LabelPosition.AbovePoint -> topInset += label.getHeight(context) + tickSizeDp.pixels

                        LabelPosition.Bottom -> bottomInset += label.getHeight(context) + tickSizeDp.pixels
                        LabelPosition.AroundPoint -> {}
                    }
                    insets.ensureValuesAtLeast(top = topInset, bottom = bottomInset)
                }
            }
        }
    }
}

private const val LABEL_BACKGROUND_SHADOW_RADIUS_DP = 2f
private const val LABEL_BACKGROUND_SHADOW_DY_DP = 2f
private const val CLIPPING_FREE_SHADOW_RADIUS_MULTIPLIER = 2f