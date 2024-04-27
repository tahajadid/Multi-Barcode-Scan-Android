package com.example.multi_barcode_scan_android

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import android.Manifest
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import com.example.multi_barcode_scan_android.screen.CameraPreviewScreen
import com.example.multi_barcode_scan_android.theme.MultiBarcodeScanAndroidTheme
import com.google.mlkit.vision.barcode.Barcode
import java.util.concurrent.atomic.AtomicBoolean

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
                setCameraPreview()
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
            ) -> {
                setCameraPreview()
            }
            else -> {
                cameraPermissionRequest.launch(Manifest.permission.CAMERA)
            }
        }
    }

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
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MultiBarcodeScanAndroidTheme {
        Greeting("Android")
    }
}

