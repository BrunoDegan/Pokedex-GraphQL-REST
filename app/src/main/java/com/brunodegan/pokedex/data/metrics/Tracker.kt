package com.brunodegan.pokedex.data.metrics

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect

@Composable
fun TrackScreen(
    screenName: String,
    analyticsHelper: Metrics = LocalMetrics.current,
) = DisposableEffect(Unit) {
    analyticsHelper.onEnteredScreen(screenName = screenName)
    onDispose {}
}