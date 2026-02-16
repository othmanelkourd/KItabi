package com.smile.kitabi.ui.addbook

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import java.io.ByteArrayOutputStream

/**
 * Android implementation of ImagePicker using the Camera.
 */
@Composable
actual fun createImagePicker(): ImagePicker {
    var onImagePickedCallback: ((ImageBitmap, ByteArray) -> Unit)? = null

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        bitmap?.let { capturedBitmap ->
            // Compress to reasonable size (max 1024x1024)
            val scaledBitmap = scaleBitmap(capturedBitmap, 1024)

            // Convert to ByteArray (JPEG format with 85% quality)
            val outputStream = ByteArrayOutputStream()
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 85, outputStream)
            val byteArray = outputStream.toByteArray()
            outputStream.close()

            onImagePickedCallback?.invoke(scaledBitmap.asImageBitmap(), byteArray)
        }
    }

    return remember(cameraLauncher) {
        object : ImagePicker {
            override fun pickImage(onImagePicked: (ImageBitmap, ByteArray) -> Unit) {
                onImagePickedCallback = onImagePicked
                cameraLauncher.launch(null)
            }
        }
    }
}

private fun scaleBitmap(bitmap: Bitmap, maxDimension: Int): Bitmap {
    val width = bitmap.width
    val height = bitmap.height

    if (width <= maxDimension && height <= maxDimension) {
        return bitmap
    }

    val scaleFactor = if (width > height) {
        maxDimension.toFloat() / width
    } else {
        maxDimension.toFloat() / height
    }

    val newWidth = (width * scaleFactor).toInt()
    val newHeight = (height * scaleFactor).toInt()

    return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
}