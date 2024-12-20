package com.example.mydoctor.ui.theme.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mydoctor.ui.theme.BlackColor
import com.example.mydoctor.ui.theme.StateDescriptionColor
import com.example.mydoctor.ui.theme.Transparent
import com.example.mydoctor.ui.theme.White
import com.example.mydoctor.utils.Constants

/**
 * Компонент для создания базового текстового поля ввода.
 *
 * @param onValueChange Функция обратного вызова, вызываемая при изменении текста.
 * @param modifier Модификатор для настройки внешнего вида текстового поля.
 * @param placeholderText Текст-заполнитель, отображаемый, когда поле ввода пустое.
 * @param textStyle Стиль текста для отображения в поле ввода.
 * @param isNumericInput Указывает, является ли ввод числовым (по умолчанию true).
 * @param onNextFocus Функция обратного вызова для обработки перехода к следующему полю фокуса.
 */
@Composable
fun BaseEditText(
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholderText: String = Constants.EMPTY_STRING,
    textStyle: TextStyle = TextStyle(),
    isNumericInput: Boolean = true,
    onNextFocus: (() -> Unit)? = null
) {
    var text by remember { mutableStateOf(Constants.EMPTY_STRING) }
    val focusRequester = remember { FocusRequester() }
    var isFocused by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .border(
                width = if (isFocused) 1.dp else 0.dp,
                color = if (isFocused) BlackColor else Transparent,
                shape = RoundedCornerShape(14.dp)
            )
            .background(White, shape = RoundedCornerShape(14.dp))
            .focusRequester(focusRequester)
            .clickable { focusRequester.requestFocus() }
            .wrapContentSize()
            .onFocusChanged { focusState ->
                isFocused = focusState.isFocused
            }
    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.weight(1f)) {
                BasicTextField(
                    maxLines = 1,
                    value = text,
                    onValueChange = {
                        if (isNumericInput && it.all { char -> char.isDigit() } || !isNumericInput) {
                            text = it
                            onValueChange(it)
                        }
                    },
                    textStyle = textStyle.copy(fontSize = 18.sp, color = BlackColor),
                    modifier = Modifier
                        .padding(0.dp)
                        .onKeyEvent { keyEvent ->
                            if (keyEvent.type == KeyEventType.KeyUp && keyEvent.key == Key.Enter) {
                                onNextFocus?.invoke()
                                true
                            } else {
                                false
                            }
                        },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = if (isNumericInput)
                            KeyboardType.Number
                        else
                            KeyboardType.Text
                    )
                )
                if (text.isEmpty()) {
                    Text(
                        placeholderText,
                        style = textStyle.copy(color = StateDescriptionColor, fontSize = 18.sp),
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .clickable {
                                focusRequester.requestFocus()
                                text = Constants.EMPTY_STRING
                            }
                    )
                }
            }
        }
    }
}

