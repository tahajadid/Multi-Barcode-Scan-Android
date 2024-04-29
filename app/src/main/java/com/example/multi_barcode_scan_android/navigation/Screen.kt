package com.example.multi_barcode_scan_android.navigation

sealed class Screen(val route: String) {
    object CameraPreviewScreen : Screen("camera_preview_screen")
    object ResultScreen : Screen("result_screen")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach {
                append("/$it")
            }
        }
    }
}
