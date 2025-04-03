package com.brunodegan.pokedex.ui.screen.details

import com.brunodegan.pokedex.base.errors.ErrorData
import com.brunodegan.pokedex.ui.models.PokemonDetailsViewData

sealed interface PokemonDetailsUiState {
    data object Initial : PokemonDetailsUiState
    data object Loading : PokemonDetailsUiState
    data class Success(val viewData: PokemonDetailsViewData) : PokemonDetailsUiState
    data class Error(val error: ErrorData) : PokemonDetailsUiState
}