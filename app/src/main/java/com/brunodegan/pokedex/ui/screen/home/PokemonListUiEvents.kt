package com.brunodegan.pokedex.ui.screen.home

sealed interface PokemonListUiEvents {
    data class OnPokemonClickedUiEvent(val pokemonId: String) : PokemonListUiEvents
    data class OnRetryButtonClickedUiEvent(val errorMessage: String) : PokemonListUiEvents
}