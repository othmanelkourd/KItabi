package com.smile.kitabi

import androidx.compose.ui.window.ComposeUIViewController
import com.smile.kitabi.di.initKoin
import org.koin.compose.KoinContext

// Keep this in iosMain (top-level file) so it survives recompositions
private var koinStarted = false

fun MainViewController() = ComposeUIViewController {

    if (!koinStarted) {
        initKoin()
        koinStarted = true
    }

    KoinContext {
        App()
    }
}