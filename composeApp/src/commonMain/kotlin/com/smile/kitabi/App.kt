package com.smile.kitabi

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.smile.kitabi.ui.addbook.AddBookScreen
import com.smile.kitabi.ui.addbook.AddBookViewModel
import com.smile.kitabi.ui.home.HomeScreen
import com.smile.kitabi.ui.home.HomeViewModel
import org.koin.compose.koinInject

enum class Screen {
    Home,
    AddBook,
    ScanIsbn
}

@Composable
fun App(
    viewModel: HomeViewModel = koinInject()
) {
    var currentScreen by remember { mutableStateOf(Screen.Home) }
    var refreshTrigger by remember { mutableStateOf(0) }

    MaterialTheme {
        when (currentScreen) {
            Screen.Home -> {
                HomeScreen(
                    viewModel = viewModel,
                    refreshTrigger = refreshTrigger,
                    onScanIsbnClick = { currentScreen = Screen.ScanIsbn },
                    onAddManuallyClick = { currentScreen = Screen.AddBook }
                )
            }

            Screen.AddBook -> {
                val addBookViewModel: AddBookViewModel = koinInject()
                AddBookScreen(
                    viewModel = addBookViewModel,
                    onNavigateBack = {
                        refreshTrigger++
                        currentScreen = Screen.Home
                    }
                )
            }

            Screen.ScanIsbn -> {
                // TODO: Implement ISBN scanner screen
                // For now, just go back to home
                Text(
                    "ISBN Scanner - Coming Soon",
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { currentScreen = Screen.Home }
                )
            }
        }
    }
}