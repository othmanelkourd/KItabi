package com.smile.kitabi

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.smile.kitabi.data.repository.FakeBooksRepository
import com.smile.kitabi.domain.usecase.GetRentedBooksUseCase
import com.smile.kitabi.ui.home.HomeScreen
import com.smile.kitabi.ui.home.HomeViewModel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import kitabi.composeapp.generated.resources.Res
import kitabi.composeapp.generated.resources.compose_multiplatform

@Composable
fun App(viewModel: HomeViewModel) {
    MaterialTheme {
        HomeScreen(viewModel)
    }
}