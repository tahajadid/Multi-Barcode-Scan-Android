package com.example.multi_barcode_scan_android

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import android.Manifest
import androidx.compose.runtime.Composable
import androidx.core.content.ContextCompat
import com.example.multi_barcode_scan_android.navigation.Navigation
import com.example.multi_barcode_scan_android.theme.MultiBarcodeScanAndroidTheme
import com.google.mlkit.vision.barcode.Barcode


typealias BarcodeAnalyzerListener = (barcode: MutableList<Barcode>) -> Unit

class MainActivity : ComponentActivity() {

    companion object {
        lateinit var activityInstance: MainActivity
    }

    private val cameraPermissionRequest =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                //setCameraPreview()
            } else {
                // Camera permission denied
            }

        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityInstance = this

        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ),
            -> {
                setContent {
                    MultiBarcodeScanAndroidTheme {
                        initNavigation()
                    }
                }

                //setCameraPreview()
            }
            else -> {
                cameraPermissionRequest.launch(Manifest.permission.CAMERA)
            }
        }
    }

    @Composable
    fun initNavigation() {
        Navigation()
    }
}