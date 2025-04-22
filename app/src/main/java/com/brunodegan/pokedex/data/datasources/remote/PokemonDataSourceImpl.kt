package com.brunodegan.pokedex.data.datasources.remote

import com.brunodegan.pokedex.data.api.GetAllPokemonsGraphQLRequestData
import com.brunodegan.pokedex.data.api.GraphQLApiService
import com.brunodegan.pokedex.data.api.RestApiService
import com.brunodegan.pokedex.data.datasources.PokemonsDataSource
import org.koin.core.annotation.Single

@Single
class PokemonsRemoteDataSourceImpl(
    private val graphQlApi: GraphQLApiService, private val restApi: RestApiService
) : PokemonsDataSource {

    override suspend fun getPokemons() = graphQlApi.getAllPokemons(
        GetAllPokemonsGraphQLRequestData()
    )

    override suspend fun getPokemonById(id: Int) = restApi.getPokemonDetailsById(id = id)
}