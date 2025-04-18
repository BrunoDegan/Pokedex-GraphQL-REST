package com.brunodegan.pokedex.data.models

import com.brunodegan.pokedex.base.network.base.ApiData
import com.google.gson.annotations.SerializedName

data class PokemonDetailsRestApiModel(
    @SerializedName("species") val species: Species?,
    @SerializedName("height") val height: Float?,
    @SerializedName("weight") val weight: Float?,
    @SerializedName("types") val types: List<PokemonTypes>?,
    @SerializedName("abilities") val abilities: List<Abilities>?,
    @SerializedName("sprites") val sprites: PokemonSprites?,
    @SerializedName("stats") val stats: List<Stats>?
) : ApiData()

data class Abilities(
    @SerializedName("ability")
    val ability: Ability?
) : ApiData()

data class Ability(
    @SerializedName("name")
    val name: String?,
) : ApiData()

data class Species(
    @SerializedName("name")
    val name: String?,
) : ApiData()

data class Stat(
    @SerializedName("name")
    val name: String?,
) : ApiData()

data class Stats(
    @SerializedName("base_stat")
    val baseStat: Int?,
    @SerializedName("stat")
    val stat: Stat?
) : ApiData()

data class TypeName(
    @SerializedName("name")
    val name: String?,
) : ApiData()

data class PokemonTypes(
    @SerializedName("type")
    val type: TypeName?
) : ApiData()

data class PokemonSpriteDreamWorld(
    @SerializedName("front_default")
    val frontDefault: String?
) : ApiData()

data class PokemonSpriteOther(
    @SerializedName("dream_world")
    val dreamWorld: PokemonSpriteDreamWorld?
) : ApiData()

data class PokemonSprites(
    @SerializedName("other")
    val other: PokemonSpriteOther?
) : ApiData()
