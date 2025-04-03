package com.brunodegan.pokedex.domain.getPokemonDetailsByName

import com.brunodegan.pokedex.ui.models.PokemonDetailsViewData
import kotlinx.coroutines.flow.Flow

interface GetPokemonDetailsByNameUseCase {
    suspend fun invoke(name: String): Flow<PokemonDetailsViewData?>
}