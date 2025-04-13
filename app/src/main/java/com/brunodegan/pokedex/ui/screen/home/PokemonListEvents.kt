package com.brunodegan.pokedex.ui.screen.home

sealed interface PokemonListEvents {
    data class OnPokemonClicked(val pokemonId: String) : PokemonListEvents
    data class OnRetryButtonClicked(val errorMessage: String) : PokemonListEvents
}