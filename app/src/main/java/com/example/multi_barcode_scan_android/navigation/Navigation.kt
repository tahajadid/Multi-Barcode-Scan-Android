package com.example.multi_barcode_scan_android.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.multi_barcode_scan_android.screen.CameraPreviewScreen
import com.example.multi_barcode_scan_android.screen.ResultScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.CameraPreviewScreen.route
    ) {
        composable(route = Screen.CameraPreviewScreen.route) {
            CameraPreviewScreen(navController = navController)
        }

        composable(route = Screen.ResultScreen.route) {
            ResultScreen(navController = navController)
        }
    }
}