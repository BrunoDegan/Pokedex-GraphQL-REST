package com.brunodegan.pokedex.data.repositories

import com.brunodegan.pokedex.data.datasources.PokemonsDataSource
import com.brunodegan.pokedex.data.models.GetAllPokemonsGraphQLApiModel
import com.brunodegan.pokedex.data.models.PokemonDetailsRestApiModel
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

    override suspend fun getPokemons(): Flow<GetAllPokemonsGraphQLApiModel> = flow {
        emit(remoteDataSource.getPokemons())
    }.flowOn(dispatcher)

    override suspend fun getPokemonById(id: Int): Flow<PokemonDetailsRestApiModel> = flow {
        emit(remoteDataSource.getPokemonById(id = id))
    }.flowOn(dispatcher)
}