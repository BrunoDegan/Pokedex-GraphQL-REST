package com.brunodegan.pokedex.domain.getPokemonDetailsById

import com.brunodegan.pokedex.base.network.base.NetworkResponse
import com.brunodegan.pokedex.data.models.PokemonDetailsRestApiModel
import kotlinx.coroutines.flow.Flow

interface GetPokemonDetailsByIdUseCase {
    suspend operator fun invoke(id: Int): Flow<NetworkResponse<PokemonDetailsRestApiModel>>
}