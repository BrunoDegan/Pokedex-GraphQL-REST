package com.brunodegan.pokedex.ui.screen.details

sealed interface PokemonDetailsUiEvents {
    data class OnRetryButtonClicked(val id: Int, val errorMessage: String) :
        PokemonDetailsUiEvents
}