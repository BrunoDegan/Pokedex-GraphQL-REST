package com.brunodegan.pokedex.ui.mappers

import com.brunodegan.pokedex.base.ui.BaseMapper
import com.brunodegan.pokedex.data.models.PokemonDetailsRestApiModel
import com.brunodegan.pokedex.data.models.PokemonTypes
import com.brunodegan.pokedex.data.models.Stats
import com.brunodegan.pokedex.ui.models.PokemonDetailsViewData
import org.koin.core.annotation.Factory

@Factory
class PokemonDetailsViewDataMapper :
    BaseMapper<PokemonDetailsRestApiModel, PokemonDetailsViewData> {

    override fun map(input: PokemonDetailsRestApiModel): PokemonDetailsViewData {
        return PokemonDetailsViewData(
            id = input.id.toString(),
            name = input.name ?: "",
            height = input.height.toString(),
            weight = input.weight.toString(),
            imgUrl = input.sprites?.other?.dreamWorld?.frontDefault ?: "",
            types = input.types.getTypesList(),
            species = input.species?.name ?: "",
            stats = input.stats.toStatsList()
        )
    }

    private fun List<Stats>?.toStatsList(): List<Pair<String, String>> {
        return this?.mapNotNull { statItem ->
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
