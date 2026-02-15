package com.smile.kitabi

import androidx.compose.ui.window.ComposeUIViewController
import com.smile.kitabi.di.initKoin
import com.smile.kitabi.ui.home.HomeViewModel
import org.koin.compose.KoinContext
import org.koin.compose.koinInject


// Keep this in iosMain (top-level file) so it survives recompositions
private var koinStarted = false

fun MainViewController() = ComposeUIViewController {

    if (!koinStarted) {
        initKoin()
        koinStarted = true
    }

    KoinContext {
        val viewModel: HomeViewModel = koinInject()
        App(viewModel = viewModel)
    }
}