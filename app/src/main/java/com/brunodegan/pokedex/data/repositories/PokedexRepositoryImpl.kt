package com.brunodegan.pokedex.data.repositories

import com.brunodegan.pokedex.base.network.base.ErrorType
import com.brunodegan.pokedex.base.network.base.NetworkResponse
import com.brunodegan.pokedex.data.datasources.PokemonsDataSource
import com.brunodegan.pokedex.data.models.PokemonDetailsRestApiModel
import com.brunodegan.pokedex.data.models.PokemonsGraphQLApiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.core.annotation.Factory

@Factory
class PokedexRepositoryImpl(
    private val remoteDataSource: PokemonsDataSource,
) : PokedexRepository {

    override suspend fun getPokemons(): Flow<NetworkResponse<PokemonsGraphQLApiModel>> = flow {
        runCatching {
            remoteDataSource.getPokemons()
        }.onSuccess {
            emit(NetworkResponse.Success(it))
        }.onFailure {
            emit(NetworkResponse.Error(ErrorType.Generic(it.message)))
        }
    }


    override suspend fun getPokemonById(id: Int): Flow<NetworkResponse<PokemonDetailsRestApiModel>> =
        flow {
            runCatching {
                remoteDataSource.getPokemonById(id)
            }.onSuccess {
                emit(NetworkResponse.Success(it))
            }.onFailure {
                emit(NetworkResponse.Error(ErrorType.Generic(it.message)))
            }
        }

}