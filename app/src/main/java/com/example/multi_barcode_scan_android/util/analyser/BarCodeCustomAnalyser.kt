package com.example.multi_barcode_scan_android.util.analyser

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.example.multi_barcode_scan_android.BarcodeAnalyzerListener
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage

class BarCodeCustomAnalyser(
    private val barcodeListener: BarcodeAnalyzerListener,
) : ImageAnalysis.Analyzer {
    // Get an instance of BarcodeScanner
    private val options =
        BarcodeScannerOptions.Builder().setBarcodeFormats(
            Barcode.FORMAT_CODE_39,
            Barcode.FORMAT_CODE_93,
            Barcode.FORMAT_CODE_128,
            Barcode.FORMAT_CODABAR,
            Barcode.FORMAT_EAN_13,
            Barcode.FORMAT_EAN_8,
            Barcode.FORMAT_ITF,
            Barcode.FORMAT_UPC_A,
            Barcode.FORMAT_UPC_E,
        ).build()

    private val scanner by lazy {
        BarcodeScanning.getClient(options)
    }

    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            // process the image
            scanner.process(image)
                .addOnSuccessListener { barcodes ->

                    // Task completed successfully
                    if (barcodes.isEmpty().not()) {
                        barcodeListener(barcodes)
                    }
                }
                .addOnFailureListener {
                    it.printStackTrace()
                }
                .addOnCompleteListener {
                    // It's important to close the imageProxy
                    imageProxy.close()
                }
        }
    }
}
