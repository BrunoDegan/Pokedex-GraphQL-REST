package com.brunodegan.pokedex.ui.screen.home.state

import com.brunodegan.pokedex.base.errors.ErrorData
import com.brunodegan.pokedex.ui.models.PokemonListViewData

sealed interface PokemonListUiState {
    data object Initial : PokemonListUiState
    data object Loading : PokemonListUiState
    data class Success(val viewData: List<PokemonListViewData>) : PokemonListUiState
    data class Error(val error: ErrorData) : PokemonListUiState
}