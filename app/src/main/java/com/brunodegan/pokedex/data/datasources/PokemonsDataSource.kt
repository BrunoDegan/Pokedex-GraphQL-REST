package com.brunodegan.pokedex.data.datasources

import com.brunodegan.pokedex.data.models.GetAllPokemonsGraphQLResponseData
import com.brunodegan.pokedex.data.models.PokemonDetails

interface PokemonsDataSource {
    suspend fun getPokemons(): GetAllPokemonsGraphQLResponseData
    suspend fun getPokemonById(id: String): PokemonDetails
    suspend fun getPokemonByName(name: String): PokemonDetails
}