package com.brunodegan.pokedex.ui.mappers

import com.brunodegan.pokedex.base.ui.BaseMapper
import com.brunodegan.pokedex.data.models.PokemonDetailsRestApiModel
import com.brunodegan.pokedex.data.models.PokemonTypes
import com.brunodegan.pokedex.data.models.Stats
import com.brunodegan.pokedex.ui.models.PokemonDetailsViewData
import org.koin.core.annotation.Factory

@Factory
class PokemonDetailsViewDataMapper :
    BaseMapper<PokemonDetailsRestApiModel?, PokemonDetailsViewData> {

    override fun map(input: PokemonDetailsRestApiModel?): PokemonDetailsViewData {
        return PokemonDetailsViewData(
            name = input?.species?.name.orEmpty(),
            height = input?.height?.times(10).toString(),
            weight = input?.weight?.times(0.1).toString(),
            imgUrl = input?.sprites?.other?.dreamWorld?.frontDefault ?: "",
            types = input?.types.getTypesList(),
            stats = input?.stats.toStatsList()
        )
    }

    private fun List<Stats>?.toStatsList(): List<Pair<String, String>> {
        return this?.map { statItem ->
            val statName = statItem.stat?.name
            Pair(statName ?: "Unknown Stat", "${statItem.baseStat}")
        } ?: emptyList()
    }

    private fun List<PokemonTypes>?.getTypesList(): List<String> {
        return this?.mapNotNull { typeItem ->
            typeItem.type?.name?.takeIf {
                it.isNotEmpty()
            }
        } ?: emptyList()
    }
}
