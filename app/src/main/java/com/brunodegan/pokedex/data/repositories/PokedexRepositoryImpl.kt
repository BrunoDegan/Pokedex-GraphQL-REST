package com.brunodegan.pokedex.data.repositories

import com.brunodegan.pokedex.base.errors.customErrorHandler
import com.brunodegan.pokedex.data.datasources.PokemonsDataSource
import com.brunodegan.pokedex.data.models.PokemonDetails
import com.brunodegan.pokedex.data.models.PokemonGraphQLApiDataModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.koin.core.annotation.Factory

@Factory
class PokedexRepositoryImpl(
    private val remoteDataSource: PokemonsDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : PokedexRepository {
    override suspend fun getPokemons(): Flow<Result<PokemonGraphQLApiDataModel>> {
        return flow {
            emit(remoteDataSource.getPokemons())
        }.flowOn(dispatcher).customErrorHandler()
    }

    override suspend fun getPokemonById(id: String): Flow<Result<PokemonDetails>> {
        return flow {
            emit(remoteDataSource.getPokemonById(id = id))
        }.flowOn(dispatcher).customErrorHandler()
    }

    override suspend fun getPokemonByName(name: String): Flow<Result<PokemonDetails>> {
        return flow {
            emit(remoteDataSource.getPokemonByName(name = name))
        }.flowOn(dispatcher).customErrorHandler()
    }
}