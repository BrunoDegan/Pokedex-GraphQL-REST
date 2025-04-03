package com.brunodegan.pokedex.ui.mappers

import com.brunodegan.pokedex.base.ui.BaseMapper
import com.brunodegan.pokedex.data.models.PokemonGraphQLApiDataModel
import com.brunodegan.pokedex.data.models.PokemonGraphQLTypes
import com.brunodegan.pokedex.data.models.PokemonImages
import com.brunodegan.pokedex.ui.models.PokemonListViewData
import org.koin.core.annotation.Factory

@Factory
class PokemonListViewDataMapper :
    BaseMapper<PokemonGraphQLApiDataModel?, List<PokemonListViewData>> {
    override fun map(input: PokemonGraphQLApiDataModel?): List<PokemonListViewData> {
        val pokemonListViewDataList = mutableListOf<PokemonListViewData>()

        input?.pokemonAggregate?.nodes?.forEach { apiData ->
            pokemonListViewDataList.add(
                PokemonListViewData(
                    id = apiData.pokemonId ?: "",
                    name = apiData.pokemonName ?: "",
                    imgUrl = getImageUrl(apiData.pokemonSprites),
                    types = getTypes(apiData.pokemonTypes),
                )
            )
        }
        return pokemonListViewDataList
    }

    private fun getTypes(types: List<PokemonGraphQLTypes>?): List<String> {
        val typesList = mutableListOf<String>()
        types?.forEach { type ->
            type.type.name?.let { typesList.add(it) }
        }
        return typesList
    }

    private fun getImageUrl(sprites: List<PokemonImages?>): String {
        return sprites.firstOrNull()?.spritesUrl ?: ""
    }
}