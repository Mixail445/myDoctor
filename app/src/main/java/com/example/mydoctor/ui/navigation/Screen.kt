package com.example.mydoctor.ui.navigation

sealed class Screen(
    val screenName: String,
    val titleResourceId: Int,
) {
    data object ScreenAddPressure : Screen("screenAddPressure", -1)

    data object Pressure : Screen("pressure", -1)
}