package com.brunodegan.pokedex.domain.getPokemonsUseCase

import com.brunodegan.pokedex.base.network.base.NetworkResponse
import com.brunodegan.pokedex.data.models.PokemonsGraphQLApiModel
import kotlinx.coroutines.flow.Flow

interface GetPokemonsDataUseCase {
    suspend operator fun invoke(): Flow<NetworkResponse<PokemonsGraphQLApiModel>>
}