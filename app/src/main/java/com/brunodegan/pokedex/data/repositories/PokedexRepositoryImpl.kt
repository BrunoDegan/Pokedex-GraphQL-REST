package com.brunodegan.pokedex.data.repositories

import com.brunodegan.pokedex.base.errors.customErrorHandler
import com.brunodegan.pokedex.data.datasources.PokemonsDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import org.koin.core.annotation.Factory

@Factory
class PokedexRepositoryImpl(
    private val remoteDataSource: PokemonsDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : PokedexRepository {
    override suspend fun getPokemons() =
        remoteDataSource.getPokemons().flowOn(dispatcher).customErrorHandler()

    override suspend fun getPokemonById(id: Int) =
        remoteDataSource.getPokemonById(id = id).flowOn(dispatcher).customErrorHandler()
}