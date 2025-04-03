package com.brunodegan.pokedex.data.datasources

import com.brunodegan.pokedex.data.models.PokemonDetails
import com.brunodegan.pokedex.data.models.PokemonGraphQLApiDataModel

interface PokemonsDataSource {
    suspend fun getPokemons(): PokemonGraphQLApiDataModel
    suspend fun getPokemonById(id: String): PokemonDetails
    suspend fun getPokemonByName(name: String): PokemonDetails
}