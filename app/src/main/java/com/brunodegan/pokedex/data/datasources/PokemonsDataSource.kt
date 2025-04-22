package com.brunodegan.pokedex.data.datasources

import com.brunodegan.pokedex.data.models.PokemonDetailsRestApiModel
import com.brunodegan.pokedex.data.models.PokemonsGraphQLApiModel

interface PokemonsDataSource {
    suspend fun getPokemons(): PokemonsGraphQLApiModel
    suspend fun getPokemonById(id: Int): PokemonDetailsRestApiModel
}