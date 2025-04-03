package com.brunodegan.pokedex.data.api

import com.google.gson.annotations.SerializedName

data class GetAllPokemonsGraphQLRequestData(
    @SerializedName("query")
    val query: String = GRAPHQL_QUERY,
    @SerializedName("operationName")
    val operationName: String = "getItems"
)

const val GRAPHQL_QUERY = "query getItems {\n" +
        "          pokemon_v2_pokemon_aggregate {\n" +
        "            nodes {\n" +
        "              id\n" +
        "              name\n" +
        "              pokemon_v2_pokemontypes {\n" +
        "                pokemon_v2_type {\n" +
        "                  name\n" +
        "                }\n" +
        "              }\n" +
        "              pokemon_v2_pokemonsprites {\n" +
        "                id\n" +
        "                sprites(path: \"other.dream_world.front_default\")\n" +
        "              }\n" +
        "            }\n" +
        "          }\n" +
        "        }"
