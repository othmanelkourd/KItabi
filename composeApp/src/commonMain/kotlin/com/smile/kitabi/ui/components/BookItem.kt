package com.smile.kitabi.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.smile.kitabi.domain.model.Book
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import com.smile.kitabi.domain.model.remainingDays
import androidx.compose.foundation.clickable

@Composable
fun BookItem(
    book: Book,
    onBookClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onBookClick() },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp)) {

            // Display image from ByteArray or URL
            BookCoverImage(
                imageBytes = book.imageBytes,
                imageUrl = book.imageUrl,
                title = book.title,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = book.description,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2
                )

                Spacer(Modifier.height(6.dp))

                Text(
                    text = "⏳ ${book.remainingDays()} days left",
                    color = if (book.remainingDays() <= 3) Color.Red else Color.Gray,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}

@Composable
private fun BookCoverImage(
    imageBytes: ByteArray,
    imageUrl: String,
    title: String,
    modifier: Modifier = Modifier
) {
    // Priority: imageBytes > imageUrl > placeholder
    val imageBitmap = rememberImageBitmapFromBytes(imageBytes)

    if (imageBitmap != null) {
        // Use local image bytes if available
        Image(
            bitmap = imageBitmap,
            contentDescription = title,
            modifier = modifier,
            contentScale = ContentScale.Crop
        )
    } else if (imageUrl.isNotEmpty()) {
        // Use image URL from Google Books API if no local bytes
        RemoteImage(
            url = imageUrl,
            contentDescription = title,
            modifier = modifier
        )
    } else {
        // Fallback placeholder when no image is available
        Box(
            modifier = modifier.background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Book,
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun RemoteImage(
    url: String,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    KamelImage(
        asyncPainterResource(url),
        contentDescription = contentDescription,
        modifier = modifier,
        contentScale = ContentScale.Crop,
        onFailure = {
            // Show placeholder on load failure
            Box(
                modifier = modifier.background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Book,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    )
}

/**
 * Converts ByteArray to ImageBitmap. Platform-specific implementation.
 */
@Composable
expect fun rememberImageBitmapFromBytes(imageBytes: ByteArray): ImageBitmap?