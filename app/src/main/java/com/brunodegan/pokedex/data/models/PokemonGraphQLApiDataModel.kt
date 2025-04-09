package com.brunodegan.pokedex.data.models

import com.brunodegan.pokedex.base.network.base.ApiData
import com.google.gson.annotations.SerializedName

data class GetAllPokemonsGraphQLResponseData(
    @SerializedName("pokemon")
    val pokemon: PokemonNodes?
) : ApiData()

data class PokemonNodes(
    @SerializedName("nodes")
    val pokemonNodes: List<PokemonGraphQLModel>?
) : ApiData()

data class PokemonGraphQLModel(
    @SerializedName("id") val pokemonId: String?,
    @SerializedName("name") val pokemonName: String?,
    @SerializedName("pokemon_v2_pokemontypes") val pokemonTypes: List<PokemonGraphQLTypes>?,
    @SerializedName("pokemon_v2_pokemonsprites") val pokemonSprites: List<PokemonImages>?,
    @SerializedName("pokemon_v2_pokemonabilities") val abilities: List<PokemonAbilities>?,
) : ApiData()

data class PokemonAbilities(@SerializedName("pokemon_v2_ability") val ability: PokemonAbilityName?) :
    ApiData()

data class PokemonAbilityName(
    @SerializedName("name") val name: String?,
) : ApiData()

data class PokemonGraphQLTypes(
    @SerializedName("pokemon_v2_type") val type: Type,
) : ApiData()

data class Type(
    @SerializedName("name") val name: String?,
) : ApiData()

data class PokemonImages(
    @SerializedName("sprites") val spritesUrl: String?
) : ApiData()

data class Sprites(
    @SerializedName("front_default") val frontDefault: String?
) : ApiData()

data class PokemonDetails(
    @SerializedName("id") val id: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("height") val height: String?,
    @SerializedName("weight") val weight: String?,
    @SerializedName("types") val types: List<Type>?,
    @SerializedName("sprites") val sprites: List<Sprites>?
) : ApiData()
