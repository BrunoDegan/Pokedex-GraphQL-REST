package com.brunodegan.pokedex.domain.getPokemonsUseCase

import com.brunodegan.pokedex.base.errors.ResponseApiErrorException
import com.brunodegan.pokedex.data.repositories.PokedexRepository
import com.brunodegan.pokedex.ui.mappers.PokemonListViewDataMapper
import com.brunodegan.pokedex.ui.models.PokemonListViewData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapNotNull
import org.koin.core.annotation.Factory

@Factory
class GetPokemonsDataUseCaseImpl(
    private val mapper: PokemonListViewDataMapper,
    private val repository: PokedexRepository
) : GetPokemonsDataUseCase {
    override suspend fun invoke(errorMessage: String?): Flow<List<PokemonListViewData>?> {
        return repository.getPokemons().distinctUntilChanged().mapNotNull {
            mapper.map(it.getOrThrow())
        }.catch {
            throw ResponseApiErrorException(message = errorMessage)
        }
    }
}