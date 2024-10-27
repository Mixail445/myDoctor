package com.example.mydoctor.ui.theme.components

import android.annotation.SuppressLint
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mydoctor.ui.theme.ActiveButtonColor
import com.example.mydoctor.ui.theme.ButtonTextColor
import com.example.mydoctor.ui.theme.InactiveButtonColor
import com.example.mydoctor.ui.theme.PlaceholderTextColor
import com.example.mydoctor.ui.theme.PressureButtonColor
import com.example.mydoctor.utils.Constants.ADD_DATA_BUTTON_TEXT
import com.example.mydoctor.utils.Constants.BUTTON_TEXT_DATE


@Composable
fun BaseButton(
    onClick: () -> Unit = {},
    isActive: Boolean = false,
    text: String,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
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

@Preview
@Composable
fun ButtonScreenPressure(onClick: () -> Unit = {}, @SuppressLint("ModifierParameter") modifier: Modifier = Modifier) {
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
        Text(text = ADD_DATA_BUTTON_TEXT, fontSize = 12.sp)
    }
}

@Preview
@Composable
fun ButtonAction(
    onClick: () -> Unit = {},
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
    text: String = BUTTON_TEXT_DATE
) {
    Button(
        onClick = { onClick() },
        colors = ButtonDefaults.buttonColors(Color.White),
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
    ) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart,) {
            Text(text, color = PlaceholderTextColor, fontSize = 18.sp, fontWeight = FontWeight.Normal)
        }
    }
}