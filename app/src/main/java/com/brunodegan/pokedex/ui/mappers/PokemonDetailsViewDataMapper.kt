package com.brunodegan.pokedex.ui.mappers

import com.brunodegan.pokedex.base.ui.BaseMapper
import com.brunodegan.pokedex.data.models.PokemonDetails
import com.brunodegan.pokedex.data.models.Type
import com.brunodegan.pokedex.ui.models.PokemonDetailsViewData
import org.koin.core.annotation.Factory

@Factory
class PokemonDetailsViewDataMapper : BaseMapper<PokemonDetails?, PokemonDetailsViewData> {
    override fun map(input: PokemonDetails?): PokemonDetailsViewData {
        return PokemonDetailsViewData(
            id = input?.id ?: "",
            name = input?.name ?: "",
            height = input?.height ?: "",
            weight = input?.weight ?: "",
            imgUrl = input?.sprites?.first()?.frontDefault ?: "",
            types = getTypesList(input?.types),
        )
    }

    private fun getTypesList(types: List<Type>?): List<String> {
        return types?.mapNotNull { typeItem ->
            typeItem.name?.takeIf {
                it.isNotEmpty()
            }
        } ?: emptyList()
    }
}
