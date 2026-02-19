package com.smile.kitabi.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.smile.kitabi.data.repository.FakeBooksRepository
import com.smile.kitabi.domain.usecase.GetRentedBooksUseCase
import com.smile.kitabi.ui.components.BookItem
import kotlinx.coroutines.flow.collectLatest
import androidx.compose.ui.tooling.preview.Preview
import com.smile.kitabi.domain.model.Book

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    refreshTrigger: Int = 0,
    onScanIsbnClick: () -> Unit = {},
    onAddManuallyClick: () -> Unit = {},
    onBookClick: (Book) -> Unit = {}
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // BottomSheet state (KMP-friendly)
    var showSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // Refresh books when returning to this screen or when refreshTrigger changes
    LaunchedEffect(refreshTrigger) {
        viewModel.refreshBooks()
    }

    LaunchedEffect(viewModel) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is HomeUiEvent.ShowSnackbar -> snackbarHostState.showSnackbar(event.message)
            }
        }
    }

    // --- BottomSheet UI ---
    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSheet = false },
            sheetState = sheetState
        ) {
            BottomSheetContent(
                onScanIsbn = {
                    showSheet = false
                    onScanIsbnClick()
                },
                onAddManually = {
                    showSheet = false
                    onAddManuallyClick()
                }
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.surface,
                        MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)
                    )
                )
            )
    ) {
        Scaffold(
            topBar = {
                FancyTopBar(
                    onAddClick = { showSheet = true }
                )
            },
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            containerColor = Color.Transparent
        ) { innerPadding ->

            if (state.books.isEmpty() && !state.isLoading) {
                // Empty State - No books in repository
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    EmptyBooksState(
                        onAddClick = { showSheet = true },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            } else {
                // Books List
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    items(state.books) { book ->
                        BookItem(book, onBookClick = { onBookClick(book) })
                    }
                }
            }
        }
    }
}

@Composable
private fun FancyTopBar(onAddClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .drawBehind {
                // Main gradient background
                val gradient = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF7C8FD6),
                        Color(0xFF9FACDF)
                    ),
                    start = Offset(0f, 0f),
                    end = Offset(size.width, size.height)
                )
                drawRect(gradient, size = size)

                // Draw curved bottom
                val path = androidx.compose.ui.graphics.Path().apply {
                    moveTo(0f, size.height * 0.8f)
                    quadraticBezierTo(
                        size.width * 0.5f,
                        size.height,
                        size.width,
                        size.height * 0.8f
                    )
                    lineTo(size.width, 0f)
                    lineTo(0f, 0f)
                    close()
                }
                drawPath(path, gradient)

                // Geometric decorative shapes
                val lightColor = Color.White.copy(alpha = 0.15f)

                // Large rounded square top right
                drawPath(
                    androidx.compose.ui.graphics.Path().apply {
                        moveTo(size.width * 0.75f, -20f)
                        quadraticBezierTo(
                            size.width * 0.85f,
                            -20f,
                            size.width * 0.85f,
                            size.height * 0.15f
                        )
                        lineTo(size.width + 20f, size.height * 0.15f)
                        lineTo(size.width + 20f, -20f)
                        close()
                    },
                    lightColor
                )

                // Medium rounded square top left
                drawPath(
                    androidx.compose.ui.graphics.Path().apply {
                        moveTo(-20f, size.height * 0.05f)
                        quadraticBezierTo(
                            -20f,
                            size.height * 0.2f,
                            size.width * 0.15f,
                            size.height * 0.2f
                        )
                        lineTo(size.width * 0.15f, -20f)
                        lineTo(-20f, -20f)
                        close()
                    },
                    lightColor
                )

                // Small rounded square middle
                drawPath(
                    androidx.compose.ui.graphics.Path().apply {
                        moveTo(size.width * 0.35f, size.height * 0.25f)
                        quadraticBezierTo(
                            size.width * 0.42f,
                            size.height * 0.25f,
                            size.width * 0.42f,
                            size.height * 0.35f
                        )
                        lineTo(size.width * 0.5f, size.height * 0.35f)
                        lineTo(size.width * 0.5f, size.height * 0.25f)
                        close()
                    },
                    lightColor
                )
            }
    ) {
        // Content: Title and Add button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    "Mes Livres",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    "Suivi de mes lectures",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }

            // Add button circle
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White.copy(alpha = 0.25f))
                    .clickable { onAddClick() }
                    .border(
                        width = 2.dp,
                        color = Color.White.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(16.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = "Add book",
                    modifier = Modifier.size(28.dp),
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
private fun BottomSheetContent(
    onScanIsbn: () -> Unit,
    onAddManually: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 24.dp)
    ) {
        Text(
            text = "Add a book",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(bottom = 20.dp)
        )

        SheetOption(
            title = "Scan ISBN code",
            subtitle = "Use your camera to scan the barcode",
            onClick = onScanIsbn
        )

        Spacer(modifier = Modifier.height(12.dp))

        SheetOption(
            title = "Add manually",
            subtitle = "Enter title, author, ISBN…",
            onClick = onAddManually
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun EmptyBooksState(
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(horizontal = 32.dp)
            .padding(bottom = 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.MenuBook,
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "No books yet",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Start by adding a book to your collection",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        androidx.compose.material3.Button(
            onClick = onAddClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    "Add Your First Book",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}

@Composable
private fun SheetOption(
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.Add,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.padding(horizontal = 12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    val fakeViewModel = HomeViewModel(
        getRentedBooks = GetRentedBooksUseCase(
            repository = FakeBooksRepository()
        )
    )
    
    MaterialTheme {
        HomeScreen(
            viewModel = fakeViewModel,
            onScanIsbnClick = {},
            onAddManuallyClick = {}
        )
    }
}