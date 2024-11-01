package com.example.mydoctor.ui.screens

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mydoctor.ui.navigation.Screen
import com.example.mydoctor.ui.screens.addPressure.ScreenAddPressure
import com.example.mydoctor.ui.screens.pressure.ScreenPressure


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ComposeApp() {
    val navController = rememberNavController()
    Scaffold {
        NavHost(navController = navController, startDestination = Screen.Pressure.screenName) {
            composable(Screen.ScreenAddPressure.screenName) { ScreenAddPressure(navController) }
            composable(Screen.Pressure.screenName) { ScreenPressure(navController) }
        }
    }
}