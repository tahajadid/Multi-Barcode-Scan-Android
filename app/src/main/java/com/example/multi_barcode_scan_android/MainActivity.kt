package com.example.multi_barcode_scan_android

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import android.Manifest
import androidx.core.content.ContextCompat
import com.example.multi_barcode_scan_android.navigation.Navigation
import com.example.multi_barcode_scan_android.screen.CameraPreviewScreen
import com.example.multi_barcode_scan_android.theme.MultiBarcodeScanAndroidTheme
import com.google.mlkit.vision.barcode.Barcode

const val RATIO_4_3_VALUE = 4.0 / 3.0
const val RATIO_16_9_VALUE = 16.0 / 9.0
typealias BarcodeAnalyzerListener = (barcode: MutableList<Barcode>) -> Unit

class MainActivity : ComponentActivity() {

    companion object {
        lateinit var activityInstance: MainActivity
    }

    private val cameraPermissionRequest =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // Navigation()
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
                        Navigation()
                    }
                }

                //setCameraPreview()
            }
            else -> {
                cameraPermissionRequest.launch(Manifest.permission.CAMERA)
            }
        }
    }

    /*
    private fun setCameraPreview() {
        setContent {
            MultiBarcodeScanAndroidTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CameraPreviewScreen()
                }

            }
        }
    }

     */
}