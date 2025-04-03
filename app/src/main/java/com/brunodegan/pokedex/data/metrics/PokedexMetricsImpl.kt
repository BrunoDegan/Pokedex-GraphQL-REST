package com.brunodegan.pokedex.data.metrics

import android.util.Log

private const val TAG = "AnalyticsHelper"

class PokedexMetricsImpl : MyMoneyConversorMetrics {
    override fun logEvent(event: PokedexAnalyticsData) {
        Log.d(TAG, "Received analytics event: $event")
    }
}