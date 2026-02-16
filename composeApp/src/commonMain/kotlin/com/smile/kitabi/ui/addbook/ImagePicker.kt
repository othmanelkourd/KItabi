package com.smile.kitabi.ui.addbook

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap

/**
 * Interface for picking images from camera or gallery.
 * Platform-specific implementations will handle the actual image picking.
 */
interface ImagePicker {
    /**
     * Launch the image picker (camera or gallery).
     * @param onImagePicked Callback with the selected image as ImageBitmap and its ByteArray
     */
    fun pickImage(onImagePicked: (ImageBitmap, ByteArray) -> Unit)
}

/**
 * Expect declaration for creating a platform-specific ImagePicker.
 */
@Composable
expect fun createImagePicker(): ImagePicker