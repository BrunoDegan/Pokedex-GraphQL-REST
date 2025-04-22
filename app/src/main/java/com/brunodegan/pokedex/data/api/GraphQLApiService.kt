package com.brunodegan.pokedex.data.api

import com.brunodegan.pokedex.data.models.PokemonsGraphQLApiModel
import retrofit2.http.Body
import retrofit2.http.POST

interface GraphQLApiService {

    @POST(GRAPHQL_BASE_URL)
    suspend fun getAllPokemons(
        @Body requestData: GetAllPokemonsGraphQLRequestData
    ): PokemonsGraphQLApiModel

    companion object {
        const val GRAPHQL_BASE_URL = "https://beta.pokeapi.co/graphql/v1beta/"
    }
}