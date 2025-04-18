package com.brunodegan.pokedex.ui.mappers

import com.brunodegan.pokedex.base.ui.BaseMapper
import com.brunodegan.pokedex.data.models.GetAllPokemonsGraphQLApiModel
import com.brunodegan.pokedex.data.models.PokemonAbilities
import com.brunodegan.pokedex.data.models.PokemonGraphQLTypes
import com.brunodegan.pokedex.data.models.PokemonImages
import com.brunodegan.pokedex.ui.models.PokemonListViewData
import org.koin.core.annotation.Factory

@Factory
class PokemonListViewDataMapper :
    BaseMapper<GetAllPokemonsGraphQLApiModel?, List<PokemonListViewData>> {
    override fun map(input: GetAllPokemonsGraphQLApiModel?): List<PokemonListViewData> {

        val pokemonListViewDataList = mutableListOf<PokemonListViewData>()
        input?.pokemon?.pokemonNodes?.forEach { apiData ->
            pokemonListViewDataList.add(
                PokemonListViewData(
                    id = apiData.pokemonId.toId(),
                    name = apiData.pokemonName ?: "",
                    imgUrl = apiData.pokemonSprites.getImageUrl(),
                    types = apiData.pokemonTypes.getTypes(),
                    abilities = apiData.abilities.toAbilitiesList()
                )
            )
        }

        return pokemonListViewDataList
    }

    private fun String?.toId(): Int {
        return this?.toIntOrNull() ?: 0
    }

    private fun List<PokemonAbilities>?.toAbilitiesList(): List<String> {
        return this?.mapNotNull { it.ability?.name } ?: emptyList()
    }

    private fun List<PokemonGraphQLTypes>?.getTypes(): List<String> {
        return this?.mapNotNull { it.type.name } ?: emptyList()
    }

    private fun List<PokemonImages>?.getImageUrl(): String {
        return this?.firstOrNull()?.spritesUrl ?: ""
    }
}