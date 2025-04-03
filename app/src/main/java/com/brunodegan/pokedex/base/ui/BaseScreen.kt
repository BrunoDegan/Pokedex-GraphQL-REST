package com.brunodegan.pokedex.base.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun BaseScreen(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    snackbar: @Composable () -> Unit,
    topBar: @Composable () -> Unit,
    body: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        modifier = modifier,
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing),
            ) {
                snackbar()
            }
        },
        topBar = {
            topBar()
        },
        content = { innerPadding ->
            body(innerPadding)
        }
    )
}