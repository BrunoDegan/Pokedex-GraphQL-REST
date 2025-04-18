package com.brunodegan.pokedex.ui.screen.home.events

sealed interface PokemonListUiEvents {
    data class OnPokemonClickedUiEvent(val id: Int) : PokemonListUiEvents
    data class OnRetryButtonClickedUiEvent(val errorMessage: String) : PokemonListUiEvents
}