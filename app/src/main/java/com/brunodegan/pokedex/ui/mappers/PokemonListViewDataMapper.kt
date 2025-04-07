package com.brunodegan.pokedex.ui.mappers

import com.brunodegan.pokedex.base.ui.BaseMapper
import com.brunodegan.pokedex.data.models.GetAllPokemonsGraphQLResponseData
import com.brunodegan.pokedex.data.models.PokemonAbilities
import com.brunodegan.pokedex.data.models.PokemonGraphQLTypes
import com.brunodegan.pokedex.data.models.PokemonImages
import com.brunodegan.pokedex.ui.models.PokemonListViewData
import org.koin.core.annotation.Factory

@Factory
class PokemonListViewDataMapper :
    BaseMapper<GetAllPokemonsGraphQLResponseData?, List<PokemonListViewData>> {
    override fun map(input: GetAllPokemonsGraphQLResponseData?): List<PokemonListViewData> {

        val pokemonListViewDataList = mutableListOf<PokemonListViewData>()
        input?.pokemon?.pokemonNodes?.forEach { apiData ->
            pokemonListViewDataList.add(
                PokemonListViewData(
                    id = apiData.pokemonId ?: "",
                    name = apiData.pokemonName ?: "",
                    imgUrl = getImageUrl(apiData.pokemonSprites),
                    types = getTypes(apiData.pokemonTypes),
                    abilities = getAbilities(apiData.abilities)
                )
            )
        }

        return pokemonListViewDataList
    }

    private fun getAbilities(apiData: List<PokemonAbilities>?): List<String> {
        val abilitiesList = mutableListOf<String>()
        apiData?.forEach { ability ->
            ability.ability?.name?.let { abilitiesList.add(it) }
        }

        return abilitiesList
    }

    private fun getTypes(apiData: List<PokemonGraphQLTypes>?): List<String> {
        val typesList = mutableListOf<String>()
        apiData?.forEach { type ->
            type.type.name?.let { typesList.add(it) }
        }
        return typesList
    }

    private fun getImageUrl(apiData: List<PokemonImages>?): String {
        return apiData?.firstOrNull()?.spritesUrl ?: ""
    }
}