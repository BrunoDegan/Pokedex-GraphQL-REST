package com.brunodegan.pokedex.data.datasources

import com.brunodegan.pokedex.data.models.GetAllPokemonsGraphQLApiModel
import com.brunodegan.pokedex.data.models.PokemonDetailsRestApiModel

interface PokemonsDataSource {
    suspend fun getPokemons(): GetAllPokemonsGraphQLApiModel
    suspend fun getPokemonById(id: Int): PokemonDetailsRestApiModel
}