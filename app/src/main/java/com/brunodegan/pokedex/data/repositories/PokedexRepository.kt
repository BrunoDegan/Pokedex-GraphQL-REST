package com.brunodegan.pokedex.data.repositories

import com.brunodegan.pokedex.data.models.GetAllPokemonsGraphQLResponseData
import com.brunodegan.pokedex.data.models.PokemonDetails
import kotlinx.coroutines.flow.Flow

interface PokedexRepository {
    suspend fun getPokemons(): Flow<Result<GetAllPokemonsGraphQLResponseData>>
    suspend fun getPokemonByName(name: String): Flow<Result<PokemonDetails>>
    suspend fun getPokemonById(id: String): Flow<Result<PokemonDetails>>
}