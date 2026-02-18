package com.smile.kitabi.ui.scanisbn

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
expect fun CameraPreview(
    modifier: Modifier = Modifier,
    onBarcodeDetected: (String) -> Unit,
    onError: (String) -> Unit
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanIsbnScreen(
    viewModel: ScanIsbnViewModel,
    onNavigateBack: () -> Unit,
    onIsbnScanned: (com.smile.kitabi.domain.model.GoogleBookInfo) -> Unit
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.successMessage) {
        state.successMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.clearSuccess()
        }
    }

    LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let { error ->
            snackbarHostState.showSnackbar(error)
            viewModel.clearError()
        }
    }

    LaunchedEffect(state.bookInfo) {
        if (state.bookInfo != null) {
            onIsbnScanned(state.bookInfo!!)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Scan Book Barcode") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Show camera while not loading and no book info
            if (!state.isLoadingBookInfo && state.bookInfo == null) {
                CameraPreview(
                    modifier = Modifier.fillMaxSize(),
                    onBarcodeDetected = { barcode ->
                        viewModel.onBarcodeDetected(barcode)
                    },
                    onError = { error ->
                        viewModel.onScanningError(error)
                    }
                )
            }

            // Show loading spinner while fetching book info
            if (state.isLoadingBookInfo) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
                ) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Fetching book information...")
                }
            }

            // Instructions overlay at the bottom (only show when scanning)
            if (!state.isLoadingBookInfo && state.bookInfo == null) {
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.9f))
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Position the barcode in the frame",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = onNavigateBack) {
                        Text("Cancel")
                    }
                }
            }
        }
    }}
