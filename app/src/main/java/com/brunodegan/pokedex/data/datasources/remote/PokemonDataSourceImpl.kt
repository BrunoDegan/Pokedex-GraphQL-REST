package com.brunodegan.pokedex.data.datasources.remote

import com.brunodegan.pokedex.data.api.GetAllPokemonsGraphQLRequestData
import com.brunodegan.pokedex.data.api.GraphQLApiService
import com.brunodegan.pokedex.data.api.RestApiService
import com.brunodegan.pokedex.data.datasources.PokemonsDataSource
import com.brunodegan.pokedex.data.models.GetAllPokemonsGraphQLResponseData
import com.brunodegan.pokedex.data.models.PokemonDetailsRestApiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.koin.core.annotation.Single

@Single
class PokemonsRemoteDataSourceImpl(
    private val graphQlApi: GraphQLApiService,
    private val restApi: RestApiService
) : PokemonsDataSource {
    override suspend fun getPokemons(): Flow<GetAllPokemonsGraphQLResponseData> {
        return flowOf(graphQlApi.getAllPokemons(GetAllPokemonsGraphQLRequestData()).data)
    }

    override suspend fun getPokemonById(id: Int): Flow<PokemonDetailsRestApiModel> {
        return flowOf(restApi.getPokemonDetailsById(id = id).data)
    }
}