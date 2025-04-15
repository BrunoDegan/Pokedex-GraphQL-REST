package com.brunodegan.pokedex.base.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.IntSize
import com.brunodegan.pokedex.R

@Composable
fun BaseScreen(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    snackbar: @Composable () -> Unit,
    topBar: @Composable () -> Unit,
    body: @Composable (PaddingValues) -> Unit,
) {
    var buttonSize by remember { mutableStateOf(IntSize.Zero) }
    val density = LocalDensity.current
    val offsetDp =
        with(density) { buttonSize.height.toDp() + dimensionResource(R.dimen.double_padding) }
    Scaffold(modifier = modifier, snackbarHost = {
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.onError)
                .offset(y = -offsetDp)
        ) {
            snackbar()
        }
    }, topBar = topBar, content = { innerPadding ->
        body(innerPadding)
    })
}