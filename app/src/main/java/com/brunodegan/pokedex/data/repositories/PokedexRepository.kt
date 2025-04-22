package com.brunodegan.pokedex.data.repositories

import com.brunodegan.pokedex.base.network.base.NetworkResponse
import com.brunodegan.pokedex.data.models.PokemonDetailsRestApiModel
import com.brunodegan.pokedex.data.models.PokemonsGraphQLApiModel
import kotlinx.coroutines.flow.Flow

interface PokedexRepository {
    suspend fun getPokemons(): Flow<NetworkResponse<PokemonsGraphQLApiModel>>
    suspend fun getPokemonById(id: Int): Flow<NetworkResponse<PokemonDetailsRestApiModel>>
}