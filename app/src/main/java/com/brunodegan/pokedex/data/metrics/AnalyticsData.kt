package com.brunodegan.pokedex.data.metrics

data class AnalyticsData(
    val eventType: EventType,
    val extras: List<Param> = emptyList(),
) {
    data class Param(val key: String, val value: String)

    enum class ScreenName(screenName: String) {
        SCREEN_NAME("screen_name")
    }

    enum class EventType(eventType: String) {
        EVENT_TYPE("event_type")
    }
}