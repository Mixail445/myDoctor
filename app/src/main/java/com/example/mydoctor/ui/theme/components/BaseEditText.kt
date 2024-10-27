package com.example.mydoctor.ui.theme.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mydoctor.ui.theme.BlackColor
import com.example.mydoctor.ui.theme.StateDescriptionColor
import com.example.mydoctor.ui.theme.TitleTextColor
import com.example.mydoctor.ui.theme.Transparent
import com.example.mydoctor.ui.theme.White
import com.example.mydoctor.utils.Constants

@Composable
fun BaseEditText(
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    placeholderText: String = Constants.EMPTY_STRING,
    textStyle: TextStyle = TextStyle(),
) {
    var text by remember { mutableStateOf("") }
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
            .padding(horizontal = 16.dp)
            .height(56.dp)
            .onFocusChanged { focusState ->
                isFocused = focusState.isFocused
            }
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            leadingIcon?.invoke()
            Box(modifier = Modifier.weight(1f)) {
                BasicTextField(
                    maxLines = 1,
                    value = text,
                    onValueChange = {
                        text = it
                        onValueChange(it)
                    },
                    textStyle = textStyle.copy(fontSize = 18.sp, color = BlackColor),
                    modifier = Modifier.padding(0.dp)
                )
                if (text.isEmpty()) {
                    Text(
                        placeholderText,
                        style = textStyle.copy(color = StateDescriptionColor, fontSize = 18.sp),
                        modifier = Modifier.align(Alignment.CenterStart)
                    )
                }
            }
            trailingIcon?.invoke()
        }
    }
}
