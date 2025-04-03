package com.brunodegan.pokedex.data.datasources.remote

import com.brunodegan.pokedex.data.api.GetAllPokemonsGraphQLRequestData
import com.brunodegan.pokedex.data.api.GraphQLApiService
import com.brunodegan.pokedex.data.api.RestApiService
import com.brunodegan.pokedex.data.datasources.PokemonsDataSource
import com.brunodegan.pokedex.data.models.PokemonDetails
import com.brunodegan.pokedex.data.models.PokemonGraphQLApiDataModel
import org.koin.core.annotation.Single

@Single
class PokemonsRemoteDataSourceImpl(
    private val graphQlApi: GraphQLApiService,
    private val restApi: RestApiService
) :
    PokemonsDataSource {
    override suspend fun getPokemons(): PokemonGraphQLApiDataModel {
        return graphQlApi.getAllPokemons(GetAllPokemonsGraphQLRequestData()).data
    }

    override suspend fun getPokemonById(id: String): PokemonDetails {
        return restApi.getPokemonDetailsById(id).data
    }

    override suspend fun getPokemonByName(name: String): PokemonDetails {
        return restApi.getPokemonDetailsByName(name).data
    }
}