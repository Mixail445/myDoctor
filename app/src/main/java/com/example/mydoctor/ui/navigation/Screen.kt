package com.example.mydoctor.ui.navigation

sealed class Screen(
    val screenName: String,
    val titleResourceId: Int,
) {
    /**
     * Экран для добавления давления.
     */
    data object ScreenAddPressure : Screen("screenAddPressure", -1)
    /**
     * Экран для отображения данных о давлении.
     */
    data object Pressure : Screen("pressure", -1)
}