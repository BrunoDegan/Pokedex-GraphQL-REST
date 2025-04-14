package com.brunodegan.pokedex.data.metrics

import android.util.Log
import androidx.compose.runtime.staticCompositionLocalOf

val LocalMetrics = staticCompositionLocalOf<Metrics> {
    MetricsImpl()
}

fun Metrics.onEnteredScreen(screenName: String) {

    val eventData = AnalyticsData(
        eventType = AnalyticsData.EventType.EVENT_TYPE,
        extras = listOf(
            AnalyticsData.Param(
                key = AnalyticsData.ScreenName.SCREEN_NAME.name,
                value = screenName
            )
        )
    )
    logEvent(eventData)
}

interface Metrics {
    fun logEvent(event: AnalyticsData)
}

class MetricsImpl : Metrics {
    override fun logEvent(analyticsData: AnalyticsData) {
        Log.d(
            analyticsData.eventType.name,
            "${analyticsData.extras.first().key} - ${analyticsData.extras.first().value}"
        )
    }
}