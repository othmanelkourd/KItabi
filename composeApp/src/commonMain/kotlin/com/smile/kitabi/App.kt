package com.smile.kitabi

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.smile.kitabi.ui.addbook.AddBookScreen
import com.smile.kitabi.ui.addbook.AddBookViewModel
import com.smile.kitabi.ui.home.HomeScreen
import com.smile.kitabi.ui.home.HomeViewModel
import com.smile.kitabi.ui.scanisbn.ScanIsbnScreen
import com.smile.kitabi.ui.scanisbn.ScanIsbnViewModel
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
                val scanIsbnViewModel: ScanIsbnViewModel = koinInject()
                ScanIsbnScreen(
                    viewModel = scanIsbnViewModel,
                    onNavigateBack = {
                        currentScreen = Screen.Home
                    },
                    onIsbnScanned = { isbn ->
                        // TODO: Use the scanned ISBN to fetch book info
                        println("Scanned ISBN: $isbn")
                    }
                )
            }
        }
    }
}