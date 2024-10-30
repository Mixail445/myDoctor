package com.example.mydoctor.ui.theme.components

import android.annotation.SuppressLint
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun CustomText(
    text: String,
    fontSize: Float,
    fontWeight: FontWeight = FontWeight.Normal,
    color: Color = Color.Black,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    Text(
        text = text,
        fontSize = fontSize.sp,
        fontWeight = fontWeight,
        color = color,
        modifier = modifier
    )
}


@Composable
fun TitleText(text: String, modifier: Modifier = Modifier) {
    CustomText(
        text = text,
        fontSize = 18f,
        fontWeight = FontWeight.SemiBold,
        modifier = modifier
    )
}

@Composable
fun HeadingText(text: String, @SuppressLint("ModifierParameter") modifier: Modifier = Modifier) {
    CustomText(
        text = text,
        fontSize = 18f,
        fontWeight = FontWeight.SemiBold,
        modifier = modifier
    )
}

@Composable
fun SubtitleText(text: String, @SuppressLint("ModifierParameter") modifier: Modifier = Modifier) {
    CustomText(
        text = text,
        fontSize = 16f,
        fontWeight = FontWeight.SemiBold,
        modifier = modifier
    )
}

@Composable
fun BodySText(text: String, color: Color = Color.Black, @SuppressLint("ModifierParameter") modifier: Modifier = Modifier) {
    CustomText(
        text = text,
        fontSize = 14f,
        color = color,
        modifier = modifier
    )
}

@Composable
fun BodyLText(text: String, color: Color = Color.Black, @SuppressLint("ModifierParameter") modifier: Modifier = Modifier) {
    CustomText(
        text = text,
        fontSize = 18f,
        fontWeight = FontWeight.Normal,
        color = color,
        modifier = modifier
    )
}

@Composable
fun BodyXsText(text: String, color: Color = Color.Black,@SuppressLint("ModifierParameter")  modifier: Modifier = Modifier) {
    CustomText(
        text = text,
        fontSize = 12f,
        color = color,
        modifier = modifier
    )
}

@Composable
fun BodyMText(text: String,@SuppressLint("ModifierParameter")  modifier: Modifier = Modifier,color: Color = Color.Black) {
    CustomText(
        text = text,
        fontSize = 16f,
        fontWeight = FontWeight.Normal,
        modifier = modifier,
        color = color
    )
}