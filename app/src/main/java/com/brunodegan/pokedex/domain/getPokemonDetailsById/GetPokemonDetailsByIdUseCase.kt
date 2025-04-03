package com.brunodegan.pokedex.domain.getPokemonDetailsById

import com.brunodegan.pokedex.ui.models.PokemonDetailsViewData
import kotlinx.coroutines.flow.Flow

interface GetPokemonDetailsByIdUseCase {
    suspend fun invoke(id: String): Flow<PokemonDetailsViewData?>
}