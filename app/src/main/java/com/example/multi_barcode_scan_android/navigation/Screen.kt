package com.example.multi_barcode_scan_android.navigation

sealed class Screen(val route: String) {
    object CameraPreviewScreen : Screen("camera_preview_screen")

    object ResultScreen : Screen("result_screen")
}
