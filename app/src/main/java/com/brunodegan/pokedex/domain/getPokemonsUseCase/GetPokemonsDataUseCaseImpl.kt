package com.brunodegan.pokedex.domain.getPokemonsUseCase

import com.brunodegan.pokedex.base.network.base.NetworkResponse
import com.brunodegan.pokedex.data.models.PokemonsGraphQLApiModel
import com.brunodegan.pokedex.data.repositories.PokedexRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
class GetPokemonsDataUseCaseImpl(
    private val repository: PokedexRepository,
) : GetPokemonsDataUseCase {
    override suspend fun invoke(): Flow<NetworkResponse<PokemonsGraphQLApiModel>> =
        repository.getPokemons()
}