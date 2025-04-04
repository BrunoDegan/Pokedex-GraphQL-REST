package com.brunodegan.pokedex.data.api

import com.brunodegan.pokedex.base.network.base.NetworkResponse
import com.brunodegan.pokedex.data.models.PokemonDetails
import retrofit2.http.GET
import retrofit2.http.Path

interface RestApiService {

    @GET(POKEMON_DETAIL_BY_ID)
    suspend fun getPokemonDetailsById(@Path(ID) id: String): NetworkResponse<PokemonDetails>

    @GET(POKEMON_DETAIL_BY_NAME)
    suspend fun getPokemonDetailsByName(@Path(NAME) name: String): NetworkResponse<PokemonDetails>

    companion object {
        const val ID = "{id}"
        const val NAME = "{name}"
        const val BASE_URL = "https://pokeapi.co/api/v2/"
        const val POKEMON_DETAIL_BY_ID = "$BASE_URL/pokemon/$ID/"
        const val POKEMON_DETAIL_BY_NAME = "$BASE_URL/pokemon/$NAME/"
    }
}