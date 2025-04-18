package com.brunodegan.pokedex.data.datasources.remote

import com.brunodegan.pokedex.data.api.GetAllPokemonsGraphQLRequestData
import com.brunodegan.pokedex.data.api.GraphQLApiService
import com.brunodegan.pokedex.data.api.RestApiService
import com.brunodegan.pokedex.data.datasources.PokemonsDataSource
import com.brunodegan.pokedex.data.models.GetAllPokemonsGraphQLApiModel
import com.brunodegan.pokedex.data.models.PokemonDetailsRestApiModel
import org.koin.core.annotation.Single

@Single
class PokemonsRemoteDataSourceImpl(
    private val graphQlApi: GraphQLApiService,
    private val restApi: RestApiService
) : PokemonsDataSource {

    override suspend fun getPokemons(): GetAllPokemonsGraphQLApiModel {
        return graphQlApi.getAllPokemons(GetAllPokemonsGraphQLRequestData()).data
    }

    override suspend fun getPokemonById(id: Int): PokemonDetailsRestApiModel {
        return restApi.getPokemonDetailsById(id = id).data
    }
}