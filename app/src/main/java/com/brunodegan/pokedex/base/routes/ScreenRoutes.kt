package com.brunodegan.pokedex.base.routes

import kotlinx.serialization.Serializable

@Serializable
object HomeRoute

@Serializable
data class DetailsRoute(
    val id: String
)

enum class ScreenRoutes(name: String) {
    HOME("Home"),
    DETAILS("Details")
}