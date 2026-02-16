package com.smile.kitabi.ui.addbook

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.readBytes
import platform.UIKit.UIApplication
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation
import platform.UIKit.UIImagePickerController
import platform.UIKit.UIImagePickerControllerDelegateProtocol
import platform.UIKit.UIImagePickerControllerEditedImage
import platform.UIKit.UIImagePickerControllerOriginalImage
import platform.UIKit.UIImagePickerControllerSourceType
import platform.UIKit.UINavigationControllerDelegateProtocol
import platform.UIKit.UIViewController
import platform.darwin.NSObject
import platform.darwin.NSObjectMeta

/**
 * iOS implementation of ImagePicker using UIImagePickerController.
 */
@Composable
actual fun createImagePicker(): ImagePicker {
    return remember {
        ImagePickerIOS()
    }
}

private class ImagePickerIOS : ImagePicker {

    private var onImagePickedCallback: ((ImageBitmap, ByteArray) -> Unit)? = null

    override fun pickImage(onImagePicked: (ImageBitmap, ByteArray) -> Unit) {
        onImagePickedCallback = onImagePicked

        // Get the current UIViewController
        val rootViewController = getCurrentUIViewController()
            ?: run {
                println("Failed to get current UIViewController")
                return
            }

        // Create UIImagePickerController with Camera source
        val imagePickerController = UIImagePickerController().apply {
            sourceType = UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypeCamera
            delegate = ImagePickerDelegate { image ->
                handlePickedImage(image)
            }
            allowsEditing = true
        }

        // Present the picker
        rootViewController.presentViewController(imagePickerController, animated = true, completion = null)
    }

    private fun handlePickedImage(image: UIImage) {
        // Convert UIImage to ByteArray (JPEG format)
        val imageData = imageToByteArray(image)
        val imageBitmap = imageToImageBitmap(image)

        if (imageData.isNotEmpty()) {
            onImagePickedCallback?.invoke(imageBitmap, imageData)
        }
    }

    private fun getCurrentUIViewController(): UIViewController? {
        // Get the root view controller from the key window
        val keyWindow = UIApplication.sharedApplication.keyWindow
        return keyWindow?.rootViewController
    }
}

/**
 * UIImagePickerController delegate to handle image selection.
 */
private class ImagePickerDelegate(
    private val onImagePicked: (UIImage) -> Unit
) : NSObject(), UIImagePickerControllerDelegateProtocol, UINavigationControllerDelegateProtocol {

    companion object : NSObjectMeta()

    override fun imagePickerController(
        picker: UIImagePickerController,
        didFinishPickingMediaWithInfo: Map<Any?, *>
    ) {
        // Get the selected image (prefer edited image if available)
        val image = didFinishPickingMediaWithInfo[UIImagePickerControllerEditedImage] as? UIImage
            ?: didFinishPickingMediaWithInfo[UIImagePickerControllerOriginalImage] as? UIImage

        image?.let { selectedImage ->
            onImagePicked(selectedImage)
        }

        // Dismiss the picker
        picker.dismissViewControllerAnimated(true, completion = null)
    }

    override fun imagePickerControllerDidCancel(picker: UIImagePickerController) {
        // Dismiss the picker when user cancels
        picker.dismissViewControllerAnimated(true, completion = null)
    }
}

/**
 * Convert UIImage to ByteArray (JPEG format with 85% quality).
 */
@OptIn(ExperimentalForeignApi::class)
private fun imageToByteArray(image: UIImage): ByteArray {
    val imageData = UIImageJPEGRepresentation(image, 0.85)
        ?: return byteArrayOf()

    // Convert NSData to ByteArray
    val length = imageData.length.toInt()
    val bytesPointer = imageData.bytes ?: return byteArrayOf()

    return bytesPointer.readBytes(length)
}

/**
 * Convert UIImage to Compose ImageBitmap.
 * Note: Full implementation would require Skiko or similar for proper conversion.
 * This is a placeholder that returns an empty ImageBitmap.
 */
@Suppress("UNUSED_PARAMETER")
private fun imageToImageBitmap(image: UIImage): ImageBitmap {
    // TODO: Implement proper UIImage to ImageBitmap conversion
    // This requires platform-specific bindings or Skiko
    // For now, return a placeholder
    return ImageBitmap(1, 1)
}