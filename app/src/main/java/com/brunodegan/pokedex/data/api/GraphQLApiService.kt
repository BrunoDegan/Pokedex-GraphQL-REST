package com.brunodegan.pokedex.data.api

import com.brunodegan.pokedex.base.network.base.NetworkResponse
import com.brunodegan.pokedex.data.models.PokemonGraphQLApiDataModel
import retrofit2.http.Body
import retrofit2.http.POST

interface GraphQLApiService {

    @POST(GRAPHQL_BASE_URL)
    suspend fun getAllPokemons(
        @Body requestData: GetAllPokemonsGraphQLRequestData
    ): NetworkResponse<PokemonGraphQLApiDataModel>

    companion object {
        const val GRAPHQL_BASE_URL = "https://beta.pokeapi.co/graphql/v1beta/"
    }
}