package com.example.mydoctor.ui.theme.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mydoctor.R
import com.example.mydoctor.ui.theme.ActiveButtonColor
import com.example.mydoctor.ui.theme.ButtonTextColor
import com.example.mydoctor.ui.theme.InactiveButtonColor
import com.example.mydoctor.ui.theme.PlaceholderTextColor
import com.example.mydoctor.ui.theme.PressureButtonColor

/**
 * Базовый компонент кнопки.
 *
 * @param onClick Функция обратного вызова, вызываемая при нажатии кнопки.
 * @param isActive Указывает, активна ли кнопка.
 * @param text Текст, отображаемый на кнопке.
 * @param modifier Модификатор для настройки внешнего вида кнопки.
 */
@Composable
fun BaseButton(
    onClick: () -> Unit = {},
    isActive: Boolean = false,
    text: String,
    modifier: Modifier
) {
    Button(
        modifier = modifier,
        enabled = isActive,
        onClick = { onClick() },
        colors = ButtonDefaults.buttonColors(
            containerColor = ActiveButtonColor,
            contentColor = ButtonTextColor,
            disabledContainerColor = InactiveButtonColor,
            disabledContentColor = ButtonTextColor
        )
    ) {
        Text(text = text)
    }
}

/**
 * Кнопка для экрана давления.
 *
 * @param onClick Функция обратного вызова, вызываемая при нажатии кнопки.
 * @param modifier Модификатор для настройки внешнего вида кнопки.
 */
@Composable
fun ButtonScreenPressure(
    onClick: () -> Unit = {},
    modifier: Modifier
) {
    Button(
        modifier = modifier.height(25.dp),
        onClick = { onClick() },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = PressureButtonColor
        ),
        border = BorderStroke(1.dp, color = PressureButtonColor),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        Text(text = stringResource(R.string.base_button_data), fontSize = 12.sp)
    }
}

/**
 * Компонент для создания действия кнопки с текстом.
 *
 * @param onClick Функция обратного вызова, вызываемая при нажатии кнопки.
 * @param modifier Модификатор для настройки внешнего вида кнопки.
 * @param text Текст, отображаемый на кнопке.
 */
@Composable
fun ButtonAction(
    onClick: () -> Unit = {},
    modifier: Modifier,
    text: String = stringResource(R.string.base_button_text)
) {
    var isValueSet by remember { mutableStateOf(false) }
    val textColor = if (isValueSet && text.isNotEmpty()) Color.Black else PlaceholderTextColor
    Button(
        onClick = {
            onClick()
            isValueSet = true
        },
        colors = ButtonDefaults.buttonColors(Color.White),
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
    ) {
        Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart) {
            Text(
                text,
                color = textColor,
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal
            )
        }
    }
}