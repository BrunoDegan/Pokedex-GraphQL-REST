package com.brunodegan.pokedex.data.models

import com.google.gson.annotations.SerializedName

data class PokemonsGraphQLApiModel(
    @SerializedName("data")
    val data: GraphQLData?,
)

data class GraphQLData(
    @SerializedName("pokemon")
    val pokemon: PokemonNodes?
)

data class PokemonNodes(
    @SerializedName("nodes")
    val pokemonNodes: List<PokemonGraphQLModel>?
)

data class PokemonGraphQLModel(
    @SerializedName("id") val pokemonId: String?,
    @SerializedName("name") val pokemonName: String?,
    @SerializedName("pokemon_v2_pokemontypes") val pokemonTypes: List<PokemonGraphQLTypes>?,
    @SerializedName("pokemon_v2_pokemonsprites") val pokemonSprites: List<PokemonImages>?,
    @SerializedName("pokemon_v2_pokemonabilities") val abilities: List<PokemonAbilities>?,
)

data class PokemonAbilities(@SerializedName("pokemon_v2_ability") val ability: PokemonAbilityName?)

data class PokemonAbilityName(
    @SerializedName("name") val name: String?,
)

data class PokemonGraphQLTypes(
    @SerializedName("pokemon_v2_type") val type: Type?,
)

data class Type(
    @SerializedName("name") val name: String?,
)

data class PokemonImages(
    @SerializedName("sprites") val sprites: String?
)