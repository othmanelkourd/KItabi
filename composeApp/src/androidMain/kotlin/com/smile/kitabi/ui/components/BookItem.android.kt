package com.smile.kitabi.ui.components

import android.graphics.BitmapFactory
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap

@Composable
actual fun rememberImageBitmapFromBytes(imageBytes: ByteArray): ImageBitmap? {
    if (imageBytes.isEmpty()) return null

    return try {
        val androidBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        androidBitmap?.asImageBitmap()
    } catch (_: Exception) {
        null
    }
}