package com.brunodegan.pokedex.domain.getPokemonDetailsById

import com.brunodegan.pokedex.base.errors.ResponseApiErrorException
import com.brunodegan.pokedex.data.repositories.PokedexRepository
import com.brunodegan.pokedex.ui.mappers.PokemonDetailsViewDataMapper
import com.brunodegan.pokedex.ui.models.PokemonDetailsViewData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory

@Factory
class GetPokemonDetailsByIdUseCaseImpl(
    private val repository: PokedexRepository,
    private val mapper: PokemonDetailsViewDataMapper
) : GetPokemonDetailsByIdUseCase {
    override suspend fun invoke(id: String): Flow<PokemonDetailsViewData?> {
        return repository.getPokemonById(id = id).map {
            mapper.map(it.getOrThrow())
        }.catch {
            throw ResponseApiErrorException(it.message)
        }
    }
}