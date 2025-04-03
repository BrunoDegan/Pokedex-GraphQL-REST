package com.brunodegan.pokedex.domain.getPokemonDetailsByName

import com.brunodegan.pokedex.base.errors.ResponseApiErrorException
import com.brunodegan.pokedex.data.repositories.PokedexRepository
import com.brunodegan.pokedex.ui.mappers.PokemonDetailsViewDataMapper
import com.brunodegan.pokedex.ui.models.PokemonDetailsViewData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory

@Factory
class GetPokemonDetailsByNameUseCaseImpl(
    private val repository: PokedexRepository,
    private val mapper: PokemonDetailsViewDataMapper
) : GetPokemonDetailsByNameUseCase {
    override suspend fun invoke(name: String): Flow<PokemonDetailsViewData?> {
        return repository.getPokemonByName(name = name).map {
            mapper.map(it.getOrThrow())
        }.catch {
            throw ResponseApiErrorException(it.message)
        }
    }

}