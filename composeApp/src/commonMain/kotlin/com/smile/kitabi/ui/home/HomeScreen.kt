package com.smile.kitabi.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.unit.dp
import com.smile.kitabi.data.repository.FakeBooksRepository
import com.smile.kitabi.domain.usecase.GetRentedBooksUseCase
import com.smile.kitabi.ui.components.BookItem
import kotlinx.coroutines.flow.collectLatest
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onScanIsbnClick: () -> Unit = {},
    onAddManuallyClick: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // BottomSheet state (KMP-friendly)
    var showSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

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

    Scaffold(
        topBar = { TopAppBar(title = { Text("Home") }) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showSheet = true }
            ) {
                Text("+")
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            items(state.books) { book ->
                BookItem(book)
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
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        Text(text = "Add a book")

        Spacer(modifier = Modifier.height(12.dp))

        SheetOption(
            title = "Scan ISBN code",
            subtitle = "Use your camera to scan the barcode",
            onClick = onScanIsbn
        )

        Spacer(modifier = Modifier.height(8.dp))

        SheetOption(
            title = "Add manually",
            subtitle = "Enter title, author, ISBN…",
            onClick = onAddManually
        )

        Spacer(modifier = Modifier.height(24.dp))
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
            .clickable(onClick = onClick)
            .padding(vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = subtitle)
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