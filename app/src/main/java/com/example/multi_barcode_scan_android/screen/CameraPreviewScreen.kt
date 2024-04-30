package com.example.multi_barcode_scan_android.screen

import android.content.Context
import android.os.Handler
import android.util.DisplayMetrics
import android.util.Log
import androidx.camera.core.AspectRatio
import androidx.camera.core.CameraControl
import androidx.camera.core.CameraInfo
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.core.UseCase
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.multi_barcode_scan_android.MainActivity
import com.example.multi_barcode_scan_android.navigation.Screen
import com.example.multi_barcode_scan_android.util.analyser.BarCodeAndQRCodeAnalyser
import com.example.multi_barcode_scan_android.util.constants.Constants.RATIO_16_9_VALUE
import com.example.multi_barcode_scan_android.util.constants.Constants.RATIO_4_3_VALUE
import com.example.multi_barcode_scan_android.util.constants.actualValues
import com.google.mlkit.vision.barcode.Barcode
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

private var processingBarcode = AtomicBoolean(false)
private val executor by lazy {
    Executors.newSingleThreadExecutor()
}
private lateinit var cameraInfo: CameraInfo
private lateinit var cameraControl: CameraControl
private var isFirst = false
private lateinit var actualNavController: NavController

@Suppress("ktlint:standard:function-naming")
@Composable
fun CameraPreviewScreen(navController: NavController) {
    actualNavController = navController
    val lensFacing = CameraSelector.LENS_FACING_BACK
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val preview = Preview.Builder().build()

    val previewView =
        remember {
            PreviewView(context)
        }

    val cameraxSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()

    LaunchedEffect(lensFacing) {
        val cameraProvider = context.getCameraProvider()
        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(lifecycleOwner, cameraxSelector, preview)
        preview.setSurfaceProvider(previewView.surfaceProvider)
    }

    AndroidView(factory = { previewView }, modifier = Modifier.fillMaxSize())

    previewView.post {
        startCamera(previewView)
    }
}

private suspend fun Context.getCameraProvider(): ProcessCameraProvider =
    suspendCoroutine {
            continuation ->
        ProcessCameraProvider.getInstance(this).also { cameraProvider ->
            cameraProvider.addListener({
                continuation.resume(cameraProvider.get())
            }, ContextCompat.getMainExecutor(this))
        }
    }

private fun startCamera(previewView: PreviewView) {
    // Get screen metrics used to setup camera for full screen resolution
    val metrics = DisplayMetrics().also { previewView.display.getRealMetrics(it) }
    val screenAspectRatio = aspectRatio(metrics.widthPixels, metrics.heightPixels)
    val rotation = previewView.display.rotation

    // Bind the CameraProvider to the LifeCycleOwner
    val cameraSelector =
        CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()
    val cameraProviderFuture = ProcessCameraProvider.getInstance(MainActivity.activityInstance.applicationContext)
    cameraProviderFuture.addListener({
        // CameraProvider
        val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

        // Preview
        val preview =
            Preview.Builder()
                .setTargetAspectRatio(screenAspectRatio)
                // Set initial target rotation
                .setTargetRotation(rotation)
                .build()

        preview.setSurfaceProvider(previewView.surfaceProvider)

        // ImageAnalysis
        val textBarcodeAnalyzer = initializeAnalyzer(screenAspectRatio, rotation)
        cameraProvider.unbindAll()

        try {
            val camera =
                cameraProvider.bindToLifecycle(
                    MainActivity.activityInstance,
                    cameraSelector,
                    preview,
                    textBarcodeAnalyzer,
                )
            cameraControl = camera.cameraControl
            cameraInfo = camera.cameraInfo
            cameraControl.setLinearZoom(0.5f)
        } catch (exc: Exception) {
            exc.printStackTrace()
            // Log.e(TAG, "Use case binding failed", exc)
        }
    }, ContextCompat.getMainExecutor(MainActivity.activityInstance.applicationContext))
}

private fun initializeAnalyzer(
    screenAspectRatio: Int,
    rotation: Int,
): UseCase {
    return ImageAnalysis.Builder()
        .setTargetAspectRatio(screenAspectRatio)
        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
        .setTargetRotation(rotation)
        .build()
        .also {
            it.setAnalyzer(
                executor,
                @Suppress("ktlint:standard:no-consecutive-comments")
                BarCodeAndQRCodeAnalyser { barcode ->
                    /**
                     * Change update  to true if you want to scan only one barcode or it will
                     * continue scanning after detecting for the first time
                     */
                    /**
                     * Change update  to true if you want to scan only one barcode or it will
                     * continue scanning after detecting for the first time
                     */
                    if (processingBarcode.compareAndSet(false, false)) {
                        onBarcodeDetected(barcode)
                    }
                },
            )
        }
}

fun onBarcodeDetected(barcode: MutableList<Barcode>) {
    Log.d("valueSprin", "Start ---- - -- -- - -- --  : ")
    barcode.forEach {
        Log.d("valueSprin", "Value : " + it.rawValue.toString())
    }
    Log.d("valueSprin", "+ + + + + + + + ++ + ++ ++ End ")

    if (isFirst) {
        if (barcode.isNotEmpty()) {
            barcode.forEach {
                actualValues.add(it.rawValue.toString())
            }
        }
        isFirst = false
    } else {
        barcode.forEach {
            if (!actualValues.contains(it.rawValue)) actualValues.add(it.rawValue)
        }
        if (actualValues.size > 1) startEndTimer()
    }
}

private fun startEndTimer() {
    Handler().postDelayed({
        actualNavController.navigate(Screen.ResultScreen.route)
    }, 2000)
}

private fun aspectRatio(
    width: Int,
    height: Int,
): Int {
    val previewRatio = max(width, height).toDouble() / min(width, height)
    if (abs(previewRatio - RATIO_4_3_VALUE) <= abs(previewRatio - RATIO_16_9_VALUE)) {
        return AspectRatio.RATIO_4_3
    }
    return AspectRatio.RATIO_16_9
}
