package com.brunodegan.pokedex.data.api

import com.google.gson.annotations.SerializedName

data class GetAllPokemonsGraphQLRequestData(
    @SerializedName("query")
    val query: String = GRAPHQL_QUERY,
    @SerializedName("operationName")
    val operationName: String = "getItems"
)

const val GRAPHQL_QUERY = """
   query getItems {
      pokemon: pokemon_v2_pokemon_aggregate {
        nodes {
          id
          name
          pokemon_v2_pokemontypes {
            pokemon_v2_type {
              name
            }
          }
          pokemon_v2_pokemonabilities {
            pokemon_v2_ability {
              name
            }
          }
          pokemon_v2_pokemonsprites {
            sprites(path: "other.dream_world.front_default")
          }
        }
      }
    }
"""
