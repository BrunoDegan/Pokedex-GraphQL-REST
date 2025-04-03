package com.brunodegan.pokedex.domain.getPokemonsUseCase

import com.brunodegan.pokedex.ui.models.PokemonListViewData
import kotlinx.coroutines.flow.Flow

interface GetPokemonsDataUseCase {
    suspend fun invoke(errorMessage: String? = null): Flow<List<PokemonListViewData>?>
}