package com.brunodegan.pokedex.data.models

import com.google.gson.annotations.SerializedName

data class PokemonDetailsRestApiModel(
    @SerializedName("species") val species: Species?,
    @SerializedName("height") val height: Float?,
    @SerializedName("weight") val weight: Float?,
    @SerializedName("types") val types: List<PokemonTypes>?,
    @SerializedName("abilities") val abilities: List<Abilities>?,
    @SerializedName("sprites") val sprites: PokemonSprites?,
    @SerializedName("stats") val stats: List<Stats>?
)

data class Abilities(
    @SerializedName("ability")
    val ability: Ability?
)

data class Ability(
    @SerializedName("name")
    val name: String?,
)

data class Species(
    @SerializedName("name")
    val name: String?,
)

data class Stat(
    @SerializedName("name")
    val name: String?,
)

data class Stats(
    @SerializedName("base_stat")
    val baseStat: Int?,
    @SerializedName("stat")
    val stat: Stat?
)

data class TypeName(
    @SerializedName("name")
    val name: String?,
)

data class PokemonTypes(
    @SerializedName("type")
    val type: TypeName?
)

data class PokemonSpriteDreamWorld(
    @SerializedName("front_default")
    val frontDefault: String?
)

data class PokemonSpriteOther(
    @SerializedName("dream_world")
    val dreamWorld: PokemonSpriteDreamWorld?
)

data class PokemonSprites(
    @SerializedName("other")
    val other: PokemonSpriteOther?
)
