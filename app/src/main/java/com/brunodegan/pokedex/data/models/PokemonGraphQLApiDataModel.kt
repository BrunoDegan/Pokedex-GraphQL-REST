package com.brunodegan.pokedex.data.models

import com.brunodegan.pokedex.base.network.base.BaseApiData
import com.google.gson.annotations.SerializedName

data class PokemonGraphQLApiDataModel(
    @SerializedName("pokemon_v2_pokemon_aggregate") val pokemonAggregate: PokemonAggregate?,
) : BaseApiData()

data class PokemonAggregate(
    @SerializedName("nodes") val nodes: List<PokemonGraphQLModel>?
) : BaseApiData()

data class PokemonGraphQLModel(
    @SerializedName("id") val pokemonId: String?,
    @SerializedName("name") val pokemonName: String?,
    @SerializedName("pokemon_v2_pokemontypes") val pokemonTypes: List<PokemonGraphQLTypes>?,
    @SerializedName("pokemon_v2_pokemonsprites") val pokemonSprites: List<PokemonImages?>,
) : BaseApiData()

data class PokemonGraphQLTypes(
    @SerializedName("pokemon_v2_type") val type: Type,
) : BaseApiData()

data class Type(
    @SerializedName("name") val name: String?,
) : BaseApiData()

data class PokemonImages(
    @SerializedName("id") val id: String?, @SerializedName("sprites") val spritesUrl: String?
) : BaseApiData()

data class Sprites(
    @SerializedName("front_default") val frontDefault: String?
) : BaseApiData()

data class PokemonDetails(
    @SerializedName("id") val id: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("height") val height: String?,
    @SerializedName("weight") val weight: String?,
    @SerializedName("types") val types: List<Type>?,
    @SerializedName("sprites") val sprites: List<Sprites>?
) : BaseApiData()
