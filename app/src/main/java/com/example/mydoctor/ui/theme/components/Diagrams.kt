package com.example.mydoctor.ui.theme.components


import android.content.Context
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.mydoctor.R
import com.example.mydoctor.utils.Constants
import com.example.mydoctor.utils.DateUtils
import com.example.mydoctor.utils.DateUtils.MONTH
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class PressurePoint(
    val dateTime: LocalDateTime,
    val systolic: Float,
    val diastolic: Float,
    val pulse: Float,
    val note: String
)

@Composable
fun LineChartComponent(
    context: Context,
    modifier: Modifier = Modifier,
    pressurePoints: List<PressurePoint>,
    onClickPoint: (String, String, String?, String?, String?) -> Unit,
    onDismissPoint: () -> Unit
) {
    val systolicColorLine = ContextCompat.getColor(context, R.color.sistolic)
    val diastolicColorLine = ContextCompat.getColor(context, R.color.diestolic)
    val pointDiastolicColor = ContextCompat.getColor(context, R.color.point_diastolic)
    val pointSystolicColor = ContextCompat.getColor(context, R.color.point_sistolic)

    val systolicDataSet = pressurePoints.mapIndexed { index, point ->
        Entry(index.toFloat(), point.systolic)
    }.createDataSetWithColor(
        systolicColorLine, pointSystolicColor,
        stringResource(R.string.diagram_data_systolic)
    )

    val diastolicDataSet = pressurePoints.mapIndexed { index, point ->
        Entry(index.toFloat(), point.diastolic)
    }.createDataSetWithColor(
        diastolicColorLine, pointDiastolicColor,
        stringResource(R.string.diagram_data_distolic)
    )

    val lineDataCombined = remember(pressurePoints) {
        LineData(systolicDataSet, diastolicDataSet)
    }

    AndroidView(modifier = modifier.fillMaxWidth(), factory = { factory ->
        LineChart(factory).apply {
            data = lineDataCombined
            description.isEnabled = false
            axisLeft.isEnabled = false
            xAxis.setDrawGridLines(false)
            axisRight.enableGridDashedLine(10f, 5f, 0f)
            axisRight.granularity = 50f
            axisRight.axisMaximum = 200f
            xAxis.enableGridDashedLine(10f, 10f, 0f)
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            systolicDataSet.setDrawValues(false)
            diastolicDataSet.setDrawValues(false)

            setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                override fun onValueSelected(e: Entry?, h: Highlight?) {
                    val index = e?.x?.toInt()
                    if (index != null && index in pressurePoints.indices) {
                        val pressurePoint = pressurePoints[index]
                        onClickPoint.invoke(
                            pressurePoint.systolic.toString(),
                            pressurePoint.diastolic.toString(),
                            pressurePoint.pulse.toString(),
                            pressurePoint.dateTime.toString(),
                            pressurePoint.note
                        )
                    }
                }

                override fun onNothingSelected() {
                    onDismissPoint.invoke()
                }
            })
        }
    }, update = { chart ->
        chart.data = lineDataCombined
        if (pressurePoints.isNotEmpty()) {
            val systolicValues = pressurePoints.map { it.systolic }
            val diastolicValues = pressurePoints.map { it.diastolic }

            chart.axisLeft.axisMinimum =
                minOf(systolicValues.minOrNull() ?: 0f, diastolicValues.minOrNull() ?: 0f)
            chart.axisLeft.axisMaximum =
                maxOf(systolicValues.maxOrNull() ?: 0f, diastolicValues.maxOrNull() ?: 0f)
        }
        chart.xAxis.valueFormatter = DateValueFormatter(pressurePoints.map { it.dateTime })
        chart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry?, h: Highlight?) {
                val index = e?.x?.toInt()
                if (index != null && index in pressurePoints.indices) {
                    val pressurePoint = pressurePoints[index]
                    onClickPoint.invoke(
                        pressurePoint.systolic.toString(),
                        pressurePoint.diastolic.toString(),
                        pressurePoint.pulse.toString(),
                        pressurePoint.dateTime.toString(),
                        pressurePoint.note
                    )
                }
            }

            override fun onNothingSelected() {
                onDismissPoint.invoke()
            }
        })
        chart.invalidate()
    })
}

class DateValueFormatter(private val dates: List<LocalDateTime>) : ValueFormatter() {
    private val formatterDay = DateTimeFormatter.ofPattern(DateUtils.TIME)
    private val formatterWeekMonth = DateTimeFormatter.ofPattern(MONTH)

    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        return when {
            value.toInt() in dates.indices -> {
                if (dates.size <= 7) {
                    dates[value.toInt()].format(formatterDay)
                } else {
                    dates[value.toInt()].format(formatterWeekMonth)
                }
            }

            else -> Constants.EMPTY_STRING
        }
    }
}

fun List<Entry>.createDataSetWithColor(
    lineColor: Int,
    pointColor: Int,
    label: String
): LineDataSet {
    return LineDataSet(this, label).apply {
        color = lineColor
        lineWidth = 4f
        circleRadius = 3f
        setDrawFilled(false)
        setDrawCircles(true)
        setCircleColors(pointColor)
        circleHoleColor = pointColor
        setDrawCircleHole(true)
        setDrawValues(false)
        mode = LineDataSet.Mode.CUBIC_BEZIER
    }
}