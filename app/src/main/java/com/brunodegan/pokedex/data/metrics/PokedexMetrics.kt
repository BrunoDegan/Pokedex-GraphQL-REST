package com.brunodegan.pokedex.data.metrics

import androidx.compose.runtime.staticCompositionLocalOf

val LocalMetrics = staticCompositionLocalOf<MyMoneyConversorMetrics> {
    PokedexMetricsImpl()
}

fun MyMoneyConversorMetrics.onEnteredScreen(screenName: String) {
    logEvent(
        event = PokedexAnalyticsData(
            type = PokedexAnalyticsData.Types.SCREEN_VIEW,
            extras = listOf(
                PokedexAnalyticsData.Param(
                    key = PokedexAnalyticsData.ParamKeys.SCREEN_NAME,
                    value = screenName
                )
            )
        )
    )
}

interface MyMoneyConversorMetrics {
    fun logEvent(event: PokedexAnalyticsData)
}