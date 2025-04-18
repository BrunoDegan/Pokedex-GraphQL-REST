package com.brunodegan.pokedex.data.repositories

import com.brunodegan.pokedex.data.models.GetAllPokemonsGraphQLApiModel
import com.brunodegan.pokedex.data.models.PokemonDetailsRestApiModel
import kotlinx.coroutines.flow.Flow

interface PokedexRepository {
    suspend fun getPokemons(): Flow<GetAllPokemonsGraphQLApiModel>
    suspend fun getPokemonById(id: Int): Flow<PokemonDetailsRestApiModel>
}