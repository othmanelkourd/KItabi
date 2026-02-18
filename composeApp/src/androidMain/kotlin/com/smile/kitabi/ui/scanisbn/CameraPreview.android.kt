@file:Suppress("DEPRECATION")

package com.smile.kitabi.ui.scanisbn

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import kotlin.OptIn
import java.util.concurrent.Executors

@Composable
actual fun CameraPreview(
    modifier: Modifier,
    onBarcodeDetected: (String) -> Unit,
    onError: (String) -> Unit
) {
    val context = LocalContext.current
    val cameraPermissionGranted = remember {
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            onError("Camera permission denied")
        }
    }

    LaunchedEffect(Unit) {
        if (!cameraPermissionGranted) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    if (cameraPermissionGranted) {
        AndroidView(
            factory = { ctx ->
                PreviewView(ctx).apply {
                    val cameraExecutor = Executors.newSingleThreadExecutor()
                    val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)

                    cameraProviderFuture.addListener({
                        val cameraProvider = cameraProviderFuture.get()

                        // Setup Preview
                        val preview = Preview.Builder().build().also {
                            it.setSurfaceProvider(this.surfaceProvider)
                        }

                        // Setup Image Analysis for barcode detection
                        val imageAnalysis = ImageAnalysis.Builder()
                            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                            .build()
                            .also { analysis ->
                                analysis.setAnalyzer(cameraExecutor) { imageProxy ->
                                    processImageForBarcode(
                                        imageProxy,
                                        onBarcodeDetected,
                                        onError
                                    )
                                }
                            }

                        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                        try {
                            cameraProvider.unbindAll()
                            cameraProvider.bindToLifecycle(
                                ctx as LifecycleOwner,
                                cameraSelector,
                                preview,
                                imageAnalysis
                            )
                        } catch (e: Exception) {
                            onError("Failed to start camera: ${e.message}")
                        }
                    }, ContextCompat.getMainExecutor(ctx))
                }
            },
            modifier = modifier
        )
    } else {
        Box(
            modifier = modifier
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "Camera permission required",
                color = Color.White
            )
        }
    }
}

@androidx.annotation.OptIn(ExperimentalGetImage::class)
@OptIn(ExperimentalGetImage::class)
private fun processImageForBarcode(
    imageProxy: ImageProxy,
    onBarcodeDetected: (String) -> Unit,
    onError: (String) -> Unit
) {
    try {
        @Suppress("UNCHECKED_CAST")
        val media = imageProxy.image
        if (media != null) {
            val inputImage = InputImage.fromMediaImage(
                media,
                imageProxy.imageInfo.rotationDegrees
            )

            val scanner = BarcodeScanning.getClient()
            scanner.process(inputImage)
                .addOnSuccessListener { barcodes ->
                    barcodes.forEach { barcode ->
                        barcode.rawValue?.let { isbn ->
                            if (isbn.isNotEmpty()) {
                                onBarcodeDetected(isbn)
                            }
                        }
                    }
                }
                .addOnFailureListener { e ->
                    onError("Barcode scanning error: ${e.message}")
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }
        } else {
            imageProxy.close()
        }
    } catch (e: Exception) {
        onError("Image processing error: ${e.message}")
        imageProxy.close()
    }
}




