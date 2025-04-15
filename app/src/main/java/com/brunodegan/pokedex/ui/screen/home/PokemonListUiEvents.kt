package com.brunodegan.pokedex.ui.screen.home

sealed interface PokemonListUiEvents {
    data class OnPokemonClickedUiEvent(val id: Int) : PokemonListUiEvents
    data class OnRetryButtonClickedUiEvent(val errorMessage: String) : PokemonListUiEvents
}