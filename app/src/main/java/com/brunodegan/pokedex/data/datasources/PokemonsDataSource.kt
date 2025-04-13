package com.brunodegan.pokedex.data.datasources

import com.brunodegan.pokedex.data.models.GetAllPokemonsGraphQLResponseData
import com.brunodegan.pokedex.data.models.PokemonDetailsRestApiModel

interface PokemonsDataSource {
    suspend fun getPokemons(): GetAllPokemonsGraphQLResponseData
    suspend fun getPokemonById(id: String): PokemonDetailsRestApiModel
    suspend fun getPokemonByName(name: String): PokemonDetailsRestApiModel
}