package com.brunodegan.pokedex.data.api

import com.brunodegan.pokedex.base.network.base.NetworkResponse
import com.brunodegan.pokedex.data.models.PokemonDetailsRestApiModel
import retrofit2.http.GET
import retrofit2.http.Path

interface RestApiService {

    @GET(POKEMON_DETAIL_BY_ID)
    suspend fun getPokemonDetailsById(
        @Path("id") id: Int
    ): NetworkResponse<PokemonDetailsRestApiModel>

    companion object {
        const val REST_API_BASE_URL = "https://pokeapi.co/api/v2/"
        private const val POKEMON_DETAIL_BY_ID = "pokemon/{id}"
    }
}