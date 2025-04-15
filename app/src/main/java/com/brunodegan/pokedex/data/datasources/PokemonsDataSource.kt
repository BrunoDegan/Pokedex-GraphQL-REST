package com.brunodegan.pokedex.data.datasources

import com.brunodegan.pokedex.data.models.GetAllPokemonsGraphQLResponseData
import com.brunodegan.pokedex.data.models.PokemonDetailsRestApiModel
import kotlinx.coroutines.flow.Flow

interface PokemonsDataSource {
    suspend fun getPokemons(): Flow<GetAllPokemonsGraphQLResponseData>
    suspend fun getPokemonById(id: Int): Flow<PokemonDetailsRestApiModel>
}