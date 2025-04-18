package com.brunodegan.pokedex.ui.models

data class PokemonDetailsViewData(
    val name: String,
    val height: String,
    val weight: String,
    val imgUrl: String,
    val types: List<String>,
    val stats: List<Pair<String, String>>
)