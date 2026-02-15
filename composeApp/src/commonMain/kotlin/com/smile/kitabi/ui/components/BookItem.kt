package com.smile.kitabi.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.smile.kitabi.domain.model.Book
import com.smile.kitabi.domain.model.remainingDays
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

@Composable
fun BookItem(book: Book) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp)) {

            KamelImage(
                resource = asyncPainterResource(book.imageUrl),
                contentDescription = book.title,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
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