package com.brunodegan.pokedex.base.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.core.content.res.ResourcesCompat
import com.brunodegan.pokedex.R

@Composable
fun LoaderUiState(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                color = Color.Black.copy(
                    alpha = ResourcesCompat.getFloat(
                        LocalContext.current.resources, R.dimen.progress_circular_indicator_alpha
                    )
                )
            ),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .background(Color.Transparent)
                .align(Alignment.Center)
                .size(dimensionResource(R.dimen.circular_progress_indicator_size)),
            color = MaterialTheme.colorScheme.tertiary,
        )
    }
}

