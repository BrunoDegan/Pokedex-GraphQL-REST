package com.brunodegan.pokedex.domain.getPokemonDetailsById

import com.brunodegan.pokedex.base.network.base.NetworkResponse
import com.brunodegan.pokedex.data.models.PokemonDetailsRestApiModel
import com.brunodegan.pokedex.data.repositories.PokedexRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
class GetPokemonDetailsByIdUseCaseImpl(
    private val repository: PokedexRepository,
) : GetPokemonDetailsByIdUseCase {
    override suspend fun invoke(id: Int): Flow<NetworkResponse<PokemonDetailsRestApiModel>> =
        repository.getPokemonById(id = id)

}